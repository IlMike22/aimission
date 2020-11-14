package com.example.michl.aimission.goalScene.implementation

import android.util.Log
import com.example.michl.aimission.goalScene.IGoalInteractor
import com.example.michl.aimission.goalScene.IGoalPresenter
import com.example.michl.aimission.utitlity.DateHelper
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.models.Genre
import com.example.michl.aimission.models.Status
import com.example.michl.aimission.utitlity.Aimission
import com.example.michl.aimission.utitlity.DateHelper.DateHelper.getFireBaseUser
import com.example.michl.aimission.utitlity.DbHelper
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.doAsync

class GoalInteractor : IGoalInteractor {
    var output: IGoalPresenter? = null

    override fun getAndValidateFirebaseUser() {
        output?.validateFirebaseUser(getFireBaseUser())
    }

    override fun updateGoal(
            userId: String,
            goal: Goal,
            isGoalRepeatable: Boolean) {
        if (validateUserInput(goal, isGoalRepeatable) == ValidationResult.VALIDATION_SUCCESS) {
            updateIterativeGoalsInSharedPrefs(goal)

            if (DbHelper.createOrUpdateGoal(userId, goal)) {
                output?.updateGoalSucceed()
                return
            }

            output?.onUpdateGoalFailed()
        }
    }

    override fun deleteGoal(userId: String, goalId: String) {
        if (DbHelper.deleteGoal(userId, goalId))
            output?.onDeleteGoalSucceed()
        else
            output?.onDeleteGoalFailed()
    }

    override fun createGoal(
            userId: String,
            goal: Goal,
            isGoalRepeatable: Boolean) {
        goal.month = DateHelper.getCurrentMonth()
        goal.year = DateHelper.getCurrentYear()

        updateIterativeGoalsInSharedPrefs(goal)

        val validationResult = validateUserInput(goal, isGoalRepeatable)
        if (validationResult == ValidationResult.VALIDATION_SUCCESS) {
            if (DbHelper.createOrUpdateGoal(userId, goal)) {
                output?.onStoreGoalSucceed()
                return
            }

            if (goal.isComingBack) {
                storeDefaultGoal(goal)
            }

            output?.onStoreGoalFailed()
        } else {
            output?.showValidationError(validationResult)
        }
    }

    override fun getDetailData(id: String) {
        val query = FirebaseDatabase.getInstance().reference.child("Aim").child(getFireBaseUser()).child(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(dbError: DatabaseError) {
                Log.e(TAG, "An error occured while trying to read goal. ${dbError.message}")
            }

            override fun onDataChange(data: DataSnapshot) {
                val goal = data.getValue(Goal::class.java)
                Log.i(TAG, "Item is $goal")

                goal?.apply {
                    output?.onGoalReadSucceed(this)
                } ?: Log.e(TAG, "Couldnt get detail goal with $id")
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

    private fun validateUserInput(
            goal: Goal,
            isGoalRepeatable: Boolean
    ): ValidationResult {
        if (goal.title.isEmpty() || goal.description.isEmpty()) {
            return ValidationResult.ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR
        }
        if (goal.genre == Genre.UNDEFINED) {
            return ValidationResult.NO_GENRE_DEFINED_ERROR
        }
        if (goal.status == Status.UNDEFINED) {
            return ValidationResult.NO_STATUS_DEFINED_ERROR
        }
        if (isGoalRepeatable && goal.repeatCount == 0)
            return ValidationResult.NO_AMOUNT_OF_REPEATS_ERROR

        return ValidationResult.VALIDATION_SUCCESS
    }

    private fun storeDefaultGoal(goal: Goal) {
        doAsync {
            try {
                Aimission.roomDb?.DefaultGoalsDao()?.storeDefaultGoalToRoomDb(goal)
            } catch (exception: Exception) {
                Log.e(TAG, "Something went wrong storing default goal ${goal.id} in RoomDB. Details: ${exception.message}")
            }

        }
    }
}

enum class ValidationResult {
    NO_GENRE_DEFINED_ERROR,
    ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR,
    NO_STATUS_DEFINED_ERROR,
    NO_AMOUNT_OF_REPEATS_ERROR,
    VALIDATION_SUCCESS
}


