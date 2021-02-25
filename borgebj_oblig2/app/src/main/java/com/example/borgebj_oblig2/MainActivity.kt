package com.example.borgebj_oblig2

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.io.InputStream
import java.util.*


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
        addAdapter()
        addSpinner()
        parse()
        progress()
    }

    fun progress() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar) as ProgressBar
        TODO("Lag hele shiten")
    }

    // henter fulle JSON via KHTTP
    fun getData(del: String): String {
        val full = "$baseURL$del"
        return khttp.get(full).text
    }

    // metode som parser fra JSON
    fun parse() {
        val parties = "/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/alpacaparties.json"

        // starter en coroutine for aa parse
        CoroutineScope(Dispatchers.IO).launch {
            val response = getData(parties)

            val json: AlpacaListe = gson.fromJson(response, AlpacaListe::class.java)
            val lister: List<AlpacaParty> = json.parties.toList()

            // legger til listene med AlpacaParty inn i den globale listen
            for (parti in lister) {
                partier.add(parti)
            }
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
            // JSON / XML
            val en = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district1.json")
            val to = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district2.json")
            val tre = getData("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/district3.xml")

            // hjelpemetode som teller stemmer for hver distrikt
            fun tellStemmer(distrikt: String) {
                CoroutineScope(Dispatchers.IO).launch {
                    val liste: List<IDholder>? =
                        gson.fromJson(distrikt, Array<IDholder>::class.java).toList()
                    val parti1: Int = 0
                    val parti2: Int = 0
                    val parti3: Int = 0
                    val parti4: Int = 0
                    val total = liste?.size
                    val p: MutableList<Int> = mutableListOf(parti1, parti2, parti3, parti4)

                    // sjekker ID'er og
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
                    for (i in partier.indices) {
                        partier[i].votes = p[i].toString()
                        partier[i].total = total.toString()
                    }
                    withContext(Dispatchers.Main) {
                        refreshAdapter()
                    }
                }
            }


            val verdi: String = parent?.getItemAtPosition(position).toString()
            when (verdi) {
                "Distrikt 1" -> {
                    tellStemmer(en)

                }
                "Distrikt 2" -> {
                    tellStemmer(to)
                }
                // bruker ingen dynamisk metode pga egen parser istedenfor API
                "Distrikt 3" -> {
                    withContext(Dispatchers.Main) {
                        val inputStream: InputStream = tre.byteInputStream()
                        val parser = XmlParser()
                        val d3 = parser.parse(inputStream)
                        for (i in d3.indices) {
                            if (d3[i].id == partier[i].id) {
                                partier[i].votes = d3[i].votes.toString()
                                partier[i].total = parser.totalVotes.toString()
                            }
                        }
                        refreshAdapter()
                    }
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    // refresher adapter og dens innhold
    fun refreshAdapter() {
        adapter.notifyDataSetChanged()
    }
}