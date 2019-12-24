package com.haris.weitani.moviecataloguesub2

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.haris.weitani.moviecataloguesub2.favorite_activity.FavoriteMovieListFragment
import com.haris.weitani.moviecataloguesub2.favorite_activity.FavoriteTvShowListFragment
import com.haris.weitani.moviecataloguesub2.main_activity.MovieListFragment
import com.haris.weitani.moviecataloguesub2.main_activity.TvShowListFragment

class FavoriteSectionPagerAdapter(private val context: Context, fm : FragmentManager) : FragmentPagerAdapter( fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{

    @StringRes
    private val TAB_TITLES = intArrayOf(
        R.string.tab1_title,
        R.string.tab2_title)

    override fun getPageTitle(position: Int): CharSequence? = context.resources.getString(TAB_TITLES[position])

    override fun getItem(position: Int): Fragment {
        var fragment : Fragment? = null
        when ( position ){
            0 ->{
                fragment =
                    FavoriteMovieListFragment()
            }
            1 ->{
                fragment =
                    FavoriteTvShowListFragment()
            }
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = 2

}