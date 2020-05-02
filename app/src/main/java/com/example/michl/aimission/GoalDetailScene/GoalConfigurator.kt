package com.example.michl.aimission.GoalDetailScene

import com.example.michl.aimission.GoalDetailScene.Views.GoalFragment
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

