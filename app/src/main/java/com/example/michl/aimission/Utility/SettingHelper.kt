package com.example.michl.aimission.Utility

import android.content.Context

class SettingHelper() {
    companion object {
        fun getEditItemInPastSetting(context: Context) = DbHelper.getSharedPrefsValueAsBoolean(context, "Settings_EditPastItems")
    }
}