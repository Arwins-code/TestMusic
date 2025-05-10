package com.arwin.testmusicapps.data.repository

import com.arwin.testmusicapps.data.model.MusicResponse
import com.arwin.testmusicapps.data.model.MusicResult
import com.arwin.testmusicapps.data.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MusicRepository(private val apiService: ApiService) {
    fun searchMusic(term: String, callback: (List<MusicResult>) -> Unit) {
        apiService.searchMusic(term).enqueue(object : Callback<MusicResponse> {
            override fun onResponse(
                call: Call<MusicResponse>,
                response: Response<MusicResponse>
            ) {
                if (response.isSuccessful) {
                    callback(response.body()?.results ?: emptyList())
                } else {
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                callback(emptyList())
            }
        })
    }
}