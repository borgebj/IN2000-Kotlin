package com.example.borgebj_oblig2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.InputStream
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
        progressBar = findViewById(R.id.progressBar)
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
        val parties = "/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/alpacaparties.json"
        //region (coroutine-1) starter en coroutine for aa parse
        CoroutineScope(Dispatchers.IO).launch {
            val response = getData(parties)

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

        //Hjelpemetode som venter og oppdaterer progressbaren
        suspend fun vent() {
            while (progressBar.progress < 100) {
                progressBar.progress += (15..25).random()
                delay(100)
            }
        }

        //Hjelpemetode som teller stemmer for hver distrikt
        fun tellStemmer(distrikt: String) {
            val liste: List<IDholder>? = gson.fromJson(distrikt, Array<IDholder>::class.java).toList()

            val parti1: Int = 0
            val parti2: Int = 0
            val parti3: Int = 0
            val parti4: Int = 0
            val total = liste?.size
            val p: MutableList<Int> = mutableListOf(parti1, parti2, parti3, parti4)

            // sjekker ID'er og teller stemmer
            if (liste != null) {
                for (nyId in liste) {
                    for (parti in partier) {
                        if (nyId.id == parti.id) {
                            when (nyId.id) {
                                "1" -> p[0]++
                                "2" -> p[1]++
                                "3" -> p[2]++
                                "4" -> p[3]++
                            }
                        }
                    }
                }
            }
            // tildeler stemmer og totalt antall til hvert parti
            for (i in partier.indices) {
                partier[i].votes = p[i].toString()
                partier[i].total = total.toString()
            }
        }

        // metode som utforer oppgaven for valg av distrikt tre. parsing -> tildeling
        suspend fun distriktTre() {
            val tre = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district3.xml")
            val inputStream: InputStream = tre.byteInputStream()
            val parser = XmlParser()
            val d3 = parser.parse(inputStream)
            vent()
            for (i in d3.indices) {
                if (d3[i].id == partier[i].id) {
                    partier[i].votes = d3[i].votes.toString()
                    partier[i].total = parser.totalVotes.toString()
                }
            }
        }

        //region - (coroutine-2)
        CoroutineScope(Dispatchers.IO).launch {

            // valg av spinner-item
            val verdi: String = parent?.getItemAtPosition(position).toString()
            when (verdi) {
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
                    distriktTre()
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