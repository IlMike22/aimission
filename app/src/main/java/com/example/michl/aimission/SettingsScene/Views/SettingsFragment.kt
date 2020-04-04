package com.example.michl.aimission.SettingsScene.Views

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.Models.DefaultSortMode
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.SettingHelper
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*


class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(
                R.layout.fragment_settings,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.apply {
            val settings = readSharedPreferences(context = this)
            frg_settings_switch_enable_past_editing.isChecked = settings.get("edit_past_items").toString().toBoolean() //todo optimize convert

            when (settings.get("default_sort_mode").toString().toDefaultSortMode()) {
                DefaultSortMode.SORT_MODE_CREATION_DATE -> settings_radio_button_sort_creation_date.isChecked = true
                DefaultSortMode.SORT_MODE_ITEMS_DONE -> settings_radio_button_sort_items_done.isChecked = true
                DefaultSortMode.SORT_MODE_PRIORITY -> settings_radio_button_sort_priority.isChecked = true
            }


            frg_settings_switch_enable_past_editing.setOnClickListener {
                context?.apply {
                    DbHelper.storeInSharedPrefs(
                            context = this,
                            key = "Settings_EditPastItems",
                            value = frg_settings_switch_enable_past_editing.isChecked
                    )
                }
            }

            settings_radio_button_group_sort.setOnCheckedChangeListener { buttonView, isChecked ->
                when (buttonView.checkedRadioButtonId) {
                    R.id.settings_radio_button_sort_creation_date -> {
                        DbHelper.storeInSharedPrefs(
                                context = this,
                                key = "Settings_DefaultSortMode",
                                value = DefaultSortMode.SORT_MODE_CREATION_DATE.toText()
                        )
                    }
                    R.id.settings_radio_button_sort_items_done -> {
                        DbHelper.storeInSharedPrefs(
                                context = this,
                                key = "Settings_DefaultSortMode",
                                value = DefaultSortMode.SORT_MODE_ITEMS_DONE.toText()
                        )
                    }
                    R.id.settings_radio_button_sort_priority -> {
                        DbHelper.storeInSharedPrefs(
                                context = this,
                                key = "Settings_DefaultSortMode",
                                value = DefaultSortMode.SORT_MODE_PRIORITY.toText()
                        )
                    }
                }
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}

private fun readSharedPreferences(context: Context): Map<String,Any> {
    val settings = mutableMapOf<String,Any>()

    settings.set("edit_past_items", SettingHelper.getEditItemInPastSetting(context))
    settings.set("default_sort_mode", SettingHelper.getDefaultSortSetting(context))
    return settings
}

//todo put this helper method in a separate package
fun DefaultSortMode.toText(): String {
    return when (this) {
        DefaultSortMode.SORT_MODE_CREATION_DATE -> "SORT_MODE_CREATION_DATE"
        DefaultSortMode.SORT_MODE_ITEMS_DONE -> "SORT_MODE_ITEMS_DONE"
        DefaultSortMode.SORT_MODE_PRIORITY -> "SORT_MODE_PRIORITY"
        else -> "SORT_MODE_CREATION_DATE"
    }
}

//todo put this helper method in a separate package
fun String.toDefaultSortMode(): DefaultSortMode {
    return when (this) {
        "SORT_MODE_CREATION_DATE" -> DefaultSortMode.SORT_MODE_CREATION_DATE
        "SORT_MODE_ITEMS_DONE" -> DefaultSortMode.SORT_MODE_ITEMS_DONE
        "SORT_MODE_PRIORITY" -> DefaultSortMode.SORT_MODE_PRIORITY
        else -> DefaultSortMode.SORT_MODE_UNKNOWN
    }
}
