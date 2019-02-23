package com.example.michl.aimission.AimDetailScene

import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Utility.DbHelper
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*


interface AimDetailInteractorInput {
    fun createNewAim(userId: String, item: AimItem)
    fun deleteSingleAim(userId: String)
    fun updateAim(userId: String, item: AimItem)
    fun getFirebaseUser()

}

class AimDetailInteractor : AimDetailInteractorInput {

    var output: AimDetailPresenterInput? = null

    override fun getFirebaseUser() {
        output?.validateFirebaseUser(FirebaseAuth.getInstance().currentUser?.uid ?: "")
    }

    override fun updateAim(userId: String, item: AimItem) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteSingleAim(userId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNewAim(userId: String, item: AimItem) {

        // add current month and year.

        item.month = DateHelper.getCurrentMonth()
        item.year = DateHelper.getCurrentYear()

        if (DbHelper.saveNewAim(userId, item))
            output?.onAimStoredSuccessfully()
        else
            output?.onAimStoredFailed()
    }

}

