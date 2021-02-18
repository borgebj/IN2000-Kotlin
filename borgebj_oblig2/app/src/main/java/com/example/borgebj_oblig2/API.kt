package com.example.borgebj_oblig2

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface API {

    @GET("/studier/emner/matnat/ifi/IN2000/v21/obligatoriske-oppgaver/alpakkaland/alpacaparties.json")
    fun hentInfo(): Call<partyList>
}