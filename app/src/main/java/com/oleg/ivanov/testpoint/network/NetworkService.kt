package com.oleg.ivanov.testpoint.network

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("/api/test/points")
    fun getPoints(
        @Query("count") count: Int,
    ): Call<JsonElement>
}