package com.example.itunes

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["trackId","artistId"], unique = true)])
data class SongTable(
    @PrimaryKey(autoGenerate = true)val uid :Int,
    val trackId : Int?,
    val artistId : Int?,
    val trackName :String?,
    val artistName : String?,
    val kind : String?,
    val collectionName :String?,
    val wrapperType : String,
    val releaseDate :String
    )

//
