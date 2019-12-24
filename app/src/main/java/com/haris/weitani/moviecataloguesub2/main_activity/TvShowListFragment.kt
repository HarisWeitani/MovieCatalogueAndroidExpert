package com.haris.weitani.moviecataloguesub2.main_activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
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

class TvShowListFragment : Fragment() {

    private lateinit var adapter : TvShowAdapter
    private lateinit var mainViewModel: MainViewModel
    private var tempResultTvShow = ArrayList<ResultTvShow>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tv_show_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTvShowRVList()
        initMainViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchManager: SearchManager =
            context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = menu.findItem(R.id.search_view).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                val dataFilter = ArrayList<ResultTvShow>()
                if (!tempResultTvShow.isNullOrEmpty()) {
                    for (data in tempResultTvShow!!) {
                        val name = data.name!!.toLowerCase()
                        if (name.contains(p0!!.toLowerCase())) {
                            dataFilter.add(data)
                        }
                    }
                    adapter.setFilter(dataFilter)
                }
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        refreshTvShows()
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

    private fun initMainViewModel(){
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.setTvShows( requireContext() )
        showLoading(true)

        mainViewModel.getTvShows().observe(this, Observer {
            if( it != null ){
                adapter.setTvShowData(it)
                showLoading(false)
            }
        })
    }

    private fun refreshTvShows(){
        mainViewModel.refreshTvShowsData()
        showLoading(true)

        mainViewModel.getTvShows().observe(this, Observer {
            if( it != null ){
                adapter.setTvShowData(it)
                tempResultTvShow.clear()
                tempResultTvShow.addAll(it)
                showLoading(false)
            }
        })

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

}
