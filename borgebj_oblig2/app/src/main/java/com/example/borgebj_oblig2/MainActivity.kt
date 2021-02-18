package com.example.borgebj_oblig2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    var parties = mutableListOf<AlpacaParty>()
    lateinit var recycleview: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        assignId()
        parse()

    }

    fun assignId() {
    }

    fun parse() {

        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.uio.no")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(API::class.java)

        val call = service.hentInfo()

        call.enqueue(object : Callback<partyList> {
            override fun onResponse(call: Call<partyList>, response: Response<partyList>) {
                val body = response.body()
                val p = body?.parties
                if (p != null) {
                    for (party in p) {
                        parties.add(party)
                    }
                }
            }
            override fun onFailure(call: Call<partyList>, t: Throwable) {
                Toast.makeText(applicationContext, "Error reading JSON", Toast.LENGTH_LONG)
            }
        })
    }
}
