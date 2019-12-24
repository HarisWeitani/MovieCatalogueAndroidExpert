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
import com.haris.weitani.moviecataloguesub2.detail_view.MovieDetailView
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.adapter.MovieAdapter
import com.haris.weitani.moviecataloguesub2.data.ResultGetMovie
import kotlinx.android.synthetic.main.fragment_movie_list.*

class MovieListFragment : Fragment() {

    private lateinit var adapter: MovieAdapter
    private lateinit var mainViewModel: MainViewModel
    private var tempResultGetMovie = ArrayList<ResultGetMovie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
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
                mainViewModel.searchMovie(requireContext(), p0!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
//                val dataFilter = ArrayList<ResultGetMovie>()
//                if (!tempResultGetMovie.isNullOrEmpty()) {
//                    for (data in tempResultGetMovie!!) {
//                        val name = data.title!!.toLowerCase()
//                        if (name.contains(p0!!.toLowerCase())) {
//                            dataFilter.add(data)
//                        }
//                    }
//                    adapter.setFilter(dataFilter)
//                }
                if (p0?.length == 0) {
                    mainViewModel.setMovie(requireContext())
                }
                return true
            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMovieRVList()
        initMainViewModel()
    }

    override fun onResume() {
        super.onResume()
        refreshMovie()
    }

    private fun initMovieRVList() {

        adapter = MovieAdapter()
        adapter.notifyDataSetChanged()

        rv_movie_list.layoutManager = LinearLayoutManager(context)
        rv_movie_list.adapter = adapter

        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultGetMovie) {
                val intent = Intent(
                    context,
                    MovieDetailView::class.java
                )
                intent.putExtra(GlobalVal.SELECTED_MOVIE, data)
                startActivity(intent)
            }
        })
    }

    private fun initMainViewModel() {
        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
        mainViewModel.setMovie(requireContext())
        showLoading(true)

        mainViewModel.getMovie().observe(this, Observer {
            if (it != null) {
                adapter.setMovieData(it)
                tempResultGetMovie.clear()
                tempResultGetMovie.addAll(it)
                showLoading(false)
            }
        })
    }

    private fun refreshMovie() {
        mainViewModel.refreshMovieData()
        showLoading(true)

        mainViewModel.getMovie().observe(this, Observer {
            if (it != null) {
                adapter.setMovieData(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBar.visibility = View.VISIBLE
        else progressBar.visibility = View.GONE
    }

}
