package com.example.itunes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SongTable::class],
    version = 1
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object{
        @Volatile private var instance : AppDataBase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "itunesDatabase"
        ).build()

    }
}