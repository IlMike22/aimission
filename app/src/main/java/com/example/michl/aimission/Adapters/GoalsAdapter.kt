package com.example.michl.aimission.Adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.michl.aimission.GoalsScene.IGoalsInteractor
import com.example.michl.aimission.GoalsScene.implementation.GoalsRouter
import com.example.michl.aimission.Helper.DateHelper
import com.example.michl.aimission.Models.Goal
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.Aimission
import kotlinx.android.synthetic.main.cv_item_aim.view.*

class GoalsAdapter(
        private var data: List<Goal>,
        private val settingEditPastItems: Boolean, // todo remove this boolean and get list of user settings with all the values or better especially only for adapter
        private val isActualMonth: Boolean,
        interactor: IGoalsInteractor,
        activity: Activity? = null
) : RecyclerView.Adapter<GoalsAdapter.ViewHolderGoal>() {
    val router = GoalsRouter()
    private val interactor = interactor
    private val activity = activity ?: null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolderGoal {
        val aimItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_aim, parent, false) as CardView


        return ViewHolderGoal(aimItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolderGoal, position: Int) {
        holder.cardViewGoal.apply {
            aimItemCV.titleTV.text = data[position].title
            aimItemCV.statusTV.text = getAimStatus(data[position].status)
            aimItemCV.genreTV.text = getGenreAsText(data[position].genre)
            aimItemCV.descriptionTV.text = data[position].description

            if (data[position].isHighPriority) {
                ivHighPriority.visibility = View.VISIBLE
            } else {
                ivHighPriority.visibility = View.GONE
            }

            if (data[position].status == Status.OPEN)
                aimItemCV.btnFinishAim.setImageResource(R.drawable.ic_check_circle_black_24dp)
            else if (data[position].status == Status.DONE)
                aimItemCV.btnFinishAim.setImageResource(R.drawable.ic_check_circle_green_24dp)

            if (settingEditPastItems == false && !isActualMonth) {
                aimItemCV.btnFinishAim.setOnClickListener {
                    Toast.makeText(
                            context,
                            context.getString(R.string.list_adapter_toast_no_edit_in_past),
                            Toast.LENGTH_LONG
                    ).show()
                }
                return@apply
            }
            aimItemCV.btnFinishAim.setOnClickListener {
                interactor.changeGoalProgress(data[position], position)
            }
            aimItemCV.btnEditItem.visibility = View.VISIBLE
            aimItemCV.btnEditItem.setOnClickListener {
                router.showGoalDetail(data[position].id
                        ?: "", DateHelper.MODE_SELECTOR.Edit, activity)
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

    private fun setHighPriorityImage(isHighPriority: Boolean?): String {
        isHighPriority?.apply {

            val context = Aimission.getAppContext() ?: return ""
            if (this)
                return context.getString(R.string.priority_item_text)
        } ?: return ""

        return ""
    }

    private fun getGenreAsText(genre: Genre): String {
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

    private fun getAimStatus(status: Status?): String {

        val context = Aimission.getAppContext() ?: return ""

        status?.apply {
            return when (status) {
                Status.OPEN -> {
                    context.getString(R.string.status_item_open)
                }
                Status.PROGRESS -> {
                    context.getString(R.string.status_item_progress)
                }
                Status.DONE -> {
                    context.getString(R.string.status_item_done)
                }
                Status.UNDEFINED -> {
                    context.getString(R.string.status_item_undefined)
                }
            }
        }

        return ""
    }
}