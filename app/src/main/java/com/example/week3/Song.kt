package com.example.week3

data class Song(
    val title: String = "",
    val artist: String = "",
    val albumArt: Int = 0,
    var second: Int = 0,
    var playTime: Int = 60,
    var isPlaying: Boolean = false
)
