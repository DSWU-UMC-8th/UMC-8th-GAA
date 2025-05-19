package com.example.week3

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: AlbumInfo)

    @Update
    fun update(album: AlbumInfo)

    @Delete
    fun delete(album: AlbumInfo)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<AlbumInfo>

    @Query("SELECT * FROM AlbumTable WHERE id= :id")
    fun getAlbum(id: Int): AlbumInfo
}