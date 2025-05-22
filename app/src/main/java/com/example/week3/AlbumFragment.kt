package com.example.week3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.week3.databinding.FragmentAlbumBinding
import com.example.week3.databinding.FragmentHomeBinding
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!

    private var gson: Gson = Gson()
    private var isLiked: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, AlbumInfo::class.java)
        isLiked = islikeedAlbum(album.id)
        setInit(album)
        setOnClickListeners(album)

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

    private fun setInit(album: AlbumInfo) {
        binding.albumTitle.text = album.albumTitle.toString()
        if (isLiked) {
            binding.albumLikeCB.setChecked(true)
        }
    }

    private fun getJwt(): Int {
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }

    private fun likeAlbum(userId: Int, albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    private fun islikeedAlbum(albumId: Int): Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId: Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }

    private fun dislikeAlbum(albumId: Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.albumDao().disLikedAlbum(userId, albumId)
    }

    private fun setOnClickListeners(album: AlbumInfo) {
        val userId = getJwt()
        binding.albumLikeCB.setOnClickListener {
            if (isLiked) {
                binding.albumLikeCB.setChecked(false)
                dislikeAlbum(album.id)
            } else {
                binding.albumLikeCB.setChecked(true)
                likeAlbum(userId, album.id)
            }
        }
    }
}