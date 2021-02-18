package com.example.oblig1

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    // variabler for EditText, TextView og Button
    lateinit var editText : EditText
    lateinit var textView : TextView
    lateinit var sjekk : Button
    lateinit var videre : Button

    private fun closeKeyboard(view: View) {
        val hide = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hide.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // henter ID'ene til de ulike elementene
        editText = findViewById(R.id.editText1)
        textView = findViewById(R.id.textView1)
        sjekk = findViewById(R.id.sjekk)
        videre = findViewById(R.id.videre1)

        // forste onclick
        sjekk.setOnClickListener {

            // sjekker om det er et palindrom ved aa reversere den og sjekke om den er lik
            if (editText.text.toString().toLowerCase().trim() == editText.text.toString().toLowerCase().trim().reversed()) {
                textView.text = "${editText.text} er et palindrom"
            }
            else {
                textView.text = "${editText.text} er ikke et palindrom"
            }
            editText.text = null
            closeKeyboard(editText)
        }


        // andre onclick
        videre.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            startActivity(intent)
        }

    }
}