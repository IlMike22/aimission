package com.example.michl.aimission.AimListScene.Views


import android.annotation.SuppressLint
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
import com.example.michl.aimission.AimListScene.IOnBackPressed
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Helper.MODE_SELECTOR
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getCurrentUserId
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_aim_list.*


interface AimListFragmentInput {
    fun afterUserIdNotFound(msg: String)
    fun afterUserItemsLoadedSuccessfully(items: ArrayList<AimItem>, month: Month, year: Int)
    fun afterUserItemsLoadedFailed(errorMsg: String)
    fun afterNoUserItemsFound(msg: String)
    fun afterItemStatusChangeSucceed(item: AimItem, position: Int)
    fun afterItemStatusChangeFailed(msg: String)
    fun afterIterativeItemsGot(items: ArrayList<AimItem>)
    fun afterIterativeItemsGotFailed(msg: String)
    fun afterHighPriorityItemsGot(items: ArrayList<AimItem>)
    fun afterHighPriorityItemsGotFailed(msg: String)
    fun afterItemInformationFromSharedPrefSucceed(msgItemsCompleted: String, msgItemsHighPrio: String, msgItemsIterative: String)
    fun afterItemInformationFromSharedPrefFailed(errorMsg: String)
}

class AimListFragment : AimListFragmentInput, Fragment(), IOnBackPressed {

    lateinit var router: AimListRouter
    lateinit var output: AimListInteractorInput
    private lateinit var aimListAdapter: RecyclerView.Adapter<*>
    private lateinit var lytManager: RecyclerView.LayoutManager
    var selectedMonth: Month? = null
    var selectedYear: Int? = null

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // get current month and year information via intent
        try {
            selectedMonth = activity?.intent?.getSerializableExtra("month") as Month
            selectedYear = activity?.intent?.getIntExtra("year", 0)
                    ?: Log.i(TAG, "Cannot get intent data information year. Value is null.")

        } catch (exc: Exception) {
            Log.i(TAG, "Cannot get intent data information month.${exc.message}")
        }

        Log.i(TAG, "current month is ${DateHelper.getCurrentMonth()}, selected month is $selectedMonth")

        val query = DbHelper.getAimTableReference().child(getCurrentUserId())
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.i(TAG, "A data changed error occured.")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //todo onDataChange must not be called if you click a list item button such as checkmark or edit button....
                Log.i(TAG, "The data has changed.")
                selectedMonth?.let { month ->
                    selectedYear?.apply {
                        output.getItems(getCurrentUserId(), dataSnapshot, selectedMonth as Month, selectedYear as Int)
                    }
                }
            }
        })

        return inflater.inflate(R.layout.fragment_aim_list, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AimListConfigurator.configure(this)

        if (DateHelper.getCurrentMonth() == selectedMonth)
            fltAddAimItem.visibility = View.VISIBLE
        else
            fltAddAimItem.visibility = View.GONE

        fltAddAimItem.setOnClickListener {
            activity?.supportFragmentManager?.apply {
                router.showAimDetailView("", MODE_SELECTOR.Create)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onBackPressed(): Boolean {
        output.updateItemList()
        return false
    }


    override fun afterUserIdNotFound(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterUserItemsLoadedSuccessfully(items: ArrayList<AimItem>, month: Month, year: Int) {

        includeEmptyTextView?.visibility = View.GONE

        aimListAdapter = AimListAdapter(items, output)
        lytManager = LinearLayoutManager(activity?.applicationContext)

        aimListRV?.apply {
            setHasFixedSize(true)
            adapter = aimListAdapter
            layoutManager = lytManager
        }

        //get current amount of highPrio items, done items and iterative items
        //todo maybe it's better to get these information via firebase database query on demand?

        output.getItemInformationFromSharedPrefs(month, year)


    }

    override fun afterNoUserItemsFound(msg: String) {

        includeEmptyTextView?.visibility = View.VISIBLE
        scrvAimList.visibility = View.GONE
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterUserItemsLoadedFailed(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterItemStatusChangeSucceed(item: AimItem, position: Int) {
        aimListAdapter.notifyItemChanged(position)
        Log.i(TAG, "Item ${item.title} successfully updated on position $position in list.")
    }

    override fun afterItemStatusChangeFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterIterativeItemsGot(items: ArrayList<AimItem>) {
        Log.i(TAG, "Amount of iterative items ${items.size}")
        Toast.makeText(context, "Amount of iterative items: ${items.size}", Toast.LENGTH_SHORT).show()
    }

    override fun afterIterativeItemsGotFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterHighPriorityItemsGot(items: ArrayList<AimItem>) {
        Toast.makeText(context, "Found ${items.size} high priority items for user.", Toast.LENGTH_SHORT).show()
    }

    override fun afterHighPriorityItemsGotFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterItemInformationFromSharedPrefSucceed(msgItemsCompleted: String, msgItemsHighPrio: String, msgItemsIterative: String) {

        //todo not working at the moment, deactivated
//        Toast.makeText(context,msgItemsCompleted,Toast.LENGTH_SHORT).show()
//        Toast.makeText(context,msgItemsHighPrio,Toast.LENGTH_SHORT).show()
//        Toast.makeText(context,msgItemsIterative,Toast.LENGTH_SHORT).show()
    }

    override fun afterItemInformationFromSharedPrefFailed(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }
}
