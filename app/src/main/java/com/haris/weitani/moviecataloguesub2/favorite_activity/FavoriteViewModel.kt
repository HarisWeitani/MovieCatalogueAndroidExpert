package com.haris.weitani.moviecataloguesub2.favorite_activity

import android.R
import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.haris.weitani.moviecataloguesub2.data.ResultGetMovie
import com.haris.weitani.moviecataloguesub2.data.ResultTvShow
import com.haris.weitani.moviecataloguesub2.room.MovieDatabase
import com.haris.weitani.moviecataloguesub2.room.TVShowsDatabase
import org.jetbrains.anko.doAsync

class FavoriteViewModel : ViewModel() {

    private val listMovie = MutableLiveData<ArrayList<ResultGetMovie>>()
    private val listTvShows = MutableLiveData<ArrayList<ResultTvShow>>()

    companion object {
        lateinit var movieDB: MovieDatabase
        lateinit var tvshowDB: TVShowsDatabase
    }

    internal fun setMovie(context: Context) {

        if (movieDB == null) {
            movieDB = MovieDatabase.getDatabase(context)
        }
        doAsync {
            listMovie.postValue(movieDB.movieDao().getAllFavMovie() as ArrayList<ResultGetMovie>?)
        }
    }

    internal fun getMovie(): LiveData<ArrayList<ResultGetMovie>> {
        return listMovie
    }

    internal fun setTvShows(context: Context) {
        if (tvshowDB == null) {
            tvshowDB = TVShowsDatabase.getDatabase(context)
        }
        doAsync {
            listTvShows.postValue(tvshowDB.tvShowsDao().getAllFavTvShows() as ArrayList<ResultTvShow>?)
        }
    }

    internal fun getTvShows(): LiveData<ArrayList<ResultTvShow>> {
        return listTvShows
    }

}