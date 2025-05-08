package com.example.week3

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.week3.databinding.ItemStorageBinding

class StorageAdapter (
    private val dataSet: MutableList<Song>,
    private val listener: OnItemButtonClickListener
): RecyclerView.Adapter<StorageAdapter.ViewHolder> () {
    inner class ViewHolder(binding: ItemStorageBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.storageDeleteBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onButtonClick(position)
                }
            }
        }
        val albumArt: ImageView = binding.storageAlbumArt
        val title: TextView = binding.storageSongTitle
        val artist: TextView = binding.storageArtist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.albumArt.setImageResource(dataSet[position].albumArt)
        holder.title.text = dataSet[position].title
        holder.artist.text = dataSet[position].artist
    }

    override fun getItemCount() = dataSet.size

    interface OnItemButtonClickListener {
        fun onButtonClick(position: Int)
    }
}