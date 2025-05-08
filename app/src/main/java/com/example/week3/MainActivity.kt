package com.example.week3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.week3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val MUSIC_TITLE = "music_title"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var song: Song
    private lateinit var timer: Timer

    private val getResultText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val returnString = result.data?.getStringExtra(MUSIC_TITLE)
            val toast = Toast.makeText(this, returnString, Toast.LENGTH_LONG)
            toast.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setBottomNavi()
        setSample()

        binding.miniPlayBtn.setOnClickListener {
            song.isPlaying = true
            startTimer()
        }

        binding.miniPlayer.setOnClickListener {
            song.second = (timer.mills / 1000).toInt()
            val intent = Intent(this, SongActivity::class.java).apply {
                putExtra("title", song.title)
                putExtra("artist", song.artist)
                putExtra("albumArt", song.albumArt)
                putExtra("second", song.second)
                putExtra("playTime", song.playTime)
                putExtra("isPlaying", song.isPlaying)
            }
            getResultText.launch(intent)
            //startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun setSample() {
        binding.songTitleTV.text = "라일락"
        binding.artistTV.text = "아이유 (IU)"
        song = Song(
            binding.songTitleTV.text.toString(),
            binding.artistTV.text.toString(),
            R.drawable.album,
            0,
            60,
            false
        )
    }

    private fun setBottomNavi() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavi.setupWithNavController(navController)

        val options = NavOptions.Builder()
            .setEnterAnim(R.anim.fragment_slide_in)
            .setExitAnim(R.anim.fragment_slide_out)
            .build()

        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navi_home -> {
                    navController.navigate(R.id.homeFragment, null, options)
                    true
                }

                R.id.navi_search -> {
                    navController.navigate(R.id.searchFragment, null, options)
                    true
                }

                R.id.navi_record -> {
                    navController.navigate(R.id.recordFragment, null, options)
                    true
                }

                R.id.navi_profile -> {
                    navController.navigate(R.id.profileFragment, null, options)
                    true
                }

                else -> false
            }
        }
    }

    private fun startTimer() {
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playerTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = 0
        var mills: Float = 0f

        override fun run() {
            super.run()

            try {
                while (true) {
                    if (second >= playerTime) {
                        break
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.miniSeekbar.progress = (((mills/1000) / playerTime) * 100000).toInt()
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("song", "쓰레드 죽음 ${e.message}")
            }

        }
    }
}