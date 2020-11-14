package com.example.michl.aimission.utitlity

import android.content.Context

class SettingHelper {
    companion object {
        //todo extract keys in string resource later
        fun getEditItemInPastSetting(context: Context) = DbHelper.getSharedPrefsValueAsBoolean(context, "Settings_EditPastItems")
        fun getDefaultSortSetting(context:Context) = DbHelper.getSharedPrefsValueAsString(context,"Settings_DefaultSortMode")
    }
}