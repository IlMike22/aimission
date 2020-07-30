package com.example.michl.aimission.RegisterScene.implementation

import com.example.michl.aimission.RegisterScene.views.RegisterFragment
import java.lang.ref.WeakReference

object RegisterConfigurator {
    fun configure(fragment: RegisterFragment) {
        val router = RegisterRouter(fragment.context)
        router.fragment = WeakReference(fragment)

        val presenter = RegisterPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = RegisterInteractor()
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}