package com.example.michl.aimission.Adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.michl.aimission.AimListScene.AimListRouter
import com.example.michl.aimission.Models.AimItem
import com.example.michl.aimission.R
import kotlinx.android.synthetic.main.cv_item_aim.view.*

class AimListAdapter(private val data:ArrayList<AimItem>):RecyclerView.Adapter<AimListAdapter.ViewHolderAimItem>()
{
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
          //todo set progress here
            //aimItemCV.progressSwitchIV.text = "${data[position].aimsSucceeded.toString()} % erreicht"
        }
    }

    private val router = AimListRouter()
    class ViewHolderAimItem(val aimItem: CardView):RecyclerView.ViewHolder(aimItem)


}