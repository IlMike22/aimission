package com.example.michl.aimission.settingScene.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.michl.aimission.models.DefaultSortMode
import com.example.michl.aimission.R
import com.example.michl.aimission.utitlity.DbHelper
import com.example.michl.aimission.utitlity.SettingHelper
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
                R.layout.fragment_settings,
                container,
                false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        context?.apply {
            val settings = readSharedPreferences(context = this)
            switch_settings_enable_past_editing.isChecked = settings.get("edit_past_items").toString().toBoolean() //todo optimize convert

            when (settings.get("default_sort_mode").toString().toDefaultSortMode()) {
                DefaultSortMode.SORT_MODE_CREATION_DATE -> radio_button_settings_sorting_creation_date.isChecked = true
                DefaultSortMode.SORT_MODE_ITEMS_DONE -> radio_button_settings_sorting_items_done.isChecked = true
                DefaultSortMode.SORT_MODE_PRIORITY -> radio_button_settings_sorting_priority.isChecked = true
            }

            switch_settings_enable_past_editing.setOnClickListener {
                context?.apply {
                    DbHelper.storeInSharedPrefs(
                            context = this,
                            key = "Settings_EditPastItems",
                            value = switch_settings_enable_past_editing.isChecked
                    )
                }
            }

            radio_group_settings_sorting.setOnCheckedChangeListener { buttonView, isChecked ->
                when (buttonView.checkedRadioButtonId) {
                    R.id.radio_button_settings_sorting_creation_date -> {
                        DbHelper.storeInSharedPrefs(
                                context = this,
                                key = "Settings_DefaultSortMode",
                                value = DefaultSortMode.SORT_MODE_CREATION_DATE.toText()
                        )
                    }
                    R.id.radio_button_settings_sorting_items_done -> {
                        DbHelper.storeInSharedPrefs(
                                context = this,
                                key = "Settings_DefaultSortMode",
                                value = DefaultSortMode.SORT_MODE_ITEMS_DONE.toText()
                        )
                    }
                    R.id.radio_button_settings_sorting_priority -> {
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

    companion object {
        fun readSharedPreferences(context: Context): Map<String, Any> {
            val settings = mutableMapOf<String, Any>()
            settings.set(context.getString(R.string.settings_value_edit_past_items), SettingHelper.getEditItemInPastSetting(context))
            settings.set(context.getString(R.string.settings_value_default_sort_mode), SettingHelper.getDefaultSortSetting(context))
            return settings
        }

        fun DefaultSortMode.toText(): String {
            return when (this) {
                DefaultSortMode.SORT_MODE_CREATION_DATE -> "SORT_MODE_CREATION_DATE"
                DefaultSortMode.SORT_MODE_ITEMS_DONE -> "SORT_MODE_ITEMS_DONE"
                DefaultSortMode.SORT_MODE_PRIORITY -> "SORT_MODE_PRIORITY"
                else -> "SORT_MODE_CREATION_DATE"
            }
        }

        fun String.toDefaultSortMode(): DefaultSortMode {
            return when (this) {
                "SORT_MODE_CREATION_DATE" -> DefaultSortMode.SORT_MODE_CREATION_DATE
                "SORT_MODE_ITEMS_DONE" -> DefaultSortMode.SORT_MODE_ITEMS_DONE
                "SORT_MODE_PRIORITY" -> DefaultSortMode.SORT_MODE_PRIORITY
                else -> DefaultSortMode.SORT_MODE_UNKNOWN
            }
        }
    }
}