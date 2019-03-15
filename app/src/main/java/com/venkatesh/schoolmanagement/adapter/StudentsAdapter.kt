package com.venkatesh.schoolmanagement.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.activity.admin.ProfileDetailsActivity
import com.venkatesh.schoolmanagement.model.Profiles
import com.venkatesh.schoolmanagement.utilities.Constants

class StudentsAdapter(val context: Context, var data: ArrayList<Profiles>) :
    RecyclerView.Adapter<StudentsAdapter.EventViewHolder>() {

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventUser: TextView = view.findViewById(R.id.name)
        val className: TextView = view.findViewById(R.id.className)

        init {
            view.setOnClickListener {
                val intent = Intent(view.context, ProfileDetailsActivity::class.java)
                intent.putExtras(
                    Bundle().apply {
                        putSerializable(Constants.profile, data[adapterPosition])
                    }
                )
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_student_layout, p0, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: EventViewHolder, p1: Int) {
        val user = data[p1]
        p0.run {
            eventUser.text = user.userInfo?.name + " "
            className.text = user.userInfo?.className + " "
        }
    }

    fun updateData(list: ArrayList<Profiles>) {
        data = list
        notifyDataSetChanged()
    }
}