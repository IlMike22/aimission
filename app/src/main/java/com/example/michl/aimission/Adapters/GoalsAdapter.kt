package com.example.michl.aimission.Adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.michl.aimission.GoalsScene.IGoalsInteractor
import com.example.michl.aimission.GoalsScene.implementation.GoalsRouter
import com.example.michl.aimission.Utility.DateHelper
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.Aimission
import kotlinx.android.synthetic.main.cv_goal.view.*

class GoalsAdapter(
        private var data: List<Goal>,
        private val settingEditPastItems: Boolean,
        private val interactor: IGoalsInteractor,
        private val isActualMonth: Boolean,
        private val activity: Activity? = null
) : RecyclerView.Adapter<GoalsAdapter.ViewHolderGoal>() {
    val router = GoalsRouter()

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolderGoal {
        val aimItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_goal, parent, false) as CardView

        return ViewHolderGoal(aimItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(
            holder: ViewHolderGoal,
            position: Int
    ) {
        holder.cardViewGoal.apply {
            val goal = data[position]
            goalItemCV.titleTV.text = goal.title
            goalItemCV.progressTV.text = setGoalProgressText(goal)
            goalItemCV.repeatTV.text = createRepeatText(goal.isComingBack)

            if (goal.isHighPriority) {
                ivHighPriority.visibility = View.VISIBLE
            }

            if (settingEditPastItems == false && !isActualMonth) {
                goalItemCV.btnFinishGoal.setOnClickListener {
                    Toast.makeText(
                            context,
                            context.getString(R.string.list_adapter_toast_no_edit_in_past),
                            Toast.LENGTH_LONG
                    ).show()
                }
                return@apply
            }
            goalItemCV.btnFinishGoal.setOnClickListener {
                interactor.changeGoalProgress(goal, position)
            }
            goalItemCV.setOnClickListener {
                router.showGoalDetail(goal.id
                        ?: "", DateHelper.MODE_SELECTOR.Edit, activity)
            }

            if (goal.status == Status.PROGRESS) {
                setPartlyGoalAchievedIcon(goalItemCV)
                return@apply
            }

            if (goal.status == Status.DONE) {
                goalItemCV.btnFinishGoal.setImageResource(R.drawable.ic_check_circle_green_24dp)
            }
        }
    }

    class ViewHolderGoal(val cardViewGoal: CardView) : RecyclerView.ViewHolder(cardViewGoal)

    fun updateGoals(goals: List<Goal>) {
        if (goals.isEmpty())
            return

        data = goals
        notifyDataSetChanged()
    }

    private fun createRepeatText(isRepeatable: Boolean): String = if (isRepeatable) "wiederholend" else "nicht wiederholend"

    private fun setGoalProgressText(goal:Goal):String {
        if (goal.partGoalsAchieved == 0)
            return when (goal.status) {
                Status.OPEN -> "offen"
                Status.DONE -> "erledigt"
                else -> "undefined"
            }

        return createPartGoalsAchievedText(
                partGoalsAchieved = goal.partGoalsAchieved,
                repeatCount = goal.repeatCount
        )
    }

    private fun createPartGoalsAchievedText(partGoalsAchieved: Int, repeatCount: Int) = "$partGoalsAchieved von $repeatCount Teilzielen erreicht"

    private fun setPartlyGoalAchievedIcon(goalItemCV: View) = goalItemCV.btnFinishGoal.setImageResource(R.drawable.ic_check_circle_black_24dp_partly_done)

    private fun getGenreAsText(genre: Genre): String {
        //todo update this function. we dont need text here, we need the correct icon for genre

        val context = Aimission.getAppContext() ?: return ""

        return when (genre) {
            Genre.PRIVATE -> context.getString(R.string.genre_item_private)
            Genre.EDUCATION -> context.getString(R.string.genre_item_progress_private)
            Genre.FUN -> context.getString(R.string.genre_item_fun)
            Genre.WORK -> context.getString(R.string.genre_item_work)
            Genre.HEALTH -> context.getString(R.string.genre_item_health)
            Genre.FINANCES -> context.getString(R.string.genre_item_finances)
            Genre.UNDEFINED -> context.getString(R.string.genre_item_undefined)
        }
    }
}

