package com.example.michl.aimission.GoalsScene.Views

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.michl.aimission.GoalsScene.IOnBackPressed
import com.example.michl.aimission.R

class AimListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aim_list)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frg_aimlist)
        if (!(fragment is IOnBackPressed) || !((fragment as IOnBackPressed).onBackPressed()))
            super.onBackPressed()
    }
}
