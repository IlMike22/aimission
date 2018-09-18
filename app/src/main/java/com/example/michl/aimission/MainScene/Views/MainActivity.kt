package com.example.michl.aimission.MainScene.Views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.michl.aimission.MainScene.MainRouter
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val router = MainRouter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId)
            {
                R.id.action_home ->
                {
                    router.openMainView()
                }
                R.id.action_info ->
                {
                    router.openInfoView()
                }
                R.id.action_settings ->
                {
                    router.openSettingsView()
                }
            }

            return@setOnNavigationItemSelectedListener true

        }
    }
}
