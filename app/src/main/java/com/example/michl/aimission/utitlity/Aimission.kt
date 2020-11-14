package com.example.michl.aimission.utitlity

import android.app.Application
import android.content.Context
import androidx.room.Room

class Aimission : Application() {
    companion object {
        lateinit var context:Context
        var roomDb: DefaultGoalsDatabase? = null

        fun getAppContext(): Context {
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

