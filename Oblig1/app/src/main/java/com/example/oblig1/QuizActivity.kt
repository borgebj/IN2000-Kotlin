package com.example.oblig1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class QuizActivity : AppCompatActivity() {

    val sporsmaal: MutableList<Question> = mutableListOf()
    lateinit var quiz: TextView
    lateinit var textview1: TextView
    lateinit var riktig: Button
    lateinit var galt: Button
    lateinit var textview2: TextView
    lateinit var videre: Button

    fun lagSporsmaal() {
        sporsmaal.add(Question("Sarajevo er hovedstaden i Serbia", false))
        sporsmaal.add(Question("Originalfargen til cola er grønn", false))
        sporsmaal.add(Question("Pranab Mukherjee er presidenten i India i år", false))
        sporsmaal.add(Question("Farris er eid av Ringnes", true))
        sporsmaal.add(Question("Er det sant at Espresso House har opphav i Norge?", false))
        sporsmaal.add(Question("Er det sant at man ikke kan kjøre NVIDIA skjermkort med AMD prosessor?", false))
        sporsmaal.add(Question("Var det kjente spillet 'Half life 2' opprinnelig lagt ut i 2004?", true))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Tilegner ID'er
        textview1 = findViewById(R.id.textView3)
        riktig = findViewById(R.id.riktig)
        galt = findViewById(R.id.galt)
        textview2 = findViewById(R.id.textView4)
        videre = findViewById(R.id.restart)

        lagSporsmaal()

        // starter selve quizen
        startQuiz()

        // restart quiz
        videre.setOnClickListener {
            textview2.text = "Poeng: 0 / ${sporsmaal.size}"
            startQuiz()
            riktig.visibility = View.VISIBLE
            galt.visibility = View.VISIBLE
        }
    }


    // funksjon som starter quizen
    fun startQuiz() {
        var score = 0
        var nummer = 0

        // setter forste sporsmaal ved oppstart
        textview1.text = sporsmaal[nummer].sporsmaal

        fun svar() {
            if (nummer < sporsmaal.size) { nummer++ }

            // hvis alle sporsmaal er gaatt gjennom:
            if (nummer == sporsmaal.size) {
                textview2.text = "Quiz over\nDu fikk: ${score} / ${sporsmaal.size} poeng"
                riktig.visibility = View.INVISIBLE
                galt.visibility = View.INVISIBLE
            } else {
                textview2.text = "Poeng: ${score} / ${sporsmaal.size}"
                textview1.text = sporsmaal[nummer].sporsmaal
            }
        }

        // TRUE-knappen
        riktig.setOnClickListener {
            if (sporsmaal[nummer].svar) {
                if (score < sporsmaal.size && nummer < sporsmaal.size) {
                    score++
                }
            }
            svar()
        }

        // FALSE-knappen
        galt.setOnClickListener {
            textview1.text = sporsmaal[nummer].sporsmaal
            if (!sporsmaal[nummer].svar) {
                if (score < sporsmaal.size && nummer < sporsmaal.size) {
                    score++
                }
            }
            svar()
        }
    }
}