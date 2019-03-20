package com.example.michl.aimission.Adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.michl.aimission.AimDetailScene.AimDetailRouter
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.Models.Status
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.cv_item_aim.view.*

class AimListAdapter(private val data: ArrayList<AimItem>) : RecyclerView.Adapter<AimListAdapter.ViewHolderAimItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolderAimItem {
        val aimItem = LayoutInflater.from(parent.context).inflate(R.layout.cv_item_aim, parent, false) as CardView
        aimItem.setOnClickListener {
            //todo call router
        }
        return AimListAdapter.ViewHolderAimItem(aimItem)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolderAimItem, position: Int) {
        holder.aimItem.apply {
            aimItemCV.titleTV.text = data[position].title
//            aimItemCV.statusTV.text = getAimStatus(data[position].status ?: Status.UNDEFINED)
        }
    }

    private val router = AimDetailRouter()

    class ViewHolderAimItem(val aimItem: CardView) : RecyclerView.ViewHolder(aimItem)

    private fun getAimStatus(status: Status): String {
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


}