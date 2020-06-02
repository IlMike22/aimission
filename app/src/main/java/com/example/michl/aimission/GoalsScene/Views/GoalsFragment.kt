package com.example.michl.aimission.GoalsScene.Views


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.michl.aimission.Adapters.GoalsAdapter
import com.example.michl.aimission.GoalsScene.*
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Models.DefaultSortMode
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.DbHelper
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import com.example.michl.aimission.Utility.DbHelper.Companion.getCurrentUserId
import com.example.michl.aimission.Utility.GoalHelper.Companion.sortGoalsBySortMode
import com.example.michl.aimission.Utility.SettingHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_aim_list.*

class GoalsFragment : IGoalsFragment, Fragment(), IOnBackPressed {
    lateinit var router: GoalsRouter
    lateinit var output: IGoalsInteractor
    private lateinit var goalsAdapter: GoalsAdapter
    private lateinit var lytManager: RecyclerView.LayoutManager
    private lateinit var goals: ArrayList<Goal>
    var selectedMonthItem: Month? = null

    @SuppressLint("RestrictedApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        try {
            selectedMonthItem = activity?.intent?.getParcelableExtra("month") as Month?
        } catch (exc: Exception) {
            Log.i(TAG, "Cannot get intent data information month.${exc.message}")
        }

        val query = DbHelper.getGoalTableReference().child(getCurrentUserId())
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(dbError: DatabaseError) {
                Log.i(TAG, "A data changed error occured.")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                selectedMonthItem?.let { monthItem ->
                    selectedMonthItem?.year?.let { year ->
                        output.getGoals(
                                userId = getCurrentUserId(),
                                data = dataSnapshot,
                                selectedMonth = monthItem.month,
                                selectedYear = monthItem.year)
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
                router.showAimDetailView("", DateHelper.MODE_SELECTOR.Create, activity)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_goal_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onBackPressed(): Boolean {
        output.updateGoals()
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == R.id.menu_item_goal_list_sorting_priority) {
            if (::goals.isInitialized) {
                goalsAdapter.updateGoals(
                        sortGoalsBySortMode(
                                sortMode = DefaultSortMode.SORT_MODE_PRIORITY,
                                goals = goals
                        )
                )
            }
        }

        if (id == R.id.menu_item_goal_list_sorting_creation_date) {
            if (::goals.isInitialized) {
                goalsAdapter.updateGoals(
                        sortGoalsBySortMode(
                                sortMode = DefaultSortMode.SORT_MODE_CREATION_DATE,
                                goals = goals
                        )
                )
            }
        }

        if (id == R.id.menu_item_goal_list_sorting_done) {
            if (::goals.isInitialized) {
                goalsAdapter.updateGoals(
                        sortGoalsBySortMode(
                                sortMode = DefaultSortMode.SORT_MODE_ITEMS_DONE,
                                goals = goals
                        )
                )
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun afterUserIdNotFound(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterGoalsLoaded(goals: ArrayList<Goal>, month: Int, year: Int) {
        val userSettings = getUserSettings()
        goalsAdapter = GoalsAdapter(goals, userSettings, isActualMonth(), output, activity)
        lytManager = LinearLayoutManager(activity?.applicationContext)

        aimListRV?.apply {
            setHasFixedSize(true)
            adapter = goalsAdapter
            layoutManager = lytManager
        }
        this.goals = goals
        showViewWithGoals()

        output.storeGoalsInSharedPreferences(goals)
    }

    override fun afterNoGoalsFound(msg: String) {
        showEmptyView()
    }

    override fun afterGoalsLoadedFailed(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterGoalStatusChange(goal: Goal, position: Int) {
        goalsAdapter.notifyItemChanged(position)
        Log.i(TAG, "Item ${goal.title} successfully updated on position $position in list.")
    }

    override fun afterGoalStatusChangeFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterIterativeGoalsLoaded(goals: ArrayList<Goal>) {
        Log.i(TAG, "Amount of iterative items ${goals.size}")
        Toast.makeText(context, "Amount of iterative items: ${goals.size}", Toast.LENGTH_SHORT).show()
    }

    override fun afterIterativeGoalsLoadedFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterHighPriorityGoalsLoaded(goals: ArrayList<Goal>) {
        Toast.makeText(context, "Found ${goals.size} high priority items for user.", Toast.LENGTH_SHORT).show()
    }

    override fun afterHighPriorityGoalsLoadedFailed(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterGoalInformationLoaded(msgItemsCompleted: String, msgItemsHighPrio: String, msgItemsIterative: String) {

        //todo not working at the moment
        Log.i(TAG, msgItemsCompleted)
        Log.i(TAG, msgItemsHighPrio)
        Log.i(TAG, msgItemsIterative)
    }

    override fun afterGoalInformationLoadedFailed(errorMsg: String) {
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

    private fun showEmptyView() {
        includeEmptyTextView?.visibility = View.VISIBLE
        scrvAimList?.apply {
            visibility = View.GONE
        }
    }

    private fun showViewWithGoals() {
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

    private fun isActualMonth(): Boolean {
        return (DateHelper.getCurrentMonth() == selectedMonthItem?.month && DateHelper.getCurrentYear() == selectedMonthItem?.year)
    }
}
