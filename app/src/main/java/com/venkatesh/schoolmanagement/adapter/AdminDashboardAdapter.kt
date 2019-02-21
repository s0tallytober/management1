package com.venkatesh.schoolmanagement.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.adapter.AdminDashboardAdapter.AdminDashboardViewHolder

class AdminDashboardAdapter(val actionsList: List<String>) : RecyclerView.Adapter<AdminDashboardViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AdminDashboardViewHolder {
        return AdminDashboardViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.admin_dashboard_grid,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = actionsList.size

    override fun onBindViewHolder(p0: AdminDashboardViewHolder, p1: Int) {
        p0.label.text = actionsList[p1]
    }

    inner class AdminDashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label = view.findViewById<TextView>(R.id.cardViewLabel)
    }
}