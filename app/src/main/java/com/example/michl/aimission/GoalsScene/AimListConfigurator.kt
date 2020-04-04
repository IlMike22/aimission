package com.example.michl.aimission.GoalsScene

import com.example.michl.aimission.GoalsScene.Views.GoalsFragment
import java.lang.ref.WeakReference


object AimListConfigurator {
    /*
      Initialize all clean code components for aim list scene
       */
    fun configure(fragment: GoalsFragment) {

        val router = GoalsRouter()
        router.fragment = WeakReference(fragment)

        val presenter = GoalsPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = GoalsInteractor()
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}