package com.example.michl.aimission.GoalsScene.implementation

import com.example.michl.aimission.GoalsScene.views.GoalsFragment
import com.example.michl.aimission.GoalsScene.implementation.GoalsInteractor
import com.example.michl.aimission.GoalsScene.implementation.GoalsPresenter
import com.example.michl.aimission.GoalsScene.implementation.GoalsRouter
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