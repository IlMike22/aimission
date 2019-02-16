package com.example.michl.aimission.AimListScene.Views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.AimListScene.AimListConfigurator
import com.example.michl.aimission.AimListScene.AimListInteractorInput
import com.example.michl.aimission.AimListScene.AimListRouter
import com.example.michl.aimission.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 *
 */

interface AimListFragmentInput {
    fun afterUserIdNotFound(msg: String)
}

class AimListFragment : AimListFragmentInput, Fragment() {

    lateinit var router: AimListRouter
    lateinit var output: AimListInteractorInput
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val firebaseDb = FirebaseDatabase.getInstance()
        var databaseRef = firebaseDb.getReference("Aim")


        // sample read out second dataset with known id

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("aimission", "an data changed error occured")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.i("aimission", "the data has changed")
                output?.getItems(dataSnapshot)
            }

        })
        return inflater.inflate(R.layout.fragment_aim_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AimListConfigurator.configure(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun afterUserIdNotFound(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}
