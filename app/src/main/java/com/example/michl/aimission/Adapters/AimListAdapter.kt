package com.example.michl.aimission.Adapters

import android.app.Activity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.michl.aimission.AimListScene.AimListInteractorInput
import com.example.michl.aimission.AimListScene.AimListRouter
import com.example.michl.aimission.Helper.MODE_SELECTOR
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.Aimission
import kotlinx.android.synthetic.main.cv_item_aim.view.*

class AimListAdapter(
        private val data: ArrayList<AimItem>,
        private val settingEditPastItems: Boolean, // todo remove this boolean and get list of user settings with all the values or better especially only for adapter
        private val isActualMonth: Boolean,
        interactor: AimListInteractorInput,
        activity: Activity? = null
) : RecyclerView.Adapter<AimListAdapter.ViewHolderAimItem>() {

    val router = AimListRouter()

    private val interactor = interactor
    private val activity = activity ?: null


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolderAimItem {
        val aimItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_aim, parent, false) as CardView


        return ViewHolderAimItem(aimItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolderAimItem, position: Int) {
        holder.aimItem.apply {
            aimItemCV.titleTV.text = data[position].title
            aimItemCV.statusTV.text = getAimStatus(data[position].status)
            aimItemCV.aimTypeTV.text = getPriorityText(data[position].isHighPriority)
            aimItemCV.genreTV.text = getGenreAsText(data[position].genre ?: Genre.UNDEFINED)

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
                interactor.changeItemProgress(data[position], position)
            }
            aimItemCV.btnEditItem.visibility = View.VISIBLE
            aimItemCV.btnEditItem.setOnClickListener {
                router.showAimDetailView(data[position].id ?: "", MODE_SELECTOR.Edit, activity)
            }
        }
    }

    class ViewHolderAimItem(val aimItem: CardView) : RecyclerView.ViewHolder(aimItem)

    private fun getPriorityText(isHighPriority: Boolean?): String {
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