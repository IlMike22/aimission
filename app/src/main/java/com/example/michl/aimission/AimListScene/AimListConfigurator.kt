package com.example.michl.aimission.AimListScene

import com.example.michl.aimission.AimListScene.Views.AimListFragment
import com.example.michl.aimission.Models.MonthItem
import java.lang.ref.WeakReference


object AimListConfigurator {
    /*
      Initialize all clean code components for aim list scene
       */
    fun configure(fragment: AimListFragment) {
        lateinit var month: MonthItem

        val router = AimListRouter()
        router.fragment = WeakReference(fragment)

        val presenter = AimListPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = AimListInteractor(month)
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}