package com.haris.weitani.moviecataloguesub2.detail_view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.data.ResultTvShow
import com.haris.weitani.moviecataloguesub2.room.TVShowsDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tv_show_detail_view.*
import org.jetbrains.anko.doAsync

class TvShowDetailView : AppCompatActivity() {

    private lateinit var tvShowData: ResultTvShow
    lateinit var tvshowsDB: TVShowsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_detail_view)

        tvShowData = intent.getParcelableExtra(GlobalVal.SELECTED_MOVIE)

        tvshowsDB = TVShowsDatabase.getDatabase(this)

        initView()
    }

    private fun initView() {
        //        iv_poster_image.setImageResource(movieData.picture!!)
        Picasso.with(this)
            .load(GlobalVal.POSTER_BASE_URL + tvShowData.poster_path)
            .into(iv_poster_image)
        tv_tvshow_title.text = tvShowData.name
        tv_tvshow_popularity.text = "Popularity : ${tvShowData.popularity}"
        tv_tvshow_year.text = tvShowData.first_air_date
        tv_tvshow_description.text = tvShowData.overview

        tv_tvshow_description.movementMethod = ScrollingMovementMethod()

        if (tvShowData.is_favorite == null) {
            tvShowData.is_favorite = false
        }

        if (tvShowData.is_favorite!!) {
            iv_fav_button.setImageResource(R.drawable.ic_favorite_red_24dp)
        } else {
            iv_fav_button.setImageResource(R.drawable.ic_favorite_border_red_24dp)
        }

        iv_fav_button.setOnClickListener {
            validateFavorite()
        }
    }

    private fun validateFavorite() {
        if (tvShowData.is_favorite!!) {
            iv_fav_button.setImageResource(R.drawable.ic_favorite_border_red_24dp)
            tvShowData.is_favorite = false
            doAsync {
                tvshowsDB.tvShowsDao().updateTvSHows(tvShowData)
            }
        } else {
            iv_fav_button.setImageResource(R.drawable.ic_favorite_red_24dp)
            tvShowData.is_favorite = true
            doAsync {
                tvshowsDB.tvShowsDao().updateTvSHows(tvShowData)
            }
        }
    }

}
