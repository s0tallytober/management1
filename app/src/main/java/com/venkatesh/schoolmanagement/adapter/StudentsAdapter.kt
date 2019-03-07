package com.venkatesh.schoolmanagement.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.model.SMSEvent
import com.venkatesh.schoolmanagement.model.UserProfile

class StudentsAdapter(val context: Context, var data: List<UserProfile>) :
    RecyclerView.Adapter<StudentsAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventUser: TextView = view.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_student_layout, p0, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: EventViewHolder, p1: Int) {
        val user = data[p1]
        p0.run {
            eventUser.text = user.userName + " "
        }
    }

    fun updateData(list: ArrayList<UserProfile>) {
        data = list
        notifyDataSetChanged()
    }
}