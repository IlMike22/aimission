package com.example.michl.aimission.AimListScene.Views


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.Adapters.AimListAdapter
import com.example.michl.aimission.AimListScene.AimListConfigurator
import com.example.michl.aimission.AimListScene.AimListInteractorInput
import com.example.michl.aimission.AimListScene.AimListRouter
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_aim_list.*

/**
 * A simple [Fragment] subclass.
 *
 */

interface AimListFragmentInput {
    fun afterUserIdNotFound(msg: String)
    fun afterUserItemsLoadedSuccessfully(items: ArrayList<AimItem>)
    fun afterUserItemsLoadedFailed(errorMsg: String)
}

class AimListFragment : AimListFragmentInput, Fragment() {

    lateinit var router: AimListRouter
    lateinit var output: AimListInteractorInput
    private lateinit var aimListAdapter: RecyclerView.Adapter<*>
    private lateinit var lytManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val firebaseDb = FirebaseDatabase.getInstance()
        var databaseRef = firebaseDb.getReference("Aim")

        // get current month and year information via intent
        val currentMonth = activity?.intent?.getIntExtra("month",0)?:Log.i("AimListFragment","Cannot get intent data information month")
        val currentYear = activity?.intent?.getIntExtra("year",0)?:Log.i("AimListFragment","Cannot get intent data information year")


        // sample read out second dataset with known id
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("aimission", "an data changed error occured")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.i("aimission", "the data has changed")
                output?.getItems(dataSnapshot, currentMonth, currentYear)
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

    override fun afterUserItemsLoadedSuccessfully(items: ArrayList<AimItem>) {
        //todo fill list adapter with content and show items in list.

        aimListAdapter = AimListAdapter(items)
        lytManager = LinearLayoutManager(activity?.applicationContext)

        aimListRV?.apply {
            setHasFixedSize(true)
            adapter = aimListAdapter
            layoutManager = lytManager
        }

    }

    override fun afterUserItemsLoadedFailed(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

}
