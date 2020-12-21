package com.example.michl.aimission.goalScene.views


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.michl.aimission.goalScene.IGoalFragment
import com.example.michl.aimission.goalScene.implementation.GoalConfigurator
import com.example.michl.aimission.goalScene.IGoalInteractor
import com.example.michl.aimission.utitlity.DateHelper
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.models.Genre
import com.example.michl.aimission.models.Status
import com.example.michl.aimission.R
import com.example.michl.aimission.utitlity.Aimission
import com.example.michl.aimission.utitlity.DbHelper
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import com.example.michl.aimission.utitlity.showSimpleDialog
import kotlinx.android.synthetic.main.fragment_goal.*
import org.jetbrains.anko.doAsync
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.absoluteValue

class GoalFragment : IGoalFragment, Fragment() {
    var output: IGoalInteractor? = null
    private var userID: String = ""
    var goal: Goal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GoalConfigurator.configure(this)

        val bundle = activity?.intent?.extras
        val mode = bundle?.get("Mode")
        var id = bundle?.getString("AimId") ?: ""

        if (mode == DateHelper.MODE_SELECTOR.Edit) {

            button_delete.visibility = View.VISIBLE

            try {
                output?.getDetailData(id)
            } catch (exc: Exception) {
                Log.e(TAG, "Cannot parse bundle parameter goal id to string. ${exc.message}")
                output?.createErrorMessageIfItemIdIsNull(getString(R.string.frg_aimdetail_error_msg_unknown_error_edit_mode))
            }
        }
        output?.getAndValidateFirebaseUser()

        button_save.setOnClickListener {
            var repeatCount = 0
            val title = edit_text_title.text.toString()
            val description = edit_text_description.text.toString()

            try {
                val userInputRepeatTime = edit_text_goal_repeat.text.toString()
                if (userInputRepeatTime.isNotEmpty())
                    repeatCount = userInputRepeatTime.toInt()
            } catch (exc: Exception) {
                Log.e(TAG, "Could not convert String into Int (repeatCount). Repeat count remains 0 as initially given.")
            }

            val isHighPriority = switch_high_priority_goal.isChecked
            val genre = getGenre(radio_group_genre.checkedRadioButtonId)
            val comesBack = switch_comes_back.isChecked
            val isGoalRepeatable = switch_repeat.isChecked

            try {
                if (id.isEmpty()) {
                    id = DbHelper.createRandomGuid()
                }
                val goal = Goal(
                        id = id,
                        creationDate = LocalDateTime.now().toString(),
                        title = title,
                        description = description,
                        repeatCount = repeatCount,
                        isHighPriority = isHighPriority,
                        status = Status.OPEN,
                        genre = genre,
                        month = getCurrentMonth(),
                        year = getCurrentYear(),
                        isComingBack = comesBack
                )

                if (mode == DateHelper.MODE_SELECTOR.Create)
                    output?.createGoal(userID, goal, isGoalRepeatable)
                else if (mode == DateHelper.MODE_SELECTOR.Edit)
                    output?.updateGoal(userID, goal, isGoalRepeatable)

            } catch (exc: Exception) {
                Log.e(TAG, "Unable to store new aim item. Reason: ${exc.message}")
                Toast.makeText(context, getString(R.string.toast_goal_save_error_text), Toast.LENGTH_SHORT).show()
            }
        }

        button_delete.setOnClickListener {
            context?.let { context ->
                showSimpleDialog(
                        context = context,
                        title = getString(R.string.dialog_delete_item_title),
                        msg = getString(R.string.dialog_delete_goal_text),
                        onButtonClicked = this::deleteGoal)
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


    override fun afterRemoveItemSuccess(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun afterRemoveItemError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun afterSaveItemSuccess() {
        Toast.makeText(context, "Goal successfully saved!", Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    override fun afterSaveItemError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterUpdateItemSuccess(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        activity?.finish()
    }

    override fun afterUpdateItemError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun showGoal(goal: Goal) {
        this.goal = goal

        edit_text_title.setText(goal.title)
        edit_text_description.setText(goal.description)

        if (goal.repeatCount > 0) switch_repeat.isChecked = true
        if (goal.isComingBack) switch_comes_back.isChecked = true
        if (goal.isHighPriority) switch_high_priority_goal.isChecked = true

        edit_text_goal_repeat.setText(goal.repeatCount.toString())

        when (goal.genre) {
            Genre.PRIVATE -> radio_button_genre_private.isChecked = true
            Genre.WORK -> radio_button_genre_work.isChecked = true
            Genre.FUN -> radio_button_genre_fun.isChecked = true
            Genre.EDUCATION -> radio_button_genre_education.isChecked = true
            Genre.HEALTH -> radio_button_genre_health.isChecked = true
            Genre.FINANCES -> radio_button_genre_finance.isChecked = true
            Genre.UNDEFINED -> Toast.makeText(context, "AimItem's genre is unknown!", Toast.LENGTH_SHORT).show()
        }

        switch_comes_back.setOnClickListener {
            val comesBackIsDisabled = switch_comes_back.isChecked == false
            if (goal.isComingBack && comesBackIsDisabled)
                showDisableComesBackWarning()
        }
    }

    override fun showErrorMessageToUser(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterValidationError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun deleteGoal(deleteGoal: Boolean) {
        val cancelDelete = !deleteGoal

        if (cancelDelete) {
            return
        }

        goal?.apply {
            output?.removeGoal(userID, this.id)
        }
    }

    private fun showDisableComesBackWarning() {
        context?.let { context ->
            showSimpleDialog(
                    context = context,
                    title = getString(R.string.dialog_goal_not_coming_back_title),
                    msg = getString(R.string.dialog_goal_not_coming_back_text),
                    onButtonClicked = this::disableComingBack)
        }
    }

    private fun disableComingBack(isConfirmed: Boolean) {
        val isDenied = !isConfirmed

        if (isDenied) {
            switch_comes_back.isChecked = true
            return
        }

        doAsync {
            try {
                goal?.apply {
                    try {
                        Aimission.roomDb?.DefaultGoalsDao()?.removeDefaultGoalFromRoomDb(this)
                        Log.i(TAG, "Default goal ${this.id} was successfully removed.")
                    } catch (error: Throwable) {
                        Toast.makeText(context, "Something went wrong, unable to remove default goal.", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Unable to remove default goal. Details: ${error.message}")
                    }
                } ?: throw java.lang.Exception("goal was null")
            } catch (error: Throwable) {
                Log.e(TAG, "Something went wrong while trying to remove default goal. ${error.message}")
            }
        }
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
            R.id.radio_button_genre_private -> Genre.PRIVATE
            R.id.radio_button_genre_work -> Genre.WORK
            R.id.radio_button_genre_education -> Genre.EDUCATION
            R.id.radio_button_genre_health -> Genre.HEALTH
            R.id.radio_button_genre_fun -> Genre.FUN
            R.id.radio_button_genre_finance -> Genre.FINANCES
            else -> Genre.UNDEFINED
        }
    }
}
