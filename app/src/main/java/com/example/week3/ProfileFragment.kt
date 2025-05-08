package com.example.week3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week3.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storageList = arrayOf(
            Song("노래", "가수", R.drawable.album),
            Song("노래2", "가수2", R.drawable.album),
            Song("노래3", "가수3", R.drawable.album),
            Song("노래4", "가수4", R.drawable.album),
            Song("노래5", "가수5", R.drawable.album),
            Song("노래6", "가수6", R.drawable.album),
            Song("노래7", "가수7", R.drawable.album),
            Song("노래7", "가수7", R.drawable.album),
            Song("노래8", "가수8", R.drawable.album),
            Song("노래9", "가수9", R.drawable.album)
        )

        binding.storageRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.storageRecyclerView.adapter = StorageAdapter(storageList, this)
    }

}