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

    @Insert
    fun likeAlbum(like: Like)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikedAlbum(userId: Int, albumId: Int): Int?

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikedAlbum(userId: Int, albumId: Int)

    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")
    fun getLikedAlbum(userId: Int): List<AlbumInfo>
}