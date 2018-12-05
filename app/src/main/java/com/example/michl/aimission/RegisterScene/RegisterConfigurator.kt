package com.example.michl.aimission.RegisterScene

import com.example.michl.aimission.RegisterScene.Views.RegisterFragment
import java.lang.ref.WeakReference

object RegisterConfigurator {

    fun configure(fragment: RegisterFragment) {

        val router = RegisterRouter()
        router.fragment = WeakReference(fragment)

        val presenter = RegisterPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = RegisterInteractor()
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}