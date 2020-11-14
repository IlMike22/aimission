package com.example.michl.aimission.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.michl.aimission.landingpageScene.implementation.LandingpageRouter
import com.example.michl.aimission.models.Month
import com.example.michl.aimission.R
import com.example.michl.aimission.utitlity.DateHelper.DateHelper.isCurrentMonthAlreadyFinished
import kotlinx.android.synthetic.main.cv_month.view.*

class MonthListAdapter(
        private val mDataSet: ArrayList<Month>,
        private val context: Context
) : RecyclerView.Adapter<MonthListAdapter.ViewHolderMonthItem>() {
    private val router = LandingpageRouter()

    class ViewHolderMonthItem(val monthItem: CardView) : RecyclerView.ViewHolder(monthItem)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolderMonthItem {
        val monthItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_month, parent, false) as CardView
        monthItem.setOnClickListener {

        }
        return ViewHolderMonthItem(monthItem)
    }

    override fun onBindViewHolder(holder: ViewHolderMonthItem, position: Int) {
        holder.monthItem.apply {
            val currentMonth = mDataSet[position]
            val goalsCompleted = currentMonth.goalsCompleted
            val goalsAmount = currentMonth.goalAmount
            val goalsNotCompleted = currentMonth.goalAmount - goalsCompleted
            val month = currentMonth.month

            monthItemCV.header.background = getCurrentMonthBackground(month)
            monthItemCV.goalAmountTV.text = "$goalsAmount Ziele insgesamt"
            monthItemCV.goalSucceededTV.text = "${getPercentOfCompletedGoals(goalsCompleted, goalsAmount)} % erreicht"
            monthItemCV.goalNotCompletedTV.text = getGoalsNotCompletedText(goalsNotCompleted)

            setRedTextColorIfCurrentMonthHasAlreadyFinished(
                    month = currentMonth,
                    textview = monthItemCV.goalNotCompletedTV
            )

            setEmoji(
                    monthItem = this,
                    goalsAmount = goalsAmount,
                    goalsCompleted = goalsCompleted,
                    isDepecrecated = mDataSet[position].isDepecrecated
            )
        }

        holder.monthItem.setOnClickListener {
            router.openGoals(
                    context = context,
                    month = mDataSet[position]
            )
        }
    }

    override fun getItemCount() = mDataSet.size

    private fun setRedTextColorIfCurrentMonthHasAlreadyFinished(month:Month, textview:TextView) {
        if (month.isCurrentMonthAlreadyFinished())
        textview.setTextColor(Color.RED)
    }
    private fun getGoalsNotCompletedText(goalsNotCompleted:Int):String =
            when(goalsNotCompleted) {
                1 -> "$goalsNotCompleted Ziel noch offen"
                2 -> "$goalsNotCompleted Ziele noch offen"
                else -> ""
            }
    private fun getCurrentMonthBackground(month: Int): Drawable? =
            when (month) {
                1 -> context.getDrawable(R.drawable.january)
                2 -> context.getDrawable(R.drawable.february)
                3 -> context.getDrawable(R.drawable.march)
                4 -> context.getDrawable(R.drawable.april)
                5 -> context.getDrawable(R.drawable.may)
                6 -> context.getDrawable(R.drawable.june)
                7 -> context.getDrawable(R.drawable.july)
                8 -> context.getDrawable(R.drawable.august)
                9 -> context.getDrawable(R.drawable.september)
                10 -> context.getDrawable(R.drawable.october)
                11 -> context.getDrawable(R.drawable.november)
                12 -> context.getDrawable(R.drawable.december)
                else -> context.getDrawable(R.drawable.january)

            }

    private fun getPercentOfCompletedGoals(
            goalsCompleted: Int,
            goalsAmpount: Int
    ): Int {
        if (goalsAmpount == 0)
            return 100
        return goalsCompleted * 100 / goalsAmpount
    }

    private fun setEmoji(
            monthItem: CardView,
            goalsAmount: Int,
            goalsCompleted: Int,
            isDepecrecated: Boolean
    ) {
        val progress = getPercentOfCompletedGoals(goalsCompleted, goalsAmount)

        if (progress == 0 && isDepecrecated) {
            monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_destructive)
            return
        }

        when (progress) {
            in 0..49 -> monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_sad)
            in 50..79 -> monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_average)
            in 80..99 -> monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_good)
            100 -> monthItem.ivEmoji.setImageResource(R.drawable.ic_emoji_great)
        }
    }
}