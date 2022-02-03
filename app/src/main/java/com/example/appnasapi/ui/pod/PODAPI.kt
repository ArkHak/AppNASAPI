package com.example.appnasapi.ui.pod

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface PODAPI {
    @GET("planetary/apod")
    fun getPOD(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<PODServerResponseData>
}
