package com.example.borgebj_oblig2

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    // globale variabler
    var partier: MutableList<AlpacaParty> = mutableListOf()
    val baseURL = "https://www.uio.no"
    val gson = Gson()
    lateinit var adapter: PartyAdapter
    lateinit var recycler: RecyclerView
    lateinit var spinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.recycler)
        spinner = findViewById(R.id.spinner)
        recycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        addSpinner()
        parse()
    }

    // henter fulle JSON via KHTTP
    fun getData(del: String): String {
        val full = "$baseURL$del"
        return khttp.get(full).text
    }

    // metode som parser fra JSON
    fun parse() {
        val parties = "/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/alpacaparties.json"

        CoroutineScope(Dispatchers.IO).launch {
            val response = getData(parties)

            val json: AlpacaListe = gson.fromJson(response, AlpacaListe::class.java)
            val lister: List<AlpacaParty> = json.parties.toList()

            // legger til listene med AlpacaParty inn i den globale listen
            for (parti in lister) {
                partier.add(parti)
            }
            addAdapter()
        }
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val distriktEn = "/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district1.json"
            val distriktTo = "/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district2.json"
            val distriktTre = "/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district3.xml"

            // JSON / XML
            val en = getData(distriktEn)
            val to = getData(distriktTo)
            val tre = getData(distriktTre)

            // hjelpemetode som teller stemmer for hver distrikt
            fun tellStemmer(distrikt: String) {
                val liste: List<IDholder>? = gson.fromJson(distrikt, Array<IDholder>::class.java).toList()

                var parti1: Int = 0
                var parti2: Int = 0
                var parti3: Int = 0
                var parti4: Int = 0
                var total = liste?.size

                if (liste != null) {
                    for (nyId in liste) {
                        for (parti in partier) {
                            if (nyId.id == parti.id) {
                                when(nyId.id){
                                    "1" -> parti1++
                                    "2" -> parti2++
                                    "3" -> parti3++
                                    "4" -> parti4++
                                }
                            }
                        }
                    }
                }
                recycler.getView
            }


            val verdi: String = parent?.getItemAtPosition(position).toString()
            when (verdi) {
                "Distrikt 1" -> {
                    tellStemmer(en)

                }
                "Distrikt 2" -> {
                    tellStemmer(to)
                }
                "Distrikt 3" -> {
                    println("Distrikt 3")
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}