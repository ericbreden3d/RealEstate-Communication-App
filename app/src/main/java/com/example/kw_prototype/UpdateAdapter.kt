package com.example.kw_prototype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for Update recycler view in Update Fragment
class UpdateAdapter(private val updates: MutableList<Update>)
    : RecyclerView.Adapter<UpdateAdapter.UpdateHolder>() {

    class UpdateHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val txtUpdate = itemView.findViewById<TextView>(R.id.txt_update)
        internal val txtDate = itemView.findViewById<TextView>(R.id.txt_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateHolder {
        return UpdateHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.update_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UpdateHolder, position: Int) {
        val curUpdate = updates[position]
        holder.txtUpdate.text = curUpdate.message
        holder.txtDate.text = curUpdate.time
    }

    override fun getItemCount(): Int {
        return updates.size
    }
}