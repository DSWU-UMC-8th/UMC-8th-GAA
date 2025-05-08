package com.example.week3

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class ReleasedAlbumAdapter (
    private val dataSet: Array<AlbumInfo>,
    private val fragment: Fragment
): RecyclerView.Adapter<ReleasedAlbumAdapter.ViewHolder> () {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumArt: ImageView = view.findViewById(R.id.released_albumart)
        val title: TextView = view.findViewById(R.id.released_title)
        val artist: TextView = view.findViewById(R.id.released_artist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_released, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.albumArt.setImageResource(dataSet[position].albumArt)
        holder.title.text = dataSet[position].albumTitle
        holder.artist.text = dataSet[position].albumArtist

        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("title", dataSet[position].albumTitle)
            }
            fragment.findNavController().navigate(R.id.action_home_to_album, bundle)
        }

        // 괜찮은 방법인가?
        // backStack을 위해서는 nav_graph가 좋다?
         /*
        holder.itemView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val activity = v!!.context as AppCompatActivity
                val albumFragment = AlbumFragment()
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, albumFragment).addToBackStack(null).commit()
            }
        })
         */
    }

    override fun getItemCount() = dataSet.size

}