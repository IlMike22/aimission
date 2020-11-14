package com.example.michl.aimission.goalScene.implementation

import com.example.michl.aimission.goalScene.views.GoalFragment
import java.lang.ref.WeakReference

object GoalConfigurator {
    fun configure(fragment: GoalFragment) {
        val router = GoalRouter()
        router.fragment = WeakReference(fragment)

        val presenter = GoalPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = GoalInteractor()
        interactor.output = presenter

        fragment.output = interactor
    }
}

