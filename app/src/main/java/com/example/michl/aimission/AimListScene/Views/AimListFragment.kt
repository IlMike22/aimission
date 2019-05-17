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
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
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
    fun afterNoUserItemsFound(msg:String)
}

class AimListFragment : AimListFragmentInput, Fragment() {


    lateinit var router: AimListRouter
    lateinit var output: AimListInteractorInput
    private lateinit var aimListAdapter: RecyclerView.Adapter<*>
    private lateinit var lytManager: RecyclerView.LayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var currentMonth:Month? = null
        var currentYear:Int? = null

        val firebaseDb = FirebaseDatabase.getInstance()
        var databaseRef = firebaseDb.getReference("Aim")

        // get current month and year information via intent
        try {
            currentMonth = activity?.intent?.getSerializableExtra("month") as Month
            currentYear = activity?.intent?.getIntExtra("year",0)?:Log.i(TAG,"Cannot get intent data information year. Value is null.")

        }
        catch(exc:Exception)
        {
            Log.i(TAG,"Cannot get intent data information month.${exc.message}")
        }


        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "A data changed error occured.")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.i(TAG, "The data has changed.")
                currentMonth?.let { month ->
                    currentYear?.apply {
                        output?.getItems(dataSnapshot, currentMonth, currentYear)
                    }
                }
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

        aimListAdapter = AimListAdapter(items)
        lytManager = LinearLayoutManager(activity?.applicationContext)

        aimListRV?.apply {
            setHasFixedSize(true)
            adapter = aimListAdapter
            layoutManager = lytManager
        }

    }

    override fun afterNoUserItemsFound(msg: String) {
        //todo later we show an empty screen and no longer a toast
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    override fun afterUserItemsLoadedFailed(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }


}
