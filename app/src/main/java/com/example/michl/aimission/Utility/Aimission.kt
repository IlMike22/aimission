package com.example.michl.aimission.Utility

import android.app.Application
import android.content.Context
import android.util.Log

class Aimission : Application() {
    companion object {
        private var context: Context? = null

        fun getAppContext():Context?
        {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}

