package com.example.borgebj_oblig2

import com.google.gson.annotations.SerializedName

data class AlpacaParty(
    val id: String,
    val name: String,
    val leader: String,
    val img: String,
    val color: String,
    var votes: Int = 0,
    var total: Int = 0)

data class AlpacaListe(@SerializedName("parties") val parties: List<AlpacaParty>)

data class IDholder(@SerializedName("id") val id: String)


