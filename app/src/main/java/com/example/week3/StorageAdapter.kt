package com.example.week3

import android.annotation.SuppressLint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.week3.databinding.ItemStorageBinding

class StorageAdapter (): RecyclerView.Adapter<StorageAdapter.ViewHolder> () {

    private val songs = ArrayList<Song>()

    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

//    inner class ViewHolder(binding: ItemStorageBinding) : RecyclerView.ViewHolder(binding.root) {
//        init {
//            binding.storageDeleteBtn.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION) {
//                    listener.onButtonClick(position)
//                }
//            }
//        }
//        val albumArt: ImageView = binding.storageAlbumArt
//        val title: TextView = binding.storageSongTitle
//        val artist: TextView = binding.storageArtist
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStorageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(songs[position])
        holder.binding.storageDeleteBtn.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSong(position)
        }
    }

    override fun getItemCount() = songs.size

    interface OnItemButtonClickListener {
        fun onButtonClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>) {
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun removeSong(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemStorageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song){
            binding.storageAlbumArt.setImageResource(song.albumArt)
            binding.storageSongTitle.text = song.title
            binding.storageArtist.text = song.artist
        }
    }
}