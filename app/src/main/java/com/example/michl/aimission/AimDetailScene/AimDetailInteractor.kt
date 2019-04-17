package com.example.michl.aimission.AimDetailScene

import android.util.Log
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*


interface AimDetailInteractorInput {
    fun createNewAim(userId: String, item: AimItem)
    fun deleteSingleAim(userId: String)
    fun updateAim(userId: String, item: AimItem)
    fun getFirebaseUser()
    fun getDetailData(id:String)

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

    override fun getDetailData(id: String) {
        //todo get firebase data
        var query = FirebaseDatabase.getInstance().reference.child("Aim")
        query.addValueEventListener(object: ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var item = dataSnapshot.children
                Log.i(TAG,"what is $item")

                //now open presenter with aimitem data
                output?.onAimReadSuccessfully(item as AimItem)  //this WILL crash :D
            }
        })

    }
}

