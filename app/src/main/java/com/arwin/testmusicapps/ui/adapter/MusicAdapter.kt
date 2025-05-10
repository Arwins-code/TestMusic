package com.arwin.testmusicapps.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arwin.testmusicapps.data.model.MusicResponse
import com.arwin.testmusicapps.data.model.MusicResult
import com.arwin.testmusicapps.databinding.ItemMusicBinding
import com.bumptech.glide.Glide

class MusicAdapter(
    private val onClickListener: (MusicResult) -> Unit,
    private val onItemSelected: (Int) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {
    private var musicList: MutableList<MusicResult> = mutableListOf()
    private var currentPlayingIndex: Int? = null

    class MusicViewHolder(val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateData(data: List<MusicResult>) {
        musicList.clear()
        musicList.addAll(data)
        notifyDataSetChanged()
    }

    fun updatePlayingIndex(index: Int?) {
        currentPlayingIndex = index
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.binding.tvSongName.text = music.trackName
        holder.binding.tvArtistName.text = music.artistName
        holder.binding.tvAlbum.text = music.collectionName
        holder.binding.animationPlay.visibility =
            if (position == currentPlayingIndex) View.VISIBLE else View.GONE
        Glide.with(holder.itemView.context)
            .load(music.artworkUrl100)
            .into(holder.binding.ivBannerMusic)

        holder.binding.root.setOnClickListener {
            onClickListener(music)
            onItemSelected(position)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}