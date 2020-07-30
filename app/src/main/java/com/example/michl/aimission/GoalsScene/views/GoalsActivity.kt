package com.example.michl.aimission.GoalsScene.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.michl.aimission.GoalsScene.IOnBackPressed
import com.example.michl.aimission.R

class GoalsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frg_goals)
        if (!(fragment is IOnBackPressed) || !((fragment as IOnBackPressed).onBackPressed()))
            super.onBackPressed()
            finish()
    }
}
