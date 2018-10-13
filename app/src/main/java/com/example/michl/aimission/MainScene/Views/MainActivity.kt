package com.example.michl.aimission.MainScene.Views

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.michl.aimission.Base.MainRouter
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.activity_base.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    val router = MainRouter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        router.openMainView(supportFragmentManager)

        bottomNavBar.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.action_home -> {
                    router.openMainView(supportFragmentManager)
                    true

                }
                R.id.action_info -> {
                    router.openInfoView(supportFragmentManager)
                    true

                }
                R.id.action_settings -> {
                    router.openSettingsView(supportFragmentManager)
                    true

                }
            }

            return@setOnNavigationItemSelectedListener true
        }

        fun getNavigationMenuItemId(): Int {
            return R.id.action_home
        }

        fun getContentViewId(): Int {
            return R.layout.activity_main
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
