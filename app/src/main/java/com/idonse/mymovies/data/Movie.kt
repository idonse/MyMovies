package com.idonse.mymovies.data

import android.icu.text.CaseMap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
open class Movie(@PrimaryKey(autoGenerate = true) val uniqueId:Int?, val id: Int, val voteCount: Long, val title: String, val originalTitle:String, val overview : String, val posterPath : String, val bigPosterPath : String, val backdropPath : String, val voteAverage : Double, val releaseDate : String) {
}