package com.example.michl.aimission.MainScene.Views

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
import com.example.michl.aimission.Adapters.MonthListAdapter
import com.example.michl.aimission.MainScene.MainConfigurator
import com.example.michl.aimission.MainScene.MainInteractorInput
import com.example.michl.aimission.MainScene.MainRouter
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main.*


interface MainFragmentInput {

    fun showAllUserItems(items: ArrayList<AimItem>)
    fun afterUserIdNotFound(errorMsg: String)
    fun afterMonthItemsLoadedSuccessfully(items: ArrayList<MonthItem>)
    fun afterMonthItemsLoadedFailed(errorMsg: String)

}

class MainFragment : MainFragmentInput, Fragment() {


    private lateinit var lytManager: RecyclerView.LayoutManager
    private lateinit var aimItemAdapter: RecyclerView.Adapter<*>
    private lateinit var monthItemAdapter: RecyclerView.Adapter<*>
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var router: MainRouter

    lateinit var output: MainInteractorInput

    val TAG = "MainFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        MainConfigurator.configure(this)

        // writing a sample data to db (users name)
        firebaseAuth = FirebaseAuth.getInstance()

        var firebaseDb = FirebaseDatabase.getInstance()
        var databaseRef = firebaseDb.getReference("Aim")


        // sample read out second dataset with known id

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("aimission", "an data changed error occured")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.i("aimission", "the data has changed")
                //output?.updateItemList(dataSnapshot)
            }

        })

        fabAddAim.setOnClickListener {
            activity?.supportFragmentManager?.apply {
                router.openAimDetailView()
            }
        }

        output?.getUsersMonthList()

        super.onViewCreated(view, savedInstanceState)
    }

    /*
    Loads all aim items for current user from db and creates list which is shown in MainFragment.
    If user is not logged in, we show an empty list with request to login instead.
     */
    override fun showAllUserItems(items: ArrayList<AimItem>) {

        aimItemAdapter = AimListAdapter(items)
        lytManager = LinearLayoutManager(activity?.applicationContext)


        // todo this is no longer needed because we show month items in main fragment.
        // todo use this code for showing users aim items in a seperate view after user clicked a specific month
//        monthListRV.apply {
//            setHasFixedSize(true)
//            adapter = aimItemAdapter
//            layoutManager = lytManager
//        }
    }

    override fun afterUserIdNotFound(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterMonthItemsLoadedSuccessfully(items: ArrayList<MonthItem>) {
        //todo show all month items in list
        monthItemAdapter = MonthListAdapter(items)
        lytManager = LinearLayoutManager(activity?.applicationContext)

        monthListRV.apply {
            setHasFixedSize(true)
            adapter = monthItemAdapter
            layoutManager = lytManager
        }

    }

    override fun afterMonthItemsLoadedFailed(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

}
