package com.haris.weitani.favoritereceiver

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_layout_item_movie.view.*

class FavoriteMovieAdapter : RecyclerView.Adapter<FavoriteMovieAdapter.MovieViewHolder>() {

    var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.rv_layout_item_movie,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = mCursor?.count ?: 0

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(mCursor!!.moveToPosition(position))

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: Boolean) {
            with(itemView) {
                Picasso.with(context)
                    .load(
                        Helper.POSTER_BASE_URL +
                                mCursor!!.getString(
                                    mCursor!!.getColumnIndexOrThrow(Helper.COLUMN_POSTER)
                                )
                    )
                    .into(iv_movie_poster)
                tv_movie_title.text = mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(Helper.COLUMN_TITLE))
                tv_movie_year.text = mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(Helper.COLUMN_RELEASE_DATE))
                tv_movie_popularity.text = mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(Helper.COLUMN_POPULARITY))
                tv_movie_description.text = mCursor!!.getString(mCursor!!.getColumnIndexOrThrow(Helper.COLUMN_OVERVIEW))
            }
        }
    }

    fun setData(cursor: Cursor?) {
        if (cursor != null) {
            mCursor = cursor
        }
        notifyDataSetChanged()
    }

}