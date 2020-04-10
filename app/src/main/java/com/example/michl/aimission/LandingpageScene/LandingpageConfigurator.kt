package com.example.michl.aimission.LandingpageScene


import com.example.michl.aimission.LandingpageScene.Views.LandingpageFragment
import java.lang.ref.WeakReference


object LandingpageConfigurator{
    /*
       Initialize all clean code components for info scene
        */
    fun configure(fragment: LandingpageFragment) {

        val router = LandingpageRouter()
        router.fragment = WeakReference(fragment)

        val presenter = LandingpagePresenter()
        presenter.output = WeakReference(fragment)

        val interactor = LandingpageInteractor()
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}