package com.example.oblig1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*


class ConverterActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var editText: EditText
    lateinit var spinner: Spinner
    lateinit var konverter: Button
    lateinit var videre: Button
    lateinit var textView: TextView


    // lukker "keyboard"-funksjon (etter knappetrykk)
    private fun closeKeyboard(view: View) {
        val hide = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        editText = findViewById(R.id.editText2)
        spinner = findViewById(R.id.spinner)
        konverter = findViewById(R.id.konverter)
        videre = findViewById(R.id.videre2)
        textView = findViewById(R.id.textView2)

        ArrayAdapter.createFromResource(this, R.array.enheter, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        spinner.onItemSelectedListener = this

        // bytter til neste aktivitet
        videre.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }

    // feilmelding av toast som kommer om ingenting er valgt
    override fun onNothingSelected(parent: AdapterView<*>?) { }

    // kode som skjer etter en ting fra spinneren er valgt
    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {

        // oppretter variabler og verdier
        val current_unit: String = parent.getItemAtPosition(pos).toString()
        val toast = Toast.makeText(parent.context, "Tekstfelt er tomt.", Toast.LENGTH_LONG)

        // kaller paa lukk keyboard etter knappetrykk
        konverter.setOnClickListener {

            if (editText.text.toString().isEmpty()) {
                toast.show()
            }
            else {
                val myNumber: Int = editText.text.toString().toInt()
                when (current_unit) {
                    "Fluid ounce (fl oz)" -> textView.text = "${"%.2f".format(myNumber * 0.02957).toDouble()} Liters."
                    "Cup (cp)" -> textView.text = "${"%.2f".format(myNumber * 0.23659).toDouble()} Liters."
                    "Gallon (gal)" -> textView.text = "${"%.2f".format(myNumber * 3.78541).toDouble()} Liters."
                    "Hogshead" -> textView.text = "${"%.2f".format(myNumber * 238.481).toDouble()} Liters."
                    else -> {
                        toast.show()
                    }
                }
            }
            closeKeyboard(editText)
        }
    }
}