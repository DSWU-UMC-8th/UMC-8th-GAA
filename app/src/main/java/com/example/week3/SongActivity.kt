package com.example.week3

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.week3.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    private lateinit var binding_song: ActivitySongBinding
    private lateinit var song: Song
    private lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var isRepeating: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding_song = ActivitySongBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding_song.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initSong()
        setPlayer(song)

        binding_song.playBtn.setOnClickListener{
            setPlayerStatus(true)
        }

        binding_song.pauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        binding_song.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra(MainActivity.MUSIC_TITLE, title)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding_song.repeatBtn.setOnClickListener {
            isRepeating = !isRepeating
            timer.isRepeating = isRepeating
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
    }

    private fun initSong() {
        if(intent.hasExtra("title") && intent.hasExtra("artist")) {
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("artist")!!,
                intent.getIntExtra("albumArt", R.drawable.album),
                intent.getIntExtra("second", 0),
                intent.getIntExtra("playTime", 0),
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song) {
        binding_song.title.text = intent.getStringExtra("title")
        binding_song.artist.text = intent.getStringExtra("artist")
        binding_song.albumArt.setImageResource(song.albumArt)
        binding_song.progressTimeTV.text = String.format("%02d:%02d", song.second/60, song.second%60)
        binding_song.totalTimeTV.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding_song.seekBar.progress = (song.second / song.playTime)

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus (isPlaying: Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying){
            binding_song.playBtn.visibility = View.GONE
            binding_song.pauseBtn.visibility = View.VISIBLE
        } else {
            binding_song.playBtn.visibility = View.VISIBLE
            binding_song.pauseBtn.visibility = View.GONE
        }
    }

    private fun startTimer(){
        timer = Timer(song.playTime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playerTime: Int, var isPlaying: Boolean = true): Thread() {
        private var second: Int = song.second
        private var mills: Float = 0f
        var isRepeating = false

        override fun run() {
            super.run()

            try {
                while (true){
                    if (second >= playerTime){
                        if (isRepeating) {
                            second = 0
                            mills = 0f
                            continue
                        } else {
                            break
                        }
                    }

                    if (isPlaying) {
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding_song.seekBar.progress = (((song.second+(mills/1000)) / playerTime) * 100000).toInt()
                        }
                        if (mills % 1000 == 0f){
                            runOnUiThread {
                                binding_song.progressTimeTV.text = String.format("%02d:%02d", second/60, second%60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("song", "쓰레드 죽음 ${e.message}")
            }

        }
    }
}