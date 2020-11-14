package com.example.michl.aimission.infoScene.implementation

import com.example.michl.aimission.infoScene.views.InfoFragment
import java.lang.ref.WeakReference

object InfoConfigurator {

    /*
    Initialize all clean code components for info scene
     */
    fun configure(fragment: InfoFragment) {

        val router = InfoRouter()
        router.fragment = WeakReference(fragment)

        val presenter = InfoPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = InfoInteractor()
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}