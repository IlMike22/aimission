package com.example.michl.aimission.LandingpageScene.Views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.Adapters.MonthListAdapter
import com.example.michl.aimission.LandingpageScene.ILandingpageFragment
import com.example.michl.aimission.LandingpageScene.LandingpageConfigurator
import com.example.michl.aimission.LandingpageScene.LandingpageInteractor
import com.example.michl.aimission.LandingpageScene.LandingpageRouter
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main.*



class LandingpageFragment : ILandingpageFragment, Fragment() {
    private lateinit var lytManager: RecyclerView.LayoutManager
    private lateinit var monthItemAdapter: RecyclerView.Adapter<*>
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var router: LandingpageRouter
    lateinit var output: LandingpageInteractor

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.let { activity ->
            LandingpageConfigurator.configure(this)
        }

        // writing a sample data to db (users name)
        firebaseAuth = FirebaseAuth.getInstance()

        val firebaseDb = FirebaseDatabase.getInstance()
        val databaseRef = firebaseDb.getReference("Aim")

        // sample read out second dataset with known id

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("aimission", "an data changed error occured")
            }

            override fun onDataChange(data: DataSnapshot) {
                Log.i("aimission", "the data has changed")

                output.getUsersMonthList(data)
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    /**
        Loads all aim items for current user from db and creates list which is shown in MainFragment.
        If user is not logged in, we show an empty list with request to login instead.
     */
    override fun afterUserIdNotFound(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterMonthItemsLoadedSuccessfully(monthItems: ArrayList<MonthItem>) {
        context?.apply {
            monthItemAdapter = MonthListAdapter(monthItems, this)
        }
        lytManager = LinearLayoutManager(activity?.applicationContext)

        monthListRV?.apply {
            setHasFixedSize(true)
            adapter = monthItemAdapter
            layoutManager = lytManager
        }
    }

    override fun afterMonthItemsLoadedFailed(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterEmptyMonthListLoaded(msg: String, firstItem: MonthItem) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        var monthList = ArrayList<MonthItem>()
        monthList.add(firstItem)
        context?.apply { monthItemAdapter = MonthListAdapter(monthList, this) }
                ?: println("Aimission - No context found. Cannot call adapter.")

        lytManager = LinearLayoutManager(activity?.applicationContext)

        monthListRV.apply {
            setHasFixedSize(true)
            adapter = monthItemAdapter
            layoutManager = lytManager
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("michl","now do something")
        if (::monthItemAdapter.isInitialized)
            monthItemAdapter.notifyDataSetChanged()
    }
}
