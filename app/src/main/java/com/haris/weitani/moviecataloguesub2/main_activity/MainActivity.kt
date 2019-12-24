package com.haris.weitani.moviecataloguesub2.main_activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.haris.weitani.moviecataloguesub2.MainSectionPagerAdapter
import com.haris.weitani.moviecataloguesub2.R
import com.haris.weitani.moviecataloguesub2.favorite_activity.FavoriteActivity
import com.haris.weitani.moviecataloguesub2.notification.NotificationReceiver
import com.haris.weitani.moviecataloguesub2.notification.NotificationSettingsActivity
import com.haris.weitani.moviecataloguesub2.room.MovieDatabase
import com.haris.weitani.moviecataloguesub2.room.TVShowsDatabase
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {

    private lateinit var adapterMain : MainSectionPagerAdapter
//    private lateinit var notificationReceiver: NotificationReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapterMain = MainSectionPagerAdapter(
            this,
            supportFragmentManager
        )
        view_pager.adapter = adapterMain
        tabs.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f

        MainViewModel.movieDB = MovieDatabase.getDatabase(this)
        MainViewModel.tvshowDB = TVShowsDatabase.getDatabase(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
            R.id.menu_favorite -> {
                startActivity(intentFor<FavoriteActivity>())
            }
            R.id.menu_reminder->{
                startActivity(intentFor<NotificationSettingsActivity>())
            }
        }
        return super.onOptionsItemSelected(item)
    }



}
