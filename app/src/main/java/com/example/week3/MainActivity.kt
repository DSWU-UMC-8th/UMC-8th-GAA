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
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    companion object {
        const val MUSIC_TITLE = "music_title"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var song: Song
    private lateinit var timer: Timer

    private var gson: Gson = Gson()

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
        //setSample()
        inputDummySongs()
        inputDummyAlbums()

        binding.miniPlayBtn.setOnClickListener {
            song.isPlaying = true
            startTimer()
        }

        binding.miniPlayer.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)

//            song.second = (timer.mills / 1000).toInt()
//            val intent = Intent(this, SongActivity::class.java).apply {
//                putExtra("title", song.title)
//                putExtra("artist", song.artist)
//                putExtra("albumArt", song.albumArt)
//                putExtra("second", song.second)
//                putExtra("playTime", song.playTime)
//                putExtra("isPlaying", song.isPlaying)
//            }
//            getResultText.launch(intent)
            //startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if(songJson == null){
//            Song("라일락", "아이유(IU)", R.drawable.album, 0, 60, false, "music_lilac")
//        } else {
//            gson.fromJson(songJson, Song::class.java)
//        }

        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0){
            songDB.songDao().getSong(1)
        } else {
            songDB.songDao().getSong(songId)
        }

        setMiniPlayer(song)
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
            false,
            "music_sample",
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

    fun setMiniPlayer(selectedSong: Song) {
        binding.songTitleTV.text = selectedSong.title
        binding.artistTV.text = selectedSong.artist
        song = selectedSong
        binding.miniSeekbar.progress = (song.second*100000)/song.playTime
    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insert(
            Song(
                "노래1",
                "가수1",
                R.drawable.album,
                0,
                60,
                false,
                "music_sample",
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "노래2",
                "가수2",
                R.drawable.album,
                0,
                60,
                false,
                "music_sample",
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "노래3",
                "가수3",
                R.drawable.album,
                0,
                60,
                false,
                "music_sample",
                false,
                3
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())
    }

    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
            AlbumInfo(R.drawable.album, "Album 1", "Artist 1")
        )

        songDB.albumDao().insert(
            AlbumInfo(R.drawable.album, "Album 2", "Artist 2")
        )

        songDB.albumDao().insert(
            AlbumInfo(R.drawable.album, "Album 3", "Artist 3")
        )
    }
}