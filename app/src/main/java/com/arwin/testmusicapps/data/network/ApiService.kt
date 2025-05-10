package com.arwin.testmusicapps.data.network

import com.arwin.testmusicapps.data.model.MusicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //apii https://itunes.apple.com/search?term=ed
    @GET("search")
    fun searchMusic(@Query("term") term: String): Call<MusicResponse>
}