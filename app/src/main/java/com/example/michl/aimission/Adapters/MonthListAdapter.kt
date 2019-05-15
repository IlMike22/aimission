package com.example.michl.aimission.Adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.michl.aimission.MainScene.MainRouter
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.cv_item_aim.view.*
import kotlinx.android.synthetic.main.cv_item_month.view.*

class MonthListAdapter(private val mDataSet: ArrayList<MonthItem>) : RecyclerView.Adapter<MonthListAdapter.ViewHolderMonthItem>() {

    private val router = MainRouter()

    class ViewHolderMonthItem(val monthItem: CardView) : RecyclerView.ViewHolder(monthItem)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolderMonthItem {
        val monthItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_month, parent, false) as CardView
//        monthItem.setOnClickListener {
//
//        }
        return ViewHolderMonthItem(monthItem)
    }

    override fun onBindViewHolder(holder: ViewHolderMonthItem, position: Int) {
        holder.monthItem.apply {
            monthItemCV.monthNameTV.text = mDataSet[position].name
            monthItemCV.aimAmountTV.text = "${mDataSet[position].aimsAmount} Ziele insgesamt"
            monthItemCV.aimSucceededTV.text = "${mDataSet[position].aimsSucceeded} % erreicht"
        }

        holder.monthItem.setOnClickListener {
           // router.openAimListView(mDataSet[position].month, mDataSet[position].year) // todo fix compiler error 15.05.19
        }
    }

    override fun getItemCount() = mDataSet.size




}