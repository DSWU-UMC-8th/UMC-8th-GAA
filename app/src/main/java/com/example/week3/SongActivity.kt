package com.example.week3

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.week3.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    private lateinit var binding_song: ActivitySongBinding
    //private lateinit var song: Song
    private lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var isRepeating: Boolean = false
    private var gson: Gson = Gson()

    var nowPos = 0

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase

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

        initPlayList()
        initSong()
        initClickListener()
        //setPlayer(song)

    }

    override fun onPause() {
        super.onPause()
        songs[nowPos].second = ((binding_song.seekBar.progress * songs[nowPos].playTime)/100)/1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId", songs[nowPos].id)
//        val songJson = gson.toJson(songs[nowPos])
//
//        editor.putString("songData", songJson)

        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)
        Log.d("now song ID", songs[nowPos].id.toString())

//        if(intent.hasExtra("title") && intent.hasExtra("artist")) {
//            song = Song(
//                intent.getStringExtra("title")!!,
//                intent.getStringExtra("artist")!!,
//                intent.getIntExtra("albumArt", R.drawable.album),
//                intent.getIntExtra("second", 0),
//                intent.getIntExtra("playTime", 0),
//                intent.getBooleanExtra("isPlaying", false)
//            )
//        }
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
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

        binding_song.prevBtn.setOnClickListener {
            moveSong(-1)
        }

        binding_song.nextBtn.setOnClickListener {
            moveSong(+1)
        }

        binding_song.likeBtn.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for(i in 0 until songs.size){
            if (songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if (!isLike){
            binding_song.likeBtn.setImageResource(R.drawable.pencil)
        } else{
            binding_song.likeBtn.setImageResource(R.drawable.home)
        }
    }

    private fun moveSong(direct: Int){
        if (nowPos + direct < 0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }

    private fun setPlayer(song: Song) {
        binding_song.title.text = song.title
        binding_song.artist.text = song.artist
        binding_song.albumArt.setImageResource(song.albumArt)
        binding_song.progressTimeTV.text = String.format("%02d:%02d", song.second/60, song.second%60)
        binding_song.totalTimeTV.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding_song.seekBar.progress = (song.second / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if (song.isLike){
            binding_song.likeBtn.setImageResource(R.drawable.pencil)
        } else{
            binding_song.likeBtn.setImageResource(R.drawable.home)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus (isPlaying: Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying){
            binding_song.playBtn.visibility = View.GONE
            binding_song.pauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            binding_song.playBtn.visibility = View.VISIBLE
            binding_song.pauseBtn.visibility = View.GONE
            mediaPlayer?.pause()
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playerTime: Int, var isPlaying: Boolean = true): Thread() {
        private var second: Int = songs[nowPos].second
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
                            binding_song.seekBar.progress = (((songs[nowPos].second+(mills/1000)) / playerTime) * 100000).toInt()
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