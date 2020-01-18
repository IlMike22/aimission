package com.example.michl.aimission.MainScene


import android.content.Context
import com.example.michl.aimission.MainScene.Views.MainFragment
import java.lang.ref.WeakReference


object MainConfigurator{
    /*
       Initialize all clean code components for info scene
        */
    fun configure(fragment: MainFragment, context: Context) {

        val router = MainRouter()
        router.fragment = WeakReference(fragment)

        val presenter = MainPresenter()
        presenter.output = WeakReference(fragment)

        val interactor = MainInteractor()
        interactor.output = presenter

        fragment.output = interactor

        fragment.router = router

    }
}