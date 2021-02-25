package com.example.borgebj_oblig2

import com.google.gson.annotations.SerializedName

data class AlpacaParty(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("leader") val leader: String,
    @SerializedName("img") val img: String,
    @SerializedName("color") val color: String,
    var votes: String = "",
    var total: String = "") {}

data class AlpacaListe(@SerializedName("parties") val parties: List<AlpacaParty>)

data class IDholder(@SerializedName("id") val id: String)


