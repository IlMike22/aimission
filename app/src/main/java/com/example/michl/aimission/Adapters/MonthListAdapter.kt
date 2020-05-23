package com.example.michl.aimission.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.michl.aimission.LandingpageScene.LandingpageRouter
import com.example.michl.aimission.Models.Month
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.cv_item_month.view.*

class MonthListAdapter(
        private val mDataSet: ArrayList<Month>,
        private val context: Context
) : RecyclerView.Adapter<MonthListAdapter.ViewHolderMonthItem>() {
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
            val goalsCompleted = mDataSet[position].goalsCompleted
            val goalsAmount = mDataSet[position].goalAmount

            monthItemCV.monthNameTV.text = "${mDataSet[position].name} ${mDataSet[position].year}"
            monthItemCV.aimAmountTV.text = "$goalsAmount Ziele insgesamt"
            monthItemCV.aimSucceededTV.text = "${getPercentOfCompletedGoals(goalsCompleted, goalsAmount)} % erreicht"
            setEmoji(
                    monthItem = this,
                    goalsAmount = goalsAmount,
                    goalsCompleted = goalsCompleted
            )
        }


        holder.monthItem.setOnClickListener {
            router.openAimListView(context, mDataSet[position].month, mDataSet[position].year)
        }
    }

    override fun getItemCount() = mDataSet.size

    private fun getPercentOfCompletedGoals(goalsCompleted: Int, goalsAmpount: Int): Int {
        if (goalsAmpount == 0)
            return 100
        return goalsCompleted * 100 / goalsAmpount
    }

    private fun setEmoji(
            monthItem: CardView,
            goalsAmount: Int,
            goalsCompleted: Int
    ) {

        val progress = getPercentOfCompletedGoals(goalsCompleted,goalsAmount)

        when (progress) {
            in 0..49 -> monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_sad)
            in 50..79 -> monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_average)
            in 80..99 -> monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_good)
            100 -> monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_great)
        }
    }
}