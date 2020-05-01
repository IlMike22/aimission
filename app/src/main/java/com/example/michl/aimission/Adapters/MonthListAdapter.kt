package com.example.michl.aimission.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.michl.aimission.LandingpageScene.LandingpageRouter
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.cv_item_month.view.*

class MonthListAdapter(private val mDataSet: ArrayList<MonthItem>, private val context: Context) : RecyclerView.Adapter<MonthListAdapter.ViewHolderMonthItem>() {

    private val router = LandingpageRouter()

    class ViewHolderMonthItem(val monthItem: CardView) : RecyclerView.ViewHolder(monthItem)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolderMonthItem {
        val monthItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_month, parent, false) as CardView
        monthItem.setOnClickListener {

        }
        return ViewHolderMonthItem(monthItem)
    }

    override fun onBindViewHolder(holder: ViewHolderMonthItem, position: Int) {
        holder.monthItem.apply {
            monthItemCV.monthNameTV.text = "${mDataSet[position].name} ${mDataSet[position].year}"
            monthItemCV.aimAmountTV.text = "${mDataSet[position].goalAmount} Ziele insgesamt"
            monthItemCV.aimSucceededTV.text = "${getPercentOfSucceededAims(mDataSet[position].goalsCompleted, mDataSet[position].goalAmount)} % erreicht"
        }

        holder.monthItem.setOnClickListener {
            router.openAimListView(context,mDataSet[position].month, mDataSet[position].year)
        }
    }

    override fun getItemCount() = mDataSet.size

    private fun getPercentOfSucceededAims(aimSucceeded: Int, aimAmount: Int):Int {
        if (aimAmount == 0)
            return 100
        return aimSucceeded * 100 / aimAmount
    }
}