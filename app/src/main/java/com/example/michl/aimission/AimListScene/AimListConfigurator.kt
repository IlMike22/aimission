package com.example.michl.aimission.AimListScene

import com.example.michl.aimission.AimListScene.Views.AimListFragment
import java.lang.ref.WeakReference


object AimListConfigurator {
    /*
      Initialize all clean code components for info scene
       */
    fun configure(fragment: AimListFragment) {

        val router = AimListRouter()
        router.fragment = WeakReference(fragment)

        val presenter = AimListPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = AimListInteractor()
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}