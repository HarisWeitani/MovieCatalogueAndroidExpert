package com.haris.weitani.moviecataloguesub2.detail_view

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.data.ResultGetMovie
import com.haris.weitani.moviecataloguesub2.room.MovieDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_detail.*
import org.jetbrains.anko.doAsync

class MovieDetailView : AppCompatActivity() {

    private lateinit var movieData: ResultGetMovie
    lateinit var movieDB: MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        movieData = intent.getParcelableExtra(GlobalVal.SELECTED_MOVIE)

        movieDB = MovieDatabase.getDatabase(this)

        initView()
    }

    private fun initView() {
//        iv_poster_image.setImageResource(movieData.picture!!)
        Picasso.with(this)
            .load(GlobalVal.POSTER_BASE_URL + movieData.poster_path)
            .into(iv_poster_image)
        tv_movie_title.text = movieData.title
        tv_movie_popularity.text = "Popularity : ${movieData.popularity}"
        tv_movie_year.text = movieData.release_date
        tv_movie_description.text = movieData.overview

        tv_movie_description.movementMethod = ScrollingMovementMethod()

        if (movieData.is_favorite == null) {
            movieData.is_favorite = false
        }

        if (movieData.is_favorite!!) {
            iv_fav_button.setImageResource(R.drawable.ic_favorite_red_24dp)
        } else {
            iv_fav_button.setImageResource(R.drawable.ic_favorite_border_red_24dp)
        }

        iv_fav_button.setOnClickListener {
            validateFavorite()
        }
    }

    private fun validateFavorite() {
        val brIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        sendBroadcast(brIntent)
        if (movieData.is_favorite!!) {
            iv_fav_button.setImageResource(R.drawable.ic_favorite_border_red_24dp)
            movieData.is_favorite = false
            doAsync {
                movieDB.movieDao().updateMovie(movieData)
            }
        } else {
            iv_fav_button.setImageResource(R.drawable.ic_favorite_red_24dp)
            movieData.is_favorite = true
            doAsync {
                movieDB.movieDao().updateMovie(movieData)
            }
        }

    }

}
