package com.breez.firestore.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breez.firestore.R
import com.breez.firestore.model.AnnounceModel
import com.breez.firestore.ui.base.OnItemClickedListener
import com.breez.firestore.util.Constants
import com.breez.firestore.util.onClick
import kotlinx.android.synthetic.main.item.view.*

class MyViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(announceModel: AnnounceModel?, listener: OnItemClickedListener<AnnounceModel>) {
        if (announceModel != null) {
            with(announceModel) {
                itemView.txtTitle.text = title
                itemView.txtMessage.text = message
                itemView.txtPrice.text = price
                itemView.txtAuthor.text = name
                itemView.txtCity.text = city
                itemView.txtTel.text = tel
                itemView.btnDelete.onClick { announceModel.let { listener.onClicked(it, Constants.DELETE_TYPE) } }
                itemView.onClick { announceModel.let { listener.onClicked(it, Constants.CLICK_TYPE) } }
                itemView.btnEdit.onClick { announceModel.let { listener.onClicked(it, Constants.EDIT_TYPE) } }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
            return MyViewHolder(view)
        }
    }
}