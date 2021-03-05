package com.example.michl.aimission.landingpageScene.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.michl.aimission.adapters.MonthsAdapter
import com.example.michl.aimission.landingpageScene.ILandingpageFragment
import com.example.michl.aimission.landingpageScene.implementation.LandingpageConfigurator
import com.example.michl.aimission.landingpageScene.implementation.LandingpageInteractor
import com.example.michl.aimission.landingpageScene.implementation.LandingpageRouter
import com.example.michl.aimission.models.Month
import com.example.michl.aimission.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_landingpage.*

class LandingPageFragment : ILandingpageFragment, Fragment() {
    lateinit var router: LandingpageRouter
    lateinit var output: LandingpageInteractor
    private lateinit var monthsLayoutManager: RecyclerView.LayoutManager
    private lateinit var monthsAdapter: RecyclerView.Adapter<*>
    private lateinit var firebaseAuth: FirebaseAuth
    private val LOG = "Aimission"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_landingpage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let { _ ->
            LandingpageConfigurator.configure(this)
        }

        showProgressBar()

        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser

        if (user == null) {
            Toast.makeText(context,"Du bist nicht in Firebase angemeldet. Hole das nach.",Toast.LENGTH_SHORT).show()

            scroll_view_months.visibility = View.GONE // todo do it with data binding later
            emptyScreenContainer.visibility = View.VISIBLE // todo later with binding
            hideProgressBar()
            return
        }

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseDatabaseReference = firebaseDatabase.getReference("Aim")

        firebaseDatabaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.i(LOG, "A data changed error occured. Details ${error.message}")
            }

            override fun onDataChange(data: DataSnapshot) {
                Log.i(LOG, "the data has changed")

                output.getUsersMonthList(data)
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    /**
    Loads all goals for current user from db and creates list which is shown in MainFragment.
    If user is not logged in, we show an empty list with request to login instead.
     */
    override fun afterUserIdNotFound(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun afterMonthsLoadedSuccessfully(months: ArrayList<Month>) {
        hideProgressBar()
        context?.apply {
            monthsAdapter = MonthsAdapter(months, this)
        }

        monthsLayoutManager = LinearLayoutManager(activity?.applicationContext)

        recycler_view_months?.apply {
            setHasFixedSize(true)
            adapter = monthsAdapter
            layoutManager = monthsLayoutManager
        }
    }

    override fun afterMonthItemsLoadedError(errorMsg: String) {
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
    }

    override fun afterEmptyMonthListLoaded(message: String, month: Month) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        val monthList = ArrayList<Month>()

        monthList.add(month)

        context?.apply { monthsAdapter = MonthsAdapter(monthList, this) }
                ?: println("Aimission - No context found. Cannot call adapter.")

        monthsLayoutManager = LinearLayoutManager(activity?.applicationContext)

        recycler_view_months.apply {
            setHasFixedSize(true)
            adapter = monthsAdapter
            layoutManager = layoutManager
        }
    }

    override fun onResume() {
        super.onResume()
        if (::monthsAdapter.isInitialized)
            monthsAdapter.notifyDataSetChanged()
    }

    private fun showProgressBar() {
        progress_bar_info.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        if (progress_bar_info != null) {
            progress_bar_info.visibility = View.GONE
        }
    }
}
