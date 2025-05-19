package com.example.week3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class AlbumInfo(
    val albumArt: Int,
    val albumTitle: String,
    val albumArtist: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}