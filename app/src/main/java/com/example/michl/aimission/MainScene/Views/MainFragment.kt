package com.example.michl.aimission.MainScene.Views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.michl.aimission.Adapters.MonthListAdapter
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {

    private lateinit var lytManager: RecyclerView.LayoutManager
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // writing a sample data to db (users name)
        firebaseAuth = FirebaseAuth.getInstance()

        var firebaseDb = FirebaseDatabase.getInstance()
        var databaseRef = firebaseDb.getReference("Aim")


        // sample read out second dataset with known id

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("aimission", "an data changed error occured")
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.i("aimission", "the data has changed")
            }

        })

//        array[1].description = "now we have another description and hopefully our data changed listener tells us something"
//        databaseRef.setValue(array)


        //todo test data, remove it later
//        val sampleData = MonthItem("Januar 2018", 19, 92)
        val data = ArrayList<MonthItem>()
//        data.add(sampleData)
//        data.add(sampleData)
//        data.add(sampleData)
//        data.add(sampleData)
//        data.add(sampleData)
        viewAdapter = MonthListAdapter(data)
        lytManager = LinearLayoutManager(activity?.applicationContext)

        // todo init recycler view here and connect it with layoutManager and Adapter
        // than you can see the sample item in the fragment (at least I hope so)

        monthListRV.apply {
            setHasFixedSize(true)
            adapter = viewAdapter
            layoutManager = lytManager
        }

        super.onViewCreated(view, savedInstanceState)
    }
}
