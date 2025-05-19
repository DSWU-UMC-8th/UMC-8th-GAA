package com.example.week3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
    val title: String = "",
    val artist: String = "",
    val albumArt: Int = 0,
    var second: Int = 0,
    var playTime: Int = 60,
    var isPlaying: Boolean = false,
    var music: String = "",
    var isLike: Boolean = false,
    val albumIdx: Int = 0
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
