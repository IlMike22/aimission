package com.example.michl.aimission.base.implementation

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.michl.aimission.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    val router = MainRouter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        bottomNavBar.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.action_home -> {
                    updateNavBarState()
                }
                R.id.action_info -> {
                    updateNavBarState()

                }
                R.id.action_settings -> {
                    updateNavBarState()
                }
            }

            return@setOnNavigationItemSelectedListener true
        }
    }

    abstract fun getNavigationMenuItemId(): Int
    abstract fun getContentViewId(): Int


    private fun updateNavBarState() {
        val id = getNavigationMenuItemId()
        selectBottomNavigationBarItem(id)
    }

    private fun selectBottomNavigationBarItem(itemId: Int) {
        val menu = bottomNavBar.menu

        for (i in 0..menu.size()) {
            val item = menu.getItem(i)

            if (item.itemId == itemId) {
                item.isChecked = true
                break
            }
        }
    }

}
