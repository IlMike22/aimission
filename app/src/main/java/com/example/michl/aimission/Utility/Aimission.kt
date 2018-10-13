package com.example.michl.aimission.Utility

import android.app.Application
import android.content.Context
import android.util.Log

class Aimission : Application() {
    companion object {
        private var context: Context? = null

        fun getAppContext():Context?
        {
            Log.i("mike","Context is $context")
            return Aimission.context
        }
    }

    override fun onCreate() {
        super.onCreate()
        Aimission.context = applicationContext
    }
}

