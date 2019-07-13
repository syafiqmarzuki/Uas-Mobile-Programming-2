package com.msyafiqm.uas

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.msyafiqm.uas.fragments.AboutFragment
import com.msyafiqm.uas.fragments.ComplainFragment
import com.msyafiqm.uas.fragments.NewsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var fragment : Fragment? = null
    companion object {
        private var navStatus = -1
        private var openFirst = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        isLoggedIn()
        init()
        if(savedInstanceState == null){
            openFirst = true
            val item = nav_view.menu.getItem(0).setChecked(true)
            onNavigationItemSelected(item)
        }
    }

    private fun init(){
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val settings = getSharedPreferences("USER", Context.MODE_PRIVATE)
                val editor = settings.edit()
                editor.clear()
                editor.commit()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_gallery -> {
                if (navStatus == 0 && !openFirst){
                    drawer_layout.closeDrawer(GravityCompat.START)
                }else{
                    navStatus = 0
                    openFirst = false
                    fragment = ComplainFragment()
                }
            }
            R.id.nav_slideshow -> {
                if (navStatus == 1 && !openFirst){
                    drawer_layout.closeDrawer(GravityCompat.START)
                }else{
                    navStatus = 1
                    openFirst = false
                    fragment = AboutFragment()
                }
            }
//            R.id.nav_slideshow -> {
//                if(navStatus == 2 && !openFirst){
//                    drawer_layout.closeDrawer(GravityCompat.START)
//                }else{
//                    navStatus = 2
//                    openFirst = false
//                    fragment = AboutFragment()
//                }
//            }
            else -> {
                openFirst = false
                navStatus = 0
                fragment = ComplainFragment()
            }
        }

        if(fragment != null){
            val fm = supportFragmentManager
            val ft = fm.beginTransaction()
            ft.replace(R.id.fragment_container, fragment!!)
            ft.commit()
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }



    private fun isLoggedIn() {
        val settings = getSharedPreferences("USER", Context.MODE_PRIVATE)
        val token = settings.getString("API_TOKEN", "UNDEFINED")
        if (token == null || token == "UNDEFINED") {
            Toast.makeText(this@MainActivity, "isLogged", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }


}
