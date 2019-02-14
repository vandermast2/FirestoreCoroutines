package com.breez.firestore.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.breez.firestore.model.AnnounceModel
import com.breez.firestore.ui.base.OnItemClickedListener

class ListAdapter(private val listener: OnItemClickedListener<AnnounceModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list: List<AnnounceModel> = ArrayList()
    fun setList(list: List<AnnounceModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MyViewHolder).bind(list[position], listener)
    }
}