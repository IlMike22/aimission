package com.example.michl.aimission.goalsScene.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.michl.aimission.goalsScene.IOnBackPressed
import com.example.michl.aimission.R

class GoalsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
        setSupportActionBar(findViewById(R.id.toolbar_goals))

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.goals_title)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_goals)
        if (!(fragment is IOnBackPressed) || !((fragment as IOnBackPressed).onBackPressed()))
            super.onBackPressed()
            finish()
    }
}
