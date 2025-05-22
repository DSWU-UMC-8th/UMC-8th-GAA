package com.example.week3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week3.databinding.FragmentProfileBinding
import com.example.week3.databinding.FragmentStorageBinding

class StorageFragment : Fragment(), StorageAdapter.OnItemButtonClickListener {
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!
    private var storageList = mutableListOf<Song>()
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)
        songDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    override fun onButtonClick(position: Int) {
        storageList.removeAt(position)
        binding.storageRecyclerView.adapter?.notifyItemRemoved(position)
    }

    private fun initRecyclerView(){
        binding.storageRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val storageAdapter = StorageAdapter()

        storageAdapter.setMyItemClickListener(object : StorageAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })

        binding.storageRecyclerView.adapter = storageAdapter

        storageAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }

}