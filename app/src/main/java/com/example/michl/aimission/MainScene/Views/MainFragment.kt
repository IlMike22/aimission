package com.example.michl.aimission.MainScene.Views

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.michl.aimission.Adapters.MonthListAdapter
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
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
        // todo delete this later
        var email = "michaelwidlok@gmail.com"
        var pswrd = "mepfde"
        firebaseAuth = FirebaseAuth.getInstance()

        // sample auth firebase with user credentials
        firebaseAuth.signInWithEmailAndPassword(email,pswrd)
                .addOnCompleteListener(activity as Activity) { task ->
                    if(task.isSuccessful)
                    {

                        Log.i("Aimission","Auth process success")
                    }
                    else
                    {
                        Log.i("Aimission","no success")
                    }
                }

        // sample firebase db write operation with two test datasets
        var aimItem = AimItem(232,"My new aim item",3,"This is my first aim item saved in firebase",1,2,0)
        var anotherAimItem = AimItem(343,"A scnd item",1,"Anche questa é disponibile",3,1,0)
        var array = ArrayList<AimItem>()
        array.add(aimItem)
        array.add(anotherAimItem)


        var user = firebaseAuth.currentUser
        Log.i("Aimission","User is ${user?.displayName}")
        var firebaseDb = FirebaseDatabase.getInstance()
        var databaseRef = firebaseDb.getReference("Aim")

        databaseRef.setValue(array)

        // sample read out second dataset with known id

        databaseRef.addValueEventListener(object: ValueEventListener
        {
            override fun onCancelled(p0: DatabaseError) {
                Log.i("aimission","an data changed error occured")
            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.i("aimission","the data has changed")
            }

        })

        array[1].description = "now we have another description and hopefully our data changed listener tells us something"
        databaseRef.setValue(array)


        //todo test data, remove it later
        val sampleData = MonthItem("Januar 2018", 19, 92)
        val data = ArrayList<MonthItem>()
        data.add(sampleData)
        data.add(sampleData)
        data.add(sampleData)
        data.add(sampleData)
        data.add(sampleData)
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
