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

class EventsAdapter(val context: Context, var data: List<SMSEvent>) :
    RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventUser: TextView = view.findViewById(R.id.eventUser)
        val eventTime: TextView = view.findViewById(R.id.eventTime)
        val eventMessage: TextView = view.findViewById(R.id.eventMessage)
        val eventImage: ImageView = view.findViewById(R.id.eventImage)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EventViewHolder {
        return EventViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.event_layout, p0, false))
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: EventViewHolder, p1: Int) {
        val smsEvent = data[p1]
        p0.run {
            eventUser.text = smsEvent.userName + " "
            eventTime.text = smsEvent.eventTime
            eventMessage.text = smsEvent.eventMessage
            Glide.with(context).load(smsEvent.eventImageUrl)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(eventImage)
        }
    }

    fun updateData(list: ArrayList<SMSEvent>) {
        data = list
        notifyDataSetChanged()
    }
}