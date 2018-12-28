package com.example.michl.aimission.AimDetailScene

import com.example.michl.aimission.AimDetailScene.Views.AimDetailFragment
import java.lang.ref.WeakReference

object AimDetailConfigurator {
    fun configure(fragment: AimDetailFragment) {
        val router = AimDetailRouter()
        router.fragment = WeakReference(fragment)

        val presenter = AimDetailPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = AimDetailInteractor()
        interactor.output = presenter

        fragment.output = interactor
    }
}

