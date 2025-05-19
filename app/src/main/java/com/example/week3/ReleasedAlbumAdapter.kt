package com.example.week3

import android.annotation.SuppressLint
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
import com.example.week3.databinding.LayoutReleasedBinding

class ReleasedAlbumAdapter (
    private val fragment: Fragment,
    private val listener: OnItemButtonClickListener
): RecyclerView.Adapter<ReleasedAlbumAdapter.ViewHolder> () {

    val dataSet = ArrayList<AlbumInfo>()

    inner class ViewHolder(binding: LayoutReleasedBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.releasedPlayBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onButtonClick(position)
                }
            }
        }
        val albumArt: ImageView = binding.releasedAlbumart
        val title: TextView = binding.releasedTitle
        val artist: TextView = binding.releasedArtist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutReleasedBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
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

    interface OnItemButtonClickListener {
        fun onButtonClick(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<AlbumInfo>) {
        this.dataSet.clear()
        this.dataSet.addAll(albums)

        notifyDataSetChanged()
    }

}