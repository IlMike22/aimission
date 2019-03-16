package com.example.michl.aimission.AimDetailScene.Views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.AimDetailScene.AimDetailConfigurator
import com.example.michl.aimission.AimDetailScene.AimDetailInteractorInput
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import kotlinx.android.synthetic.main.fragment_aim_detail.*
import java.time.LocalDate
import java.util.*
import kotlin.math.absoluteValue


interface AimDetailFragmentInput {
    fun onAimSavedSuccessfully()
    fun onAimSavedFailed(errorMsg: String)
    fun onAimDeletedSuccessfully()
    fun onAimDeletedFailed(errorMsg: String)
    fun onFirebaseUserNotExists(msg: String)
    fun onFirebaseUserExists(userId: String)
    fun afterAimStoredSuccessfully()
    fun afterAimStoredFailed()
}

class AimDetailFragment : AimDetailFragmentInput, Fragment() {

    var output: AimDetailInteractorInput? = null
    var userID: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_aim_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AimDetailConfigurator.configure(this)

        //todo get intent data here if list item was selected. if there is no data we have the new item create mode

        // first of all we verify that user is logged in on firebase
        output?.getFirebaseUser()

        frg_aimdetail_btn_save.setOnClickListener {
            var isHighPrio = false
            var repeatCount = 0
            val title = frg_aimdetail_txt_title.text.toString()
            val description = frg_aimdetail_txt_description.text.toString()

            try {
                repeatCount = frg_aimdetail_txt_repeat.text.toString().toInt()
            } catch (exc: Exception) {
                Log.e(TAG, "Could not convert String into Int (repeatCount). Repeat count remains 0 as initially given.")
            }

            if (frg_aimdetail_switch_aaim.isChecked)
                isHighPrio = true


            try {
                var aimItem = AimItem(UUID.randomUUID().toString(), title, description, repeatCount, isHighPrio, Status.OPEN, Genre.PRIVATE, getCurrentMonth(), getCurrentYear())

                output?.createNewAim(userID, aimItem)
            } catch (exc: Exception) {
                Log.e(TAG, "Unable to store new aim item. Reason: ${exc.message}")
                Toast.makeText(context, "Something went wrong while trying to save your new aim item. Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onFirebaseUserNotExists(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun onFirebaseUserExists(userId: String) {
        Toast.makeText(context, userId, Toast.LENGTH_SHORT).show()
        Log.i(TAG, "Firebase user exists. User id is $userId")
        userID = userId
    }

    override fun onAimSavedSuccessfully() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAimSavedFailed(errorMsg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAimDeletedSuccessfully() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAimDeletedFailed(errorMsg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun afterAimStoredSuccessfully() {
        Toast.makeText(context, "Aim stored successfully!", Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun afterAimStoredFailed() {
        Toast.makeText(context, "Aim stored failed! Please try again.", Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentMonth():Int
    {
        val current = LocalDate.now()
        return current.month.value
    }

    private fun getCurrentYear():Int
    {
        val current = LocalDate.now()
        return current.year.absoluteValue

    }
}
