package com.example.michl.aimission.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.michl.aimission.R
import com.example.michl.aimission.goalsScene.IGoalsInteractor
import com.example.michl.aimission.goalsScene.implementation.GoalsRouter
import com.example.michl.aimission.models.Genre
import com.example.michl.aimission.models.Goal
import com.example.michl.aimission.models.Status
import com.example.michl.aimission.utitlity.Aimission
import com.example.michl.aimission.utitlity.DateHelper
import kotlinx.android.synthetic.main.cv_goal.view.*

class GoalsAdapter(
        private var data: List<Goal>,
        private val settingEditPastItems: Boolean,
        private val interactor: IGoalsInteractor,
        private val isActualMonth: Boolean,
        private val activity: Activity? = null
) : RecyclerView.Adapter<GoalsAdapter.ViewHolderGoal>() {
    val router = GoalsRouter()
    val context = Aimission.context

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

            val areSettingsNotEditableInPast = !settingEditPastItems
            val isNotActualMonth = !isActualMonth

            if (areSettingsNotEditableInPast && isNotActualMonth) {
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

            goalItemCV.iv_genre.setImageResource(setGenreIcon(goal.genre))

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

    private fun createRepeatText(isRepeatable: Boolean): String = if (isRepeatable) context.getString(R.string.goals_adapter_repeat_text_repeat) else context.getString(R.string.goals_adapter_repeat_text_not_repeat)

    private fun setGoalProgressText(goal: Goal): String {
        if (goal.partGoalsAchieved == 0)
            return when (goal.status) {
                Status.OPEN -> context.getString(R.string.goals_adapter_status_open)
                Status.DONE -> context.getString(R.string.goals_adapter_status_done)
                else -> context.getString(R.string.goals_adapter_status_undefined)
            }

        return createPartGoalsAchievedText(
                partGoalsAchieved = goal.partGoalsAchieved,
                repeatCount = goal.repeatCount
        )
    }

    private fun createPartGoalsAchievedText(partGoalsAchieved: Int, repeatCount: Int) = "$partGoalsAchieved von $repeatCount Teilzielen erreicht"

    private fun setPartlyGoalAchievedIcon(goalItemCV: View) = goalItemCV.btnFinishGoal.setImageResource(R.drawable.ic_check_circle_black_24dp_partly_done)

    private fun setGenreIcon(genre: Genre): Int {
        return when (genre) {
            Genre.EDUCATION -> R.drawable.ic_education
            Genre.FINANCES -> R.drawable.ic_finance
            Genre.HEALTH -> R.drawable.ic_health
            Genre.FUN -> R.drawable.ic_fun
            Genre.WORK -> R.drawable.ic_work
            else -> R.drawable.ic_private
        }
    }
}

