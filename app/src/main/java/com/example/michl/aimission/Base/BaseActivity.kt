package com.example.michl.aimission.Base

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.michl.aimission.R
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
        var actionId = getNavigationMenuItemId()
        selectBottomNavigationBarItem(actionId)
    }

    fun selectBottomNavigationBarItem(itemId: Int) {
        val menu = bottomNavBar.menu

        for (i in 0..menu.size()) {
            var tempItem = menu.getItem(i)

            if (tempItem.itemId == itemId) {
                Log.i("michl", "Item with id $itemId found")
                tempItem.isChecked = true
                break
            }
        }
    }

}
