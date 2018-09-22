package com.example.michl.aimission.Base

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val router = BaseRouter()

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.i("michl","onNavItemSelected called from baseactivity")
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

        return true

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
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
