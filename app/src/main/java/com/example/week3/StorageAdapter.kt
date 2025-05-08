package com.example.week3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class StorageAdapter (
    private val dataSet: Array<Song>,
    private val fragment: Fragment
): RecyclerView.Adapter<StorageAdapter.ViewHolder> () {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumArt: ImageView = view.findViewById(R.id.storageAlbumArt)
        val title: TextView = view.findViewById(R.id.storageSongTitle)
        val artist: TextView = view.findViewById(R.id.storageArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_storage, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.albumArt.setImageResource(dataSet[position].albumArt)
        holder.title.text = dataSet[position].title
        holder.artist.text = dataSet[position].artist
    }

    override fun getItemCount() = dataSet.size

}