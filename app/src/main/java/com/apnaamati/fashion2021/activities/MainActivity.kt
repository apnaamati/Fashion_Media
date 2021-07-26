package com.apnaamati.fashion2021.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.apnaamati.fashion2021.R
import com.apnaamati.fashion2021.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment())
            .commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.mHome -> selectedFragment = HomeFragment()
            R.id.mImage -> selectedFragment = ImageFragment()
            R.id.video -> selectedFragment = VideoFragment()
            R.id.shop -> selectedFragment = ShopFragment()
            R.id.fav -> selectedFragment = FavFragment()
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, selectedFragment!!)
            .commit()
        true
    }
}