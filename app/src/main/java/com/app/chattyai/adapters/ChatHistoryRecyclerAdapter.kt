package com.app.chattyai.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.chattyai.R
import com.app.chattyai.models.chatHistory.Data

class ChatHistoryRecyclerAdapter(private val activity: FragmentActivity,val  data: List<Data>?) : RecyclerView.Adapter<ChatHistoryRecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatHistoryRecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chathistoryitem, parent, false)
        return ChatHistoryRecyclerAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvhistory.text = data?.get(position)?.question
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tvhistory = itemView.findViewById<TextView>(R.id.tvhistory)


    }
}