package com.example.michl.aimission.Adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.michl.aimission.AimListScene.AimListInteractor
import com.example.michl.aimission.AimListScene.AimListRouter
import com.example.michl.aimission.Helper.MODE_SELECTOR
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Genre
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.cv_item_aim.view.*

class AimListAdapter(private val data: ArrayList<AimItem>) : RecyclerView.Adapter<AimListAdapter.ViewHolderAimItem>() {

    val router = AimListRouter()
    private val interactor = AimListInteractor()


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolderAimItem {
        val aimItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_aim, parent, false) as CardView


        return ViewHolderAimItem(aimItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolderAimItem, position: Int) {
        holder.aimItem.apply {
            aimItemCV.titleTV.text = data[position].title
            aimItemCV.statusTV.text = getAimStatus(data[position].status)
            aimItemCV.aimTypeTV.text = getPriorityText(data[position].highPriority)
            aimItemCV.genreTV.text = getGenreAsText(data[position].genre ?: Genre.UNDEFINED)

            aimItemCV.btnFinishAim.setOnClickListener {

                // For this we connect to database. After that we change also the icon color (grey = open, yellow = progress, green = done)
                // the yellow status appears the whole time, the item has already subaims opened.
                interactor.changeItemProgress(data[position])

                if (data[position].status == Status.OPEN)
                    aimItemCV.btnFinishAim.setImageResource(R.drawable.ic_check_circle_black_24dp)
                else if (data[position].status == Status.DONE)
                    aimItemCV.btnFinishAim.setImageResource(R.drawable.ic_check_circle_green_24dp)
            }

            aimItemCV.btnEditItem.setOnClickListener {
                router.showAimDetailView(data[position].id ?: "", MODE_SELECTOR.Edit)
            }

            if (data[position].status == Status.OPEN)
                aimItemCV.btnFinishAim.setImageResource(R.drawable.ic_check_circle_black_24dp)
            else if (data[position].status == Status.DONE)
                aimItemCV.btnFinishAim.setImageResource(R.drawable.ic_check_circle_green_24dp)
        }
    }

    class ViewHolderAimItem(val aimItem: CardView) : RecyclerView.ViewHolder(aimItem)

    private fun getPriorityText(isHighPriority: Boolean?): String {
        isHighPriority?.apply {

            if (this)
                return "A-Ziel"
        } ?: return ""

        return ""
    }

    private fun getGenreAsText(genre: Genre): String {
        return when (genre) {
            Genre.PRIVATE -> "privat"
            Genre.EDUCATION -> "Weiterbildung privat"
            Genre.FUN -> "Spaß und Freizeit"
            Genre.WORK -> "Arbeit und berufliche Weiterbildung"
            Genre.HEALTH -> "Gesundheit und Fittness"
            Genre.FINANCES -> "Finanzen und Sparen"
            Genre.UNDEFINED -> "nicht definiertes Genre"
        }
    }

    private fun getAimStatus(status: Status?): String {

        status?.apply {
            return when (status) {
                Status.OPEN -> {
                    "Status: offen"
                }
                Status.PROGRESS -> {
                    "Status: teilweise erledigt"
                }
                Status.DONE -> {
                    "Status: erledigt"
                }
                Status.UNDEFINED -> {
                    "Status: undefiniert"
                }
            }
        }

        return ""
    }


}