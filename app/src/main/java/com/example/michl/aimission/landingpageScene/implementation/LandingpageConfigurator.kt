package com.example.michl.aimission.landingpageScene.implementation

import com.example.michl.aimission.landingpageScene.views.LandingPageFragment
import java.lang.ref.WeakReference

object LandingpageConfigurator {
    /*
       Initialize all clean code components for info scene
        */
    fun configure(fragment: LandingPageFragment) {

        val router = LandingpageRouter()
        router.fragment = WeakReference(fragment)

        val presenter = LandingpagePresenter(fragment.context)
        presenter.output = WeakReference(fragment)

        val interactor = LandingpageInteractor()
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}