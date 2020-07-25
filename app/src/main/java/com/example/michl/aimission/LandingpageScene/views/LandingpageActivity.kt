package com.example.michl.aimission.LandingpageScene.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.michl.aimission.Base.MainRouter
import com.example.michl.aimission.InfoScene.implementation.REQUEST_USER_REGISTER_SUCCEED
import com.example.michl.aimission.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base.*

class LandingpageActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val router = MainRouter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        router.openMainView(supportFragmentManager)

        bottomNavBar.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.action_home -> {
                    router.openMainView(supportFragmentManager)
                }
                R.id.action_info -> {
                    router.openInfoView(supportFragmentManager)
                }
                R.id.action_settings -> {
                    router.openSettingsView(supportFragmentManager)
                }
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                router.openMainView(supportFragmentManager)
            }
            R.id.action_info -> {
                router.openInfoView(supportFragmentManager)
            }
            R.id.action_settings -> {
                router.openSettingsView(supportFragmentManager)
            }
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == REQUEST_USER_REGISTER_SUCCEED) {
            // we came from registration view and have to reload main activity
            recreate()
            bottomNavBar.selectedItemId = R.id.action_home
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
