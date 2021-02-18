package com.example.borgebj_oblig2

import com.google.gson.annotations.SerializedName

// en klasse for en liste med Alpaca-partier

data class partyList(
        @SerializedName("parties") var parties: List<AlpacaParty>
){
}