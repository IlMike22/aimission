package com.example.michl.aimission.AimDetailScene

import com.example.michl.aimission.AimDetailScene.Views.AimDetailFragment
import java.lang.ref.WeakReference

object GoalConfigurator {
    fun configure(fragment: AimDetailFragment) {
        val router = GoalRouter()
        router.fragment = WeakReference(fragment)

        val presenter = GoalPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = GoalInteractor()
        interactor.output = presenter

        fragment.output = interactor
    }
}

