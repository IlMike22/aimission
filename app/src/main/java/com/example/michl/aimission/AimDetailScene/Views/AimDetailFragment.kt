package com.example.michl.aimission.AimDetailScene.Views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.michl.aimission.R


interface AimDetailInput
{
    fun onAimSavedSuccessfully()
    fun onAimSavedFailed(errorMsg:String)
    fun onAimDeletedSuccessfully()
    fun onAimDeletedFailed(errorMsg:String)
}

class AimDetailFragment : AimDetailInput, Fragment() {
    override fun onAimSavedSuccessfully() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAimSavedFailed(errorMsg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAimDeletedSuccessfully() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAimDeletedFailed(errorMsg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        //todo get intent data here to specify which month was selected

        return inflater.inflate(R.layout.fragment_aim_detail, container, false)
    }

}
