package com.example.michl.aimission.Utility

import android.app.Application
import android.content.Context

class Aimission : Application() {
    companion object {
        private var context: Context? = null

        fun getAppContext():Context?
        {
            return Aimission.context
        }
    }

    override fun onCreate() {
        super.onCreate()
        Aimission.context = applicationContext
    }
}

