package com.haris.weitani.moviecataloguesub2.favorite_activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haris.weitani.moviecataloguesub1.common.GlobalVal
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.detail_view.TvShowDetailView
import com.haris.weitani.moviecataloguesub2.adapter.TvShowAdapter
import com.haris.weitani.moviecataloguesub2.data.ResultTvShow
import kotlinx.android.synthetic.main.fragment_tv_show_list.*
import kotlinx.android.synthetic.main.fragment_tv_show_list.progressBar

class FavoriteTvShowListFragment : Fragment() {

    private lateinit var adapter : TvShowAdapter
    private lateinit var favViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tv_show_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        initfavViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTvShowRVList()
        initfavViewModel()
    }

    private fun initTvShowRVList(){
        rv_tvshow_list.layoutManager = LinearLayoutManager(context)
        adapter = TvShowAdapter()
        rv_tvshow_list.adapter = adapter

        adapter.setOnItemClickCallback(object : TvShowAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ResultTvShow) {
                val intent = Intent(context, TvShowDetailView::class.java)
                intent.putExtra(GlobalVal.SELECTED_MOVIE,data)
                startActivity(intent)
            }
        })
    }

    private fun initfavViewModel(){
        favViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FavoriteViewModel::class.java)
        favViewModel.setTvShows(requireContext())
        showLoading(true)

        favViewModel.getTvShows().observe(this, Observer {
            if( it != null ){
                adapter.setTvShowData(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

}
