package com.example.michl.aimission.goalsScene.implementation

import com.example.michl.aimission.goalsScene.views.GoalsFragment
import java.lang.ref.WeakReference


object GoalsConfigurator {
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