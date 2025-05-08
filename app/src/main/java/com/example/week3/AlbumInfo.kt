package com.example.week3


data class AlbumInfo(
    val albumArt: Int,
    val albumTitle: String,
    val albumArtist: String,
    var trackList: Array<Song> = arrayOf(Song("노래00", "가수00", R.drawable.album, 0, 60, false))
)
