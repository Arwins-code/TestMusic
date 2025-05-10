package com.arwin.testmusicapps.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arwin.testmusicapps.data.model.MusicResult
import com.arwin.testmusicapps.data.network.ApiService
import com.arwin.testmusicapps.data.network.RetrofitInstance
import com.arwin.testmusicapps.data.repository.MusicRepository
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: MusicRepository
    private val musicList = MutableLiveData<List<MusicResult>>()
    val musicTracks: LiveData<List<MusicResult>>get() = musicList
    var currentPlayingIndex = MutableLiveData<Int?>(null)
    val currentTrackIndex = MutableLiveData<Int>()

    init {
        val apiService = RetrofitInstance.getRetrofitInstance().create(ApiService::class.java)
        repo = MusicRepository(apiService)
    }

    fun updateMusic(data: String) {
        viewModelScope.launch {
            repo.searchMusic(data) { music ->
                musicList.postValue(music)
            }
        }
    }
}