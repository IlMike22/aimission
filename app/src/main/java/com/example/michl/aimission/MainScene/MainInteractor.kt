package com.example.michl.aimission.MainScene

import com.example.michl.aimission.InfoScene.InfoPresenterInput
import com.example.michl.aimission.Utility.DbHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface MainInteractorInput
{
    fun readAllAims()

}

class MainInteractor:MainInteractorInput
{
    var output: MainPresenterInput? = null

    override fun readAllAims() {
        val userId = getCurrentUserId()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getCurrentUserId():String
    {
        return FirebaseAuth.getInstance().currentUser?.uid?:""
    }

}