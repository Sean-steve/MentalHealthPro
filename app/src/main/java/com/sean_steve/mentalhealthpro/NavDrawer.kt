package com.sean_steve.mentalhealthpro

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class NavDrawer : AppCompatActivity(){
    //NavigationView.OnNavigationItemSelectedListener
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navdrawer)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setupNavigationDrawer()
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        //start here if an error
        navController = findNavController(R.id.nav_host_fragment_nav_drawer)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.illnessRecyclerView, R.id.Community, R.id.Tracker, R.id.Settings,R.id.tasks_fragment_dest,
                R.id.statistics_fragment_dest
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_nav_drawer).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    private fun setupNavigationDrawer() {
        drawerLayout = (findViewById<DrawerLayout>(R.id.drawer_layout))
            .apply {
                setStatusBarBackground(R.color.colorPrimaryDark)
            }
    }
}
// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3
