package com.example.michl.aimission.MainScene

import com.example.michl.aimission.InfoScene.InfoPresenterInput
import com.example.michl.aimission.Utility.DbHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface MainInteractorInput
{
    fun loadAllUserItems()

}

class MainInteractor:MainInteractorInput
{
    var output: MainPresenterInput? = null

    override fun loadAllUserItems() {
        val userId = getCurrentUserId()
        if (userId.isNullOrEmpty())
            output?.onNoUserIdExists()
        else
        {
            // load all user items from db..

        }

    }

    private fun getCurrentUserId():String
    {
        return FirebaseAuth.getInstance().currentUser?.uid?:""
    }

}