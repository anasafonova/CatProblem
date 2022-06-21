package com.isgneuro.android.catproblem.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "CatFetcher"

class CatFetcher {
    private val catApi: CatApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.21:8000/")
            .addConverterFactory(ScalarsConverterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create())
            .build()
        Log.d(TAG, "Init catApi")
        catApi = retrofit.create(CatApi::class.java)
    }

    fun fetchCatParameters() { //: LiveData<List<String>> {
        Log.d(TAG, "Fetch cat parameters")
        val responseLiveData: MutableLiveData<List<String>> = MutableLiveData()
        val catParamRequest: Call<String> = catApi.fetchCatParameters()
        catParamRequest.enqueue(object: Callback<String> {
            override fun onFailure(
                call: Call<String>,
                t: Throwable
            ) {
                Log.e(TAG, "Failed to fetch parameters", t)
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "Response received ${response.body()}")
                //Log.d(TAG, response.body().toString())
                //responseLiveData.value = listOf()
            }
        })
        //return responseLiveData
    }

}