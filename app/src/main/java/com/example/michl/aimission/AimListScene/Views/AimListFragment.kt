package com.example.michl.aimission.AimListScene.Views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.michl.aimission.R


/**
 * A simple [Fragment] subclass.
 * Shows user's aims for one month.
 *
 */
class AimListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        //todo get intent data here to specify which month was selected

        return inflater.inflate(R.layout.fragment_aim_list, container, false)
    }

}
