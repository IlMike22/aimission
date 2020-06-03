package com.example.michl.aimission.GoalDetailScene

import android.util.Log
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GoalInteractor : IGoalInteractor {

    var output: GoalPresenterInput? = null

    override fun getAndValidateFirebaseUser() {
        output?.validateFirebaseUser(getFireBaseUser())
    }

    override fun updateGoal(userId: String, goal: Goal) {
        updateIterativeGoalsInSharedPrefs(goal)

        if (DbHelper.createOrUpdateGoal(userId, goal))
            output?.updateGoalSucceed()
        else
            output?.onUpdateGoalFailed()
    }

    override fun deleteGoal(userId: String, goalId: String) {
        if (DbHelper.deleteGoal(userId, goalId))
            output?.onDeleteGoalSucceed()
        else
            output?.onDeleteGoalFailed()
    }

    override fun createGoal(userId: String, goal: Goal) {
        // add current month and year.
        goal.month = DateHelper.getCurrentMonth()
        goal.year = DateHelper.getCurrentYear()

        updateIterativeGoalsInSharedPrefs(goal)

        goal.apply {
            val validationResult = validateUserInput(this)
            if (validationResult == ValidationResult.VALIDATION_SUCCESS) {
                if (DbHelper.createOrUpdateGoal(userId, goal)) {
                    output?.onSaveItemSucceed()
                    return
                }
                output?.onSaveItemFailed()
            } else { // validation error occured
                output?.showValidationError(validationResult)
            }

        }
    }

    override fun getDetailData(id: String) {
        val query = FirebaseDatabase.getInstance().reference.child("Aim").child(getFireBaseUser()).child(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "An error occured while trying to read item detail data. ${p0.message}")
            }

            override fun onDataChange(data: DataSnapshot) {
                val goal = data.getValue(Goal::class.java)
                Log.i(TAG, "Item is $goal")

                //now open presenter with aimitem data
                goal?.apply {
                    output?.onAimReadSuccessfully(this)
                } ?: Log.e(TAG, "Couldnt get detail item with $id")
            }
        })

    }

    override fun createErrorMessageIfItemIdIsNull(msg: String) {
        output?.onErrorMessageCreated(msg)
    }

    private fun updateIterativeGoalsInSharedPrefs(goal: Goal) {
        if (!goal.isComingBack && DbHelper.isIterativeGoalStoredInSharedPrefs(goal.id)) {
            DbHelper.removeIterativeGoalInSharedPrefs(goal.id)

        } else {
            DbHelper.storeIterativeGoalInSharedPrefs(goal.id)
        }
    }

    private fun getFireBaseUser(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun validateUserInput(aim: Goal): ValidationResult {
        if (aim.title.isEmpty() || aim.description.isEmpty()) {
            return ValidationResult.ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR
        }
        if (aim.genre == Genre.UNDEFINED) {
            return ValidationResult.NO_GENRE_DEFINED_ERROR
        }
        if (aim.status == Genre.UNDEFINED) {
            return ValidationResult.NO_STATUS_DEFINED_ERROR
        }
        if (aim.isComingBack && aim.repeatCount == 0)
            return ValidationResult.NO_AMOUNT_OF_REPEATS_ERROR

        return ValidationResult.VALIDATION_SUCCESS
    }
}

enum class ValidationResult {
    NO_GENRE_DEFINED_ERROR,
    ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR,
    NO_STATUS_DEFINED_ERROR,
    NO_AMOUNT_OF_REPEATS_ERROR,
    VALIDATION_SUCCESS
}


