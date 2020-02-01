package com.example.michl.aimission.AimListScene.Views


import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
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
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.Aimission
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getCurrentUserId
import com.example.michl.aimission.Utility.SettingHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_aim_list.*


interface AimListFragmentInput {
    fun afterUserIdNotFound(msg: String)
    fun afterUserItemsLoadedSuccessfully(items: ArrayList<AimItem>, month: Int, year: Int)
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
    fun afterSPStoredSucceed(itemsDoneMsg: String, itemsHighPrioMsg: String, itemsIterativeMsg: String)
    fun afterSPStoredFailed(message: String)
}

class AimListFragment : AimListFragmentInput, Fragment(), IOnBackPressed {
    lateinit var router: AimListRouter
    lateinit var output: AimListInteractorInput
    private lateinit var aimListAdapter: RecyclerView.Adapter<*>
    private lateinit var lytManager: RecyclerView.LayoutManager
    var selectedMonth: Int? = 0
    var selectedYear: Int? = null
    val REQUEST_RELOAD_LIST = 101

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // get current month and year information via intent
        try {
            selectedMonth = activity?.intent?.getSerializableExtra("month") as Int
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
                        output.getItems(getCurrentUserId(), dataSnapshot)
                    }
                }
            }
        })

        return inflater.inflate(R.layout.fragment_aim_list, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AimListConfigurator.configure(this)

        if (isActualMonth())
            fltAddAimItem.visibility = View.VISIBLE
        else
            fltAddAimItem.visibility = View.GONE

        fltAddAimItem.setOnClickListener {
            activity?.supportFragmentManager?.apply {
                router.showAimDetailView("", MODE_SELECTOR.Create, activity)
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

    override fun afterUserItemsLoadedSuccessfully(items: ArrayList<AimItem>, month: Int, year: Int) {
        val userSettings = getUserSettings()
        aimListAdapter = AimListAdapter(items, userSettings, isActualMonth(), output, activity)
        lytManager = LinearLayoutManager(activity?.applicationContext)

        aimListRV?.apply {
            setHasFixedSize(true)
            adapter = aimListAdapter
            layoutManager = lytManager
        }

        showItemView()

        //get current amount of highPrio items, done items and iterative items
        //todo maybe it's better to get these information via firebase database query on demand?

        output.storeItemInformationInSharedPref(items)
    }

    override fun afterNoUserItemsFound(msg: String) {
        showEmptyTextView()
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

        //todo not working at the moment
        Log.i(TAG, msgItemsCompleted)
        Log.i(TAG, msgItemsHighPrio)
        Log.i(TAG, msgItemsIterative)
    }

    override fun afterItemInformationFromSharedPrefFailed(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterSPStoredSucceed(itemsDoneMsg: String, itemsHighPrioMsg: String, itemsIterativeMsg: String) {
        Log.i(TAG, "items done: $itemsDoneMsg")
        Log.i(TAG, "items high prio: $itemsHighPrioMsg")
        Log.i(TAG, "items iterative $itemsIterativeMsg")
    }

    override fun afterSPStoredFailed(message: String) {
        Log.e(TAG, message)
    }

    private fun showEmptyTextView() {
        includeEmptyTextView?.visibility = View.VISIBLE
        scrvAimList?.apply {
            visibility = View.GONE
        }
    }

    private fun showItemView() {
        includeEmptyTextView?.visibility = View.GONE
        scrvAimList?.apply {
            visibility = View.VISIBLE
        }
    }

    private fun getUserSettings(): Boolean {
        context?.apply {
            return SettingHelper.getEditItemInPastSetting(this)
        }
        return false
    }

    private fun isActualMonth():Boolean {
        return (DateHelper.getCurrentMonth() == selectedMonth && DateHelper.getCurrentYear() == selectedYear)
    }
}
