package com.example.borgebj_oblig2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.util.*


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    //region [globale variabler]
    var partier: MutableList<AlpacaParty> = mutableListOf()
    val baseURL = "https://www.uio.no"
    val gson = Gson()
    lateinit var adapter: PartyAdapter
    lateinit var recycler: RecyclerView
    lateinit var spinner: Spinner
    lateinit var progressBar: ProgressBar
    //endregion

    //  main-funksjon
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addID()
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        addAdapter()
        addSpinner()
        parse()
    }

    //region [add] metoder som "adder" ting til programmet
    fun addID() {
        recycler = findViewById(R.id.recycler)
        spinner = findViewById(R.id.spinner)
        progressBar = findViewById(R.id.progressBar)
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    fun addAdapter() {
        adapter = PartyAdapter(partier)
        recycler.adapter = adapter
    }

    fun addSpinner() {
        ArrayAdapter.createFromResource(
                this,
                R.array.distrikter,
                android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this
    }
    //endregion

    // henter JSON/XML via KHTTP -> til String
    fun getData(del: String): String {
        val full = "$baseURL$del"
        return khttp.get(full).text
    }

    // metode som parser fra start-data fra JSON
    fun parse() {
        //region (coroutine-1) starter en coroutine for aa parse
        CoroutineScope(Dispatchers.IO).launch {
            val response = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/alpacaparties.json")

            val json: AlpacaListe = gson.fromJson(response, AlpacaListe::class.java)
            val lister: List<AlpacaParty> = json.parties.toList()

            // legger til listene med AlpacaParty inn i den globale listen
            for (parti in lister) {
                partier.add(parti)
            }
        } //endregion
    }

    //region [spinner] Metoder som tilhorer spinneren
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        // resetter og viser progressbaren paa starten av oppgaven
        progressBar.progress = 0
        progressBar.visibility = View.VISIBLE

        // nullstiller stemmene til alle partier
        fun resetPartier() {
            for (parti in partier) {
                parti.votes = 0
                parti.total = 0
            }
        }

        //region [Ekstafunksjonalitet] Hjelpemetode som venter og oppdaterer progressbaren
        suspend fun vent() {
            while (progressBar.progress < 100) {
                progressBar.progress += (15..35).random()
                delay(90)
            }
        }//endregion

        //Hjelpemetode som teller stemmer for hver distrikt
        fun tellStemmer(distrikt: String) {
            val liste: List<IDholder> = gson.fromJson(distrikt, Array<IDholder>::class.java).toList()
            resetPartier()

            // tildeler stemmer og totalt antall til hvert parti
            for (i in liste.indices) {
                for (j in partier.indices) {
                    partier[j].total = liste.size
                    if (liste[i].id == partier[j].id) {
                        when (liste[i].id) {
                            "1" -> partier[0].votes++
                            "2" -> partier[1].votes++
                            "3" -> partier[2].votes++
                            "4" -> partier[3].votes++
                        }
                    }
                }
            }
        }

        // metode som utforer oppgaven for valg av distrikt tre. parsing -> tildeling
        fun distriktTre(tre: String) {
            resetPartier()
            val parser = XmlParser()
            val d3 = parser.parse(tre.byteInputStream())
            for (i in d3.indices) {
                if (d3[i].id == partier[i].id) {
                    partier[i].votes = d3[i].votes!!
                    partier[i].total = parser.totalVotes
                }
            }
        }

        //region [Ekstafunksjonalitet] metoder som henter stemmer fra alle distrikter og viser de
        fun hentAlle(en: String, to: String, tre: String) {

            resetPartier()
            // JSON-distrikter
            val listeEn: List<IDholder> = gson.fromJson(en, Array<IDholder>::class.java).toList()
            val listeTo: List<IDholder> = gson.fromJson(to, Array<IDholder>::class.java).toList()

            // XML-distriktet
            val parser = XmlParser()
            val listeTre: List<districtThree> = parser.parse(tre.byteInputStream())

            val total = (listeEn.size + listeTo.size + parser.totalVotes)
            for (parti in partier) {
                parti.total = total
                fun tellStemmer(liste: List<IDholder>) {
                    for (x in liste) { if (parti.id == x.id) parti.votes++ }
                }
                // teller stemmer for hvert distrikt via hjelpemetode ↑
                tellStemmer(listeEn)
                tellStemmer(listeTo)
                for (c in listeTre) { if (c.id == parti.id) parti.votes += c.votes!! }
            }
        }//endregion

        //region - (coroutine-2)
        CoroutineScope(Dispatchers.IO).launch {

            // valg av spinner-item
            when (parent?.getItemAtPosition(position).toString()) {
                "Distrikt 1" -> {
                    vent()
                    val en = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district1.json")
                    tellStemmer(en)
                }
                "Distrikt 2" -> {
                    vent()
                    val to = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district2.json")
                    tellStemmer(to)
                }
                "Distrikt 3" -> {
                    vent()
                    val tre = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district3.xml")
                    distriktTre(tre)
                }
                "Vis alle distrikter" -> {
                    vent()
                    val en = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district1.json")
                    val to = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district2.json")
                    val tre = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district3.xml")
                    hentAlle(en, to ,tre)
                }
            }
            // oppdaterer cardview og gjemmer progressbar
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }
        } //endregion
    }

    // implementert metode som ikke blir brukt (men fremdeles krav)
    override fun onNothingSelected(parent: AdapterView<*>?) {} //endregion
}