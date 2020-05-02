package com.example.michl.aimission.Utility

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Models.Goal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class DbHelper {
    companion object {
        const val TAG = "Aimission"
        const val ITERATIVE_ITEMS_KEY = "ITERATIVE_ITEMS"

        fun createOrUpdateGoal(userId: String, goal: Goal): Boolean {
            return try {
                getGoalTableReference().child(userId).child(goal.id ?: "").setValue(goal)
                true
            } catch (exception: Exception) {
                Log.e("Aimission", "Failed writing new item in table Aim. Details: ${exception.message}")
                false
            }
        }

        fun deleteGoal(userId: String, itemId: String): Boolean {
            return try {
                getGoalTableReference().child(userId).child(itemId).setValue(null)
                Log.i("Aimission", "Item was successfully deleted.")
                true
            } catch (exception: java.lang.Exception) {
                Log.e("Aimission", "An error occured while deleting item. Details: ${exception.message}")
                false
            }
        }

        fun createNewPersonReference(userId: String): Boolean {
            return try {
                getGoalTableReference().child(userId).setValue(null)
                true
            } catch (exc: java.lang.Exception) {
                Log.e(TAG, "An error occured while trying to add new user id dataset on aim table. Details: ${exc.message}")
                false
            }
        }

        fun getGoalTableReference(): DatabaseReference {
            val firebaseDb = FirebaseDatabase.getInstance()
            return firebaseDb.getReference("Aim")
        }

        fun getCurrentUserId(): String {
            return FirebaseAuth.getInstance().currentUser?.uid ?: ""
        }

        fun getSharedPrefsInstance(context: Context): SharedPreferences {

            return context.getSharedPreferences("Aimission", Context.MODE_PRIVATE)

        }

        @SuppressLint("CommitPrefEdits")
        fun storeInSharedPrefs(context: Context, key: String, value: Any): Boolean {
            val sharedPref = getSharedPrefsInstance(context)
            when (value) {
                is Boolean -> {
                    sharedPref?.apply {
                        with(this.edit())
                        {
                            putBoolean(key, value)
                            apply()
                        }
                    }
                }

                is String -> {
                    sharedPref?.apply {
                        with(this.edit())
                        {
                            putString(key, value)
                            apply()
                        }
                    }
                }

                is Int -> {
                    sharedPref?.apply {
                        with(this.edit())
                        {
                            sharedPref.edit().putInt(key, value)
                            apply()
                        }
                    }
                }
                else -> {
                    Log.e(TAG, "Unknown data type. Cannot save it into shared prefs.")
                    return false
                }
            }

            return true
        }

        fun getSharedPrefsValueAsInt(context: Context, key: String): Int {
            val sharedPref = getSharedPrefsInstance(context)
            return sharedPref.getInt(key, -1)
        }

        fun getSharedPrefsValueAsString(context: Context, key: String): String {
            val sharedPref = getSharedPrefsInstance(context)
            return sharedPref.getString(key, "")
        }

        fun getSharedPrefsValueAsBoolean(context: Context, key: String): Boolean {
            val sharedPref = getSharedPrefsInstance(context)
            return sharedPref.getBoolean(key, false)
        }

        fun storeIterativeGoalInSharedPrefs(id: String) {
            Aimission.getAppContext()?.apply {
                val sharedPreferences = getSharedPrefsInstance(this)
                val ids = readIterativeGoalsFromSharedPrefs().toMutableList()
                ids.add(id)
                sharedPreferences.edit().putStringSet(ITERATIVE_ITEMS_KEY, ids.toHashSet()).apply() {
                    apply()
                }
            }
        }

        fun removeIterativeGoalInSharedPrefs(id: String) {
            Aimission.getAppContext()?.apply {
                val sharedPreferences = getSharedPrefsInstance(this)
                val ids = readIterativeGoalsFromSharedPrefs().toMutableList()
                ids.remove(id)
                sharedPreferences.edit().putStringSet(ITERATIVE_ITEMS_KEY, ids.toHashSet()).apply() {
                    apply()
                }
            }
        }

        fun isIterativeGoalStoredInSharedPrefs(id: String): Boolean {
            Aimission.getAppContext()?.apply {
                val ids = readIterativeGoalsFromSharedPrefs().toMutableList()
                return ids.contains(id)
            }
            return false
        }

        fun readIterativeGoalsFromSharedPrefs(): List<String> {
            Aimission.getAppContext()?.apply {
                val sharedPreferences = getSharedPrefsInstance(this)
                return sharedPreferences.getStringSet(ITERATIVE_ITEMS_KEY, hashSetOf()).toList()
            }
            return emptyList()
        }

        fun getIterativeGoals(goals: ArrayList<Goal?>): ArrayList<Goal> {
            val iterativeGoals = ArrayList<Goal>()
            val ids = readIterativeGoalsFromSharedPrefs()
            goals.forEach { goal ->
                goal?.apply {
                    if (ids.contains(id)) {
                        // create new goal with new id, current month, current year, and the information of goal
                        iterativeGoals.add(
                                Goal(
                                        id = createRandomGuid(),
                                        title = title,
                                        description = description,
                                        month = DateHelper.getCurrentMonth(),
                                        year = DateHelper.getCurrentYear(),
                                        creationDate = LocalDateTime.now().toString(),
                                        repeatCount = repeatCount,
                                        isHighPriority = isHighPriority,
                                        isRepetitive = isRepetitive,
                                        status = status,
                                        genre = genre
                                )
                        )
                    }
                }
            }

            return iterativeGoals
        }

        fun createRandomGuid(): String = UUID.randomUUID().toString()
    }
}