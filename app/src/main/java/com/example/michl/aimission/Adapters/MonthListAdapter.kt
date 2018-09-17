package com.example.michl.aimission.Adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.michl.aimission.MainScene.MainRouter
import com.example.michl.aimission.Models.MonthItem
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.cv_item_month.view.*

class MonthListAdapter(private val mDataSet: ArrayList<MonthItem>) : RecyclerView.Adapter<MonthListAdapter.MyViewHolder>() {

    private val router = MainRouter()

    class MyViewHolder(val monthItem: CardView) : RecyclerView.ViewHolder(monthItem)

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val monthItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_month, parent, false) as CardView
        monthItem.setOnClickListener {
            router.openMonthItemDetails()
        }
        return MyViewHolder(monthItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.monthItem.apply {
            monthItemCV.monthNameTV.text = mDataSet[position].name
            monthItemCV.aimAmountTV.text = "${mDataSet[position].aimsAmount.toString()} Ziele insgesamt"
            monthItemCV.aimSucceededTV.text = "${mDataSet[position].aimsSucceeded.toString()} % erreicht"
        }
    }

    override fun getItemCount() = mDataSet.size




}