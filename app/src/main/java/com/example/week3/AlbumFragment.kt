package com.example.week3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.week3.databinding.FragmentAlbumBinding
import com.example.week3.databinding.FragmentHomeBinding

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         val albumTitle = arguments?.getString("title")
        binding.albumTitle.text = albumTitle

        binding.albumBackBtn.setOnClickListener {
            findNavController().popBackStack()

            //findNavController().popBackStack(R.id.homeFragment, false)
            // homeFragment까지 백스택에서 제거 (inclusive를 true로 하면 home도 제거)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}