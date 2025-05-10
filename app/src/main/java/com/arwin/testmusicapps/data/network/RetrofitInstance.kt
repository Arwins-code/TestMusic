package com.arwin.testmusicapps.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://itunes.apple.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    val apiService: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }

    fun getRetrofitInstance(): Retrofit = retrofit
}