package com.example.itunes

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM songtable WHERE trackName LIKE (:name) OR collectionName LIKE (:name) OR artistName LIKE (:name)")
    suspend fun loadAllByName(name: String): List<SongTable>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: SongTable)

    @Delete
    suspend fun delete(user: SongTable)
}