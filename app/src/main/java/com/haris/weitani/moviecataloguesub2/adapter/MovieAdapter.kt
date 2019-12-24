package com.haris.weitani.moviecataloguesub2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.data.ResultGetMovie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_layout_item_movie.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private var listMovie = ArrayList<ResultGetMovie>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_layout_item_movie,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ResultGetMovie) {
            with(itemView) {
                Picasso.with(context)
                    .load(GlobalVal.POSTER_BASE_URL + data.poster_path)
                    .into(iv_movie_poster)

                tv_movie_title.text = data.title
                tv_movie_year.text = data.release_date
                tv_movie_popularity.text = data.popularity.toString()
                tv_movie_description.text = data.overview

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultGetMovie)
    }

    fun setMovieData(items: ArrayList<ResultGetMovie>) {
        listMovie.clear()
        listMovie.addAll(items)
        notifyDataSetChanged()
    }

    fun setFilter(filter: ArrayList<ResultGetMovie>) {
        listMovie = ArrayList()
        listMovie.addAll(filter)
        notifyDataSetChanged()
    }

}