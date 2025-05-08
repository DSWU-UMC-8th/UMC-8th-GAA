package com.example.week3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.week3.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var bannerAdapter: RecommendedBannerAdapter
    private lateinit var bannerList: Array<RecommendedBannerInfo>
    private val sliderHandler = Handler(Looper.getMainLooper())
    private val slideDelay: Long = 3000

    private val sliderRunnable = object : Runnable {
        override fun run() {
            val viewPager = binding.recommendBannerViewPager
            val nextItem = (viewPager.currentItem + 1) % bannerAdapter.itemCount
            viewPager.setCurrentItem(nextItem, true)
            sliderHandler.postDelayed(this, slideDelay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recommendedBanner
        bannerList = arrayOf(
            RecommendedBannerInfo("1번 배너", R.drawable.ic_launcher_background),
            RecommendedBannerInfo("2번 배너", R.drawable.album),
            RecommendedBannerInfo("3번 배너", R.drawable.album),
        )

        bannerAdapter = RecommendedBannerAdapter(bannerList)
        binding.recommendBannerViewPager.adapter = bannerAdapter
        binding.recommendBannerViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.bannerIndicator.setViewPager(binding.recommendBannerViewPager)

        binding.recommendBannerViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, slideDelay)
            }
        })


        // releasedAlbum
        val albumList = arrayOf(
            AlbumInfo(R.drawable.album, "Album 1", "Artist 1"),
            AlbumInfo(R.drawable.album, "Album 2", "Artist 2"),
            AlbumInfo(R.drawable.album, "Album 3", "Artist 3")
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = ReleasedAlbumAdapter(albumList, this)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, slideDelay)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        sliderHandler.removeCallbacks(sliderRunnable)
    }
}