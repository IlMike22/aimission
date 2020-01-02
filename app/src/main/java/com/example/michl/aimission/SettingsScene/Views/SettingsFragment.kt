package com.example.michl.aimission.SettingsScene.Views

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.SettingHelper
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        context?.apply {
            val isEditPastItems = readSharedPreferences(this)
            frg_settings_switch_enable_past_editing.isChecked = isEditPastItems

            frg_settings_switch_enable_past_editing.setOnClickListener {
                context?.apply {
                    DbHelper.storeInSharedPrefs(context = this,
                            key = "Settings_EditPastItems",
                            value = frg_settings_switch_enable_past_editing.isChecked)
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}

private fun readSharedPreferences(context: Context):Boolean {
    val isEditPastItems = SettingHelper.getEditItemInPastSetting(context)
    return isEditPastItems // todo return an array of user settings later
}
