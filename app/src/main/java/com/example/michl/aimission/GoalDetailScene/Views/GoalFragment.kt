package com.example.michl.aimission.GoalDetailScene.Views


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.GoalDetailScene.GoalConfigurator
import com.example.michl.aimission.GoalDetailScene.IGoalInteractor
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import kotlinx.android.synthetic.main.fragment_aim_detail.*
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.absoluteValue

class GoalFragment : IGoalFragment, Fragment() {
    var output: IGoalInteractor? = null
    private var userID: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_aim_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GoalConfigurator.configure(this)

        val bundle = activity?.intent?.extras
        val mode = bundle?.get("Mode")
        var id = bundle?.getString("AimId")?:""

        if (mode == DateHelper.MODE_SELECTOR.Edit) {

            frg_aimdetail_btn_delete.visibility = View.VISIBLE

            try {
                output?.getDetailData(id)
            } catch (exc: Exception) {
                Log.e(TAG, "Cannot parse bundle parameter AimId to String. ${exc.message}")
                output?.createErrorMessageIfItemIdIsNull(getString(R.string.frg_aimdetail_error_msg_unknown_error_edit_mode))
            }
        }
        output?.getAndValidateFirebaseUser()

        frg_aimdetail_btn_save.setOnClickListener {
            var repeatCount = 0
            val title = frg_aimdetail_txt_title.text.toString()
            val description = frg_aimdetail_txt_description.text.toString()

            try {
                val userInputRepeatTime = frg_aimdetail_txt_repeat.text.toString()
                if (userInputRepeatTime.isNotEmpty())
                    repeatCount = userInputRepeatTime.toInt()
            } catch (exc: Exception) {
                Log.e(TAG, "Could not convert String into Int (repeatCount). Repeat count remains 0 as initially given.")
            }

            val isHighPrio = frg_aimdetail_switch_aaim.isChecked
            val genre = getGenre(frg_aimdetail_rbGroup_genre.checkedRadioButtonId)
            val comesBack = frg_aimdetail_switch_comesback.isChecked
            val isRepetitive = frg_aimdetail_switch_repeat.isChecked

            try {
                if (id.isEmpty()) {
                    id = DbHelper.createRandomGuid()
                }
                val goal = Goal(
                        id = id,
                        creationDate = LocalDateTime.now().toString(),
                        title = title,
                        description = description,
                        isRepetitive = isRepetitive,
                        repeatCount = repeatCount,
                        isHighPriority = isHighPrio,
                        status = Status.OPEN,
                        genre = genre,
                        month = getCurrentMonth(),
                        year = getCurrentYear(),
                        isComingBack = comesBack
                )

                if (mode == DateHelper.MODE_SELECTOR.Create)
                    output?.createGoal(userID, goal)
                else if (mode == DateHelper.MODE_SELECTOR.Edit)
                    output?.updateGoal(userID, goal)

            } catch (exc: Exception) {
                Log.e(TAG, "Unable to store new aim item. Reason: ${exc.message}")
                Toast.makeText(context, "Something went wrong while trying to save your new aim item. Please try again", Toast.LENGTH_SHORT).show()
            }
        }

        frg_aimdetail_btn_delete.setOnClickListener {
            if (showSimpleDialog("Delete item", "Do you really want to delete this item?") { isDelete ->
                        if (isDelete) {
                            //no clicked
                            id?.let { itemId ->
                                output?.deleteGoal(userID, itemId)
                            }
                                    ?: Log.e(TAG, "Cannot call delete item function because itemId is null.")
                        }
                    }) {

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


    override fun afterDeleteItemSucceed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun afterDeleteItemFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun afterSaveItemSucceed() {
        Toast.makeText(context, "Aim stored successfully!", Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun afterSaveItemFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterUpdateItemSucceed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        activity?.finish()
    }

    override fun afterUpdateItemFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun showAimDetailData(item: Goal) {
        frg_aimdetail_txt_title.setText(item.title)
        frg_aimdetail_txt_description.setText(item.description)
        if (item.repeatCount > 0) {

            frg_aimdetail_switch_repeat.isChecked = true
        }

        if (item.isComingBack == true)
            frg_aimdetail_switch_comesback.isChecked = true

        if (item.isHighPriority == true)
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
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterValidationFailed(message: String) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
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

    private fun showSimpleDialog(title: String, msg: String, onButtonClicked: (Boolean) -> Unit): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("Yes")
        { _, _ ->

            onButtonClicked(true)
        }
        builder.setNegativeButton("no")
        { _, _ ->

            onButtonClicked(false)
        }
        builder.show()

        return false
    }
}
