package com.haris.weitani.moviecataloguesub2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.data.ResultTvShow
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_layout_item_tv_show.view.*

class TvShowAdapter : RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder>() {

    private var listTvShow = ArrayList<ResultTvShow>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder =
        TvShowViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_layout_item_tv_show,parent,false))

    override fun getItemCount(): Int = listTvShow.size

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bind(listTvShow[position])
    }

    inner class TvShowViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind( data : ResultTvShow){
            with(itemView){
                Picasso.with(context)
                    .load(GlobalVal.POSTER_BASE_URL+data.poster_path)
                    .into(iv_tvshow_poster)

                tv_tvshow_title.text = data.name
                tv_tvshow_year.text = data.first_air_date
                tv_tvshow_popularity.text = data.popularity.toString()
                tv_tvshow_description.text = data.overview

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(data)
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultTvShow)
    }

    fun setTvShowData(items : ArrayList<ResultTvShow>){
        listTvShow.clear()
        listTvShow.addAll(items)
        notifyDataSetChanged()
    }

    fun setFilter(filter: ArrayList<ResultTvShow>) {
        listTvShow = ArrayList()
        listTvShow.addAll(filter)
        notifyDataSetChanged()
    }

}