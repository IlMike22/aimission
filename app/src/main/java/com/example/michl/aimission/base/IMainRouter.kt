package com.example.michl.aimission.base

import androidx.fragment.app.FragmentManager

interface IMainRouter {
    fun openSettingsView(manager: FragmentManager)

    fun openInfoView(manager: FragmentManager)

    fun openMainView(manager: FragmentManager)

    fun openAimDetailView(manager: FragmentManager)
}