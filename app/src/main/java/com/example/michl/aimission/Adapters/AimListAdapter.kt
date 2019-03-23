package com.example.michl.aimission.Adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.michl.aimission.AimDetailScene.AimDetailRouter
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.R
import com.example.michl.aimission.Utility.DbHelper.Companion.TAG
import kotlinx.android.synthetic.main.cv_item_aim.view.*
import kotlinx.android.synthetic.main.cv_item_month.view.*

class AimListAdapter(private val data: ArrayList<AimItem>) : RecyclerView.Adapter<AimListAdapter.ViewHolderAimItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolderAimItem {
        val aimItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_aim, parent, false) as CardView
        aimItem.setOnClickListener {
            //todo call router and show aim detail view
        }
        return AimListAdapter.ViewHolderAimItem(aimItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolderAimItem, position: Int) {
        holder.aimItem.apply {
            aimItemCV.titleTV.text = data[position].title
            aimItemCV.statusTV.text = getAimStatus(data[position].status)
            aimItemCV.aimTypeTV.text = getPriorityText(data[position].highPriority)

            aimItemCV.btnFinishAim.setOnClickListener {

                //todo If user clicks on checkmark button we change the status of the item.
                // For this we connect to database. After that we change also the icon color (grey = open, yellow = progress, green = done)
                // the yellow status appears the whole time, the item has already subaims opened.


                if (data[position].status == Status.OPEN)
                    aimItemCV.btnFinishAim.setImageResource(R.drawable.ic_check_circle_green_24dp)
                else if (data[position].status == Status.DONE)
                    aimItemCV.btnFinishAim.setImageResource(R.drawable.ic_check_circle_black_24dp)

            }
        }
    }

    private val router = AimDetailRouter()

    class ViewHolderAimItem(val aimItem: CardView) : RecyclerView.ViewHolder(aimItem)

    private fun getPriorityText(isHighPriority: Boolean?): String {
        isHighPriority?.apply {

            if (this)
                return "A-Ziel"
        } ?: return ""

        return ""
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