package com.example.michl.aimission.Utility

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room

class Aimission : Application() {
    companion object {
        private var context: Context? = null
        var roomDb: DefaultGoalsDatabase? = null

        fun getAppContext(): Context? {
            return context
        }
    }

    override fun onCreate() {
        roomDb = Room.databaseBuilder( // room db singleton
                applicationContext,
                DefaultGoalsDatabase::class.java,
                "default-goals-database"
        ).build()
        context = applicationContext

        super.onCreate()
    }
}

