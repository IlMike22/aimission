package com.example.michl.aimission.AimListScene

import com.example.michl.aimission.AimListScene.Views.AimListFragment
import com.example.michl.aimission.Models.MonthItem
import java.lang.ref.WeakReference
import java.time.Month


object AimListConfigurator {
    /*
      Initialize all clean code components for aim list scene
       */
    fun configure(fragment: AimListFragment,
                  currentMonth:MonthItem) {

        val router = AimListRouter()
        router.fragment = WeakReference(fragment)

        val presenter = AimListPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = AimListInteractor(currentMonth)
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}