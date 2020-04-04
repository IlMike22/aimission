package com.example.michl.aimission.GoalDetailScene

import com.example.michl.aimission.GoalDetailScene.Views.GoalDetailFragment
import java.lang.ref.WeakReference

object GoalConfigurator {
    fun configure(fragment: GoalDetailFragment) {
        val router = GoalRouter()
        router.fragment = WeakReference(fragment)

        val presenter = GoalPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = GoalInteractor()
        interactor.output = presenter

        fragment.output = interactor
    }
}

