package com.venkatesh.schoolmanagement.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.activity.EventsActivity
import com.venkatesh.schoolmanagement.activity.admin.*
import com.venkatesh.schoolmanagement.adapter.AdminDashboardAdapter.AdminDashboardViewHolder
import com.venkatesh.schoolmanagement.utilities.Constants

//Adapter to show list of grid options
class AdminDashboardAdapter(val context: Context, private val actionsList: List<String>) :
    RecyclerView.Adapter<AdminDashboardViewHolder>() {
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
        p0.run {
            label.text = actionsList[p1]
            adminDashboardCardView.setOnClickListener {
                when (p1) {
                    0 -> {
                        val intent = Intent(context, StudentsActivity::class.java)
                        intent.putExtra(Constants.user, Constants.studnet)
                        context.startActivity(intent)
                    }

                    1 -> {
                        val intent = Intent(context, TeachersActivity::class.java)
                        intent.putExtra(Constants.user, Constants.teacher)
                        context.startActivity(intent)
                    }

                    2 -> {
                        val intent = Intent(context, CreateStudentTeacherActivity::class.java)
                        intent.putExtra(Constants.user, Constants.admin)
                        context.startActivity(intent)
                    }

                    3 -> {
                        val intent = Intent(context, MaterialsActivity::class.java)
                        context.startActivity(intent)
                    }

                    4 -> {
                        val intent = Intent(context, EventsActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    inner class AdminDashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label = view.findViewById<TextView>(R.id.cardViewLabel)!!
        val adminDashboardCardView = view.findViewById<CardView>(R.id.adminDashboardCardView)!!
    }
}