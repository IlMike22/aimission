package com.example.michl.aimission.Utility

import android.util.Log
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import java.time.LocalDate

class DateHelper {

    companion object DateHelper {
        fun getFireBaseUser(): String {
            return FirebaseAuth.getInstance().currentUser?.uid ?: ""
        }

        var currentDate = LocalDate.now()

        fun getCurrentDay():Int {
            return currentDate.dayOfMonth
        }

        fun getCurrentMonth(): Int {
            return currentDate.month.value
        }

        fun getCurrentYear(): Int {
            return currentDate.year
        }

        fun Month.isCurrentMonth():Boolean = this.month == getCurrentMonth()

        fun Month.isCurrentMonthAlreadyFinished():Boolean{
            val isNotCurrentMonth = !this.isCurrentMonth()
            if (isNotCurrentMonth) {
                return false
            }

            return getCurrentDay() > 25
        }

        fun convertDataInGoals(data: DataSnapshot, queryMonth: Int? = null, queryYear: Int? = null): ArrayList<Goal> {
            var goals = ArrayList<Goal>()

            try {
                for (singleDataSet in data.children) {
                    singleDataSet.getValue(Goal::class.java)?.apply {
                        queryYear?.let { year ->
                            queryMonth?.let { month ->
                                if (this.year == year && this.month == month)
                                    goals.add(this)

                            }
                        } ?: goals.add(this)
                    }
                }

            } catch (exc: Exception) {
                Log.e(TAG, "Error while trying to convert db data into goal. ${exc.message}")
            }

            return goals
        }

    }

    enum class MODE_SELECTOR {
        Edit,
        Create
    }
}







