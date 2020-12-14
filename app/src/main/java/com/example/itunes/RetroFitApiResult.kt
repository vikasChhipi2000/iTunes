package com.example.itunes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroFitApiResult {
    @GET("search")
    fun getResult(@Query("term") term:String) : Call<Model.Results>
}