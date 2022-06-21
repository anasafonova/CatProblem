package com.isgneuro.android.catproblem.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CatApi {
    @GET("/api/v1/meta")
    fun fetchCatParameters(): Call<String>

    @GET("/api/v1/cats")
    fun fetchCat(): Call<String>

    @POST("/cat/update")
    fun updateCat(@Body cat: String): Call<String>
}