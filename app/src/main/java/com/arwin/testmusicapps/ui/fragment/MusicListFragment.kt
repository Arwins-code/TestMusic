package com.arwin.testmusicapps.ui.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arwin.testmusicapps.R
import com.arwin.testmusicapps.data.model.MusicResult
import com.arwin.testmusicapps.databinding.FragmentMusicListBinding
import com.arwin.testmusicapps.ui.adapter.MusicAdapter
import com.arwin.testmusicapps.ui.viewmodel.MusicViewModel

class MusicListFragment : Fragment() {
    private var _binding: FragmentMusicListBinding? = null
    private val binding get() = _binding!!
    private lateinit var musicAdapter: MusicAdapter
    private lateinit var viewModel: MusicViewModel

    private var mediaPlayer: MediaPlayer? = null
    private var currentSongIndex = 0
    private var musicResponse: List<MusicResult> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MusicViewModel::class.java]

        musicAdapter = MusicAdapter(
            onClickListener = { music ->
                // play music
                music.previewUrl?.let { playMusic(uri = it) }
                binding.playerControl.root.visibility = View.VISIBLE
            },
            onItemSelected = { index ->
                viewModel.currentPlayingIndex.postValue(index)
            })

//        recyclerview data set
        binding.rcListMusic.layoutManager = LinearLayoutManager(requireContext())
        binding.rcListMusic.adapter = musicAdapter
        // update list from view model
        viewModel.updateMusic("noah")
        searchMusic()

        viewModel.currentPlayingIndex.observe(viewLifecycleOwner) { index ->
            musicAdapter.updatePlayingIndex(index)
        }

        viewModel.musicTracks.observe(viewLifecycleOwner) { music ->
            musicResponse = music
            musicAdapter.updateData(music)
        }

        viewModel.currentTrackIndex.observe(viewLifecycleOwner) { index ->
            currentSongIndex = index
        }

        binding.playerControl.ibPlay.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                pauseMusic()
            } else {
                playMusic(
                    uri = musicResponse.getOrNull(currentSongIndex)?.previewUrl ?: ""
                )
            }
        }

        binding.playerControl.ibPlay.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        }
        binding.playerControl.ibPrevious.setOnClickListener {
            playPreviousTrack()
        }
        binding.playerControl.ibNext.setOnClickListener {
            playNextTrack()
        }

        return binding.root
    }

    private fun searchMusic() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()

                if (query.isNotEmpty()) {
                    viewModel.updateMusic(query)
                    stopMusic()
                    binding.playerControl.root.visibility = View.GONE
                } else {
                    viewModel.updateMusic("picolo")
                    stopMusic()
                    binding.playerControl.root.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })
    }

    private fun setupProgress() {
        binding.playerControl.sbMusic.max = mediaPlayer?.duration ?: 0

        // Update SeekBar in real-time
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    binding.playerControl.sbMusic.progress = it.currentPosition
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(runnable)

        // SeekBar listener
        binding.playerControl.sbMusic.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    if (seekBar != null) {
                        mediaPlayer!!.seekTo(seekBar.progress)
                    }
                }
            }
        })
    }

    private fun playMusic(uri: String) {
        if (uri.isEmpty()) return

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer()

        try {
            mediaPlayer?.setDataSource(uri)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.isLooping = false
            mediaPlayer?.setOnPreparedListener {
                it.start()
                setupProgress()
                binding.playerControl.ibPlay.setImageResource(R.drawable.ic_pause)
            }
            mediaPlayer?.setOnCompletionListener {
                playNextTrack()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error playing audio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playPreviousTrack() {
        if (currentSongIndex > 0) {
            currentSongIndex--
            viewModel.currentPlayingIndex.postValue(currentSongIndex)
            playMusic(uri = musicResponse.getOrNull(currentSongIndex)?.previewUrl ?: "")
        } else {
            Toast.makeText(requireContext(), "No more previous tracks", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playNextTrack() {
        if (currentSongIndex < musicResponse.size - 1) {
            currentSongIndex++
            viewModel.currentPlayingIndex.postValue(currentSongIndex)
            playMusic(uri = musicResponse.getOrNull(currentSongIndex)?.previewUrl ?: "")
        } else {
            Toast.makeText(requireContext(), "No more next tracks", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        binding.playerControl.ibPlay.setImageResource(R.drawable.ic_paly)
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        viewModel.currentPlayingIndex.postValue(null)
        musicAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}