package com.example.michl.aimission.AimDetailScene

import android.util.Log
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


interface AimDetailInteractorInput {
    fun createNewAim(userId: String, item: AimItem)
    fun deleteSingleAim(userId: String, itemId: String)
    fun updateAim(userId: String, item: AimItem)
    fun getAndValidateFirebaseUser()
    fun getDetailData(id: String)
    fun createErrorMessageIfItemIdIsNull(msg: String)
}

enum class ValidationResult{
    NO_GENRE_DEFINED_ERROR,
    ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR,
    NO_STATUS_DEFINED_ERROR,
    NO_AMOUNT_OF_REPEATS_ERROR,
    VALIDATION_SUCCESS
}

class AimDetailInteractor : AimDetailInteractorInput {


    var output: AimDetailPresenterInput? = null

    override fun getAndValidateFirebaseUser() {
        output?.validateFirebaseUser(getFireBaseUser())
    }

    override fun updateAim(userId: String, item: AimItem) {
        if (DbHelper.createOrUpdateAimItem(userId, item))
            output?.onUpdateItemSucceed()
        else
            output?.onUpdateItemFailed()
    }

    override fun deleteSingleAim(userId: String, itemId: String) {
        if (DbHelper.deleteAimItem(userId, itemId))
            output?.onDeleteItemSucceed()
        else
            output?.onDeleteItemFailed()
    }

    override fun createNewAim(userId: String, item: AimItem) {

        // add current month and year.

        item.month = DateHelper.getCurrentMonth()
        item.year = DateHelper.getCurrentYear()

        item?.apply {
            val validationResult = validateUserInput(this)
            if (validationResult == ValidationResult.VALIDATION_SUCCESS) {
                if (DbHelper.createOrUpdateAimItem(userId, item)) {
                    output?.onSaveItemSucceed()
                    return
                }
                output?.onSaveItemFailed()
            }
            else { // validation error occured
                output?.showValidationError(validationResult)
            }

        }
    }

    override fun getDetailData(id: String) {
        val query = FirebaseDatabase.getInstance().reference.child("Aim").child(getFireBaseUser()).child(id)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "An error occured while trying to read item detail data. ${p0.message}")
            }

            override fun onDataChange(data: DataSnapshot) {
                val item = data.getValue(AimItem::class.java)
                Log.i(TAG, "Item is $item")

                //now open presenter with aimitem data
                item?.apply {
                    output?.onAimReadSuccessfully(this)
                } ?: Log.e(TAG, "Couldnt get detail item with $id")
            }
        })

    }

    override fun createErrorMessageIfItemIdIsNull(msg: String) {
        output?.onErrorMessageCreated(msg)
    }

    private fun getFireBaseUser(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    private fun validateUserInput(aim: AimItem):ValidationResult {
        if (aim.title.isEmpty() || aim.description.isEmpty()) {
            return ValidationResult.ERROR_REQUIRED_FIELD_IS_EMPTY_ERROR
        }
        if (aim.genre == Genre.UNDEFINED) {
            return ValidationResult.NO_GENRE_DEFINED_ERROR
        }
        if (aim.status == Genre.UNDEFINED) {
            return ValidationResult.NO_STATUS_DEFINED_ERROR
        }
        if (aim.isRepetitive && aim.repeatCount == 0)
            return ValidationResult.NO_AMOUNT_OF_REPEATS_ERROR

        return ValidationResult.VALIDATION_SUCCESS
    }
}

