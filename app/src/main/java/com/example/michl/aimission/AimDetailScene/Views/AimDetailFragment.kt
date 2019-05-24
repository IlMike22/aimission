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
import com.example.michl.aimission.Helper.MODE_SELECTOR
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
    fun showAimDetailData(item: AimItem)
    fun showErrorMessageToUser(msg:String)
}

class AimDetailFragment : AimDetailFragmentInput, Fragment() {



    var output: AimDetailInteractorInput? = null
    private var userID: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_aim_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AimDetailConfigurator.configure(this)

        val bundle = activity?.intent?.extras

        val mode = bundle?.get("Mode")
        val id = bundle?.get("AimId")


        if (mode == MODE_SELECTOR.Edit) {
            if (id != null)
            {
                try {
                    output?.getDetailData(id as String)
                } catch (exc: Exception) {
                    Log.e(TAG, "Cannot parse bundle parameter AimId to String. ${exc.message}")
                    output?.createErrorMessageIfItemIdIsNull(getString(R.string.frg_aimdetail_error_msg_unknown_error_edit_mode))
                }
            }
            else
            {
                Log.e(TAG,"Cannot read item data for edit mode because item id is null.")
                output?.createErrorMessageIfItemIdIsNull(getString(R.string.frg_aimdetail_error_msg_item_id_null))
            }

        }

        // first of all we verify that user is logged in on firebase
        output?.getAndValidateFirebaseUser()

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

            val genre = getGenre(frg_aimdetail_rbGroup_genre.checkedRadioButtonId)

            try {
                val aimItem = AimItem(UUID.randomUUID().toString(), title, description, repeatCount, isHighPrio, Status.OPEN, genre, getCurrentMonth(), getCurrentYear())

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

    override fun showAimDetailData(item: AimItem) {
        frg_aimdetail_txt_title.setText(item.title)
        frg_aimdetail_txt_description.setText(item.description)
        if (item.repeatCount ?: 0 > 0) {

            frg_aimdetail_switch_repeat.isChecked = true
        }

        if (item.comesBack == true)
            frg_aimdetail_switch_comesback.isChecked = true

        if (item.highPriority == true)
            frg_aimdetail_switch_aaim.isChecked = true
        frg_aimdetail_txt_repeat.setText(item.repeatCount.toString())

        when (item.genre) {
            Genre.PRIVATE -> frg_aimdetail_rb_genrePrivate.isChecked = true
            Genre.WORK -> frg_aimdetail_rb_genreWork.isChecked = true
            Genre.FUN -> frg_aimdetail_rb_genreFun.isChecked = true
            Genre.EDUCATION -> frg_aimdetail_rb_genreEducation.isChecked = true
            Genre.HEALTH -> frg_aimdetail_rb_genreHealth.isChecked = true
            Genre.FINANCES -> frg_aimdetail_rb_genreFinance.isChecked = true
            Genre.UNDEFINED -> Toast.makeText(context, "AimItem's genre is unknown!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showErrorMessageToUser(msg: String) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentMonth(): Int {
        val current = LocalDate.now()
        return current.month.value
    }

    private fun getCurrentYear(): Int {
        val current = LocalDate.now()
        return current.year.absoluteValue
    }

    private fun getGenre(selectedRbId: Int): Genre {

        return when (selectedRbId) {
            R.id.frg_aimdetail_rb_genrePrivate -> Genre.PRIVATE
            R.id.frg_aimdetail_rb_genreWork -> Genre.WORK
            R.id.frg_aimdetail_rb_genreEducation -> Genre.EDUCATION
            R.id.frg_aimdetail_rb_genreHealth -> Genre.HEALTH
            R.id.frg_aimdetail_rb_genreFun -> Genre.FUN
            R.id.frg_aimdetail_rb_genreFinance -> Genre.FINANCES
            else -> Genre.UNDEFINED
        }
    }
}
