package com.idonse.mymovies.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Movie::class, FavouriteMovie::class], version = 3, exportSchema = false )
abstract class MovieDatabase: RoomDatabase() {

    companion object{
        private val DB_NAME = "movies.db"
        private var database: MovieDatabase? = null
        private  val LOCK = Any()

        fun getInstance(context: Context): MovieDatabase {
            synchronized(LOCK) {
                if (database == null) {
                    database =
                        Room.databaseBuilder(context, MovieDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build()
                }
                return database as MovieDatabase
            }
        }
    }

    abstract fun movieDao(): MovieDao

}