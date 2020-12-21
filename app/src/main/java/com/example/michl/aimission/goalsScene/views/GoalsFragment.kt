package com.example.michl.aimission.goalsScene.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.michl.aimission.adapters.GoalsAdapter
import com.example.michl.aimission.goalsScene.*
import com.example.michl.aimission.goalsScene.implementation.GoalsConfigurator
import com.example.michl.aimission.goalsScene.implementation.GoalsRouter
import com.example.michl.aimission.utitlity.DateHelper
import com.example.michl.aimission.models.DefaultSortMode
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.models.Month
import com.example.michl.aimission.R
import com.example.michl.aimission.utitlity.DbHelper
import com.example.michl.aimission.utitlity.DbHelper.Companion.TAG
import com.example.michl.aimission.utitlity.DbHelper.Companion.getCurrentUserId
import com.example.michl.aimission.utitlity.GoalHelper.Companion.sortGoalsBySortMode
import com.example.michl.aimission.utitlity.SettingHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_goals.*

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
                                monthItem = monthItem)
                    }
                }
            }
        })
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GoalsConfigurator.configure(this)

        val isNotCurrentMonth = !isActualMonth()
        if (isNotCurrentMonth) {
            fab_add_goal.visibility = View.GONE
            return
        }

        fab_add_goal.visibility = View.VISIBLE

        fab_add_goal.setOnClickListener {
            activity?.supportFragmentManager?.apply {
                router.showGoalDetail("", DateHelper.MODE_SELECTOR.Create, activity)
            }
        }
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

    override fun afterGoalsLoaded(
            goals: ArrayList<Goal>,
            month: Int,
            year: Int,
            addedDefaultGoalsSize: Int
    ) {
        val userSettings = getUserSettings()
        val addedDefaultGoalsMessage = "A new month item was created and Aimission added $addedDefaultGoalsSize default goals to it."
        goalsAdapter = GoalsAdapter(
                data = goals,
                settingEditPastItems = userSettings,
                interactor = output,
                isActualMonth = isActualMonth(),
                activity = activity

        )
        lytManager = LinearLayoutManager(activity?.applicationContext)

        recycler_view_goals?.apply {
            setHasFixedSize(true)
            adapter = goalsAdapter
            layoutManager = lytManager
        }
        this.goals = goals
        showViewWithGoals()

        if (addedDefaultGoalsSize > 0) {
            Toast.makeText(context, addedDefaultGoalsMessage, Toast.LENGTH_SHORT).show()
        }


        output.storeGoalsInSharedPreferences(goals)
    }

    override fun afterNoGoalsFound(msg: String) {
        showEmptyView()
    }

    override fun afterGoalsLoadedError(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterGoalStatusChange(position: Int) {
        goalsAdapter.notifyItemChanged(position)
    }

    override fun afterGoalStatusChangeError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterIterativeGoalsLoaded(goals: ArrayList<Goal>) {
        Log.i(TAG, "Amount of iterative items ${goals.size}")
        Toast.makeText(context, "Amount of iterative items: ${goals.size}", Toast.LENGTH_SHORT).show()
    }

    override fun afterIterativeGoalsLoadedError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterHighPriorityGoalsLoaded(goals: ArrayList<Goal>) {
        Toast.makeText(context, "Found ${goals.size} high priority items for user.", Toast.LENGTH_SHORT).show()
    }

    override fun afterHighPriorityGoalsLoadedError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun afterGoalInformationLoaded(msgItemsCompleted: String, msgItemsHighPrio: String, msgItemsIterative: String) {

        //todo not working at the moment
        Log.i(TAG, msgItemsCompleted)
        Log.i(TAG, msgItemsHighPrio)
        Log.i(TAG, msgItemsIterative)
    }

    override fun afterGoalInformationLoadedError(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterSPStoredSuccess(itemsDoneMsg: String, itemsHighPrioMsg: String, itemsIterativeMsg: String) {
        Log.i(TAG, "items done: $itemsDoneMsg")
        Log.i(TAG, "items high prio: $itemsHighPrioMsg")
        Log.i(TAG, "items iterative $itemsIterativeMsg")
    }

    override fun afterSPStoredError(message: String) {
        Log.e(TAG, message)
    }

    private fun showEmptyView() {
        include_empty_view?.visibility = View.VISIBLE
        scroll_view_goals?.apply {
            visibility = View.GONE
        }
    }

    private fun showViewWithGoals() {
        include_empty_view?.visibility = View.GONE
        scroll_view_goals?.apply {
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
