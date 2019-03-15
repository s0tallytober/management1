package com.venkatesh.schoolmanagement.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.venkatesh.schoolmanagement.ApiClient
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.RetrofitCallback
import com.venkatesh.schoolmanagement.activity.admin.AddEventActivity
import com.venkatesh.schoolmanagement.adapter.EventsAdapter
import com.venkatesh.schoolmanagement.model.SMSEvent
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_events.*
import java.util.*

class EventsActivity : AppCompatActivity() {

    var list = arrayListOf<SMSEvent>()
    lateinit var adapter: EventsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        title = getString(R.string.events)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeRecyclerView()
        getEventsFromFirebase()
    }

    private fun initializeRecyclerView() {
        recyclerViewEvents.layoutManager = LinearLayoutManager(this)
        adapter = EventsAdapter(this@EventsActivity, list)
        recyclerViewEvents.adapter = adapter
    }

    private fun getEventsFromFirebase() {
        ApiClient.getEvents(this, object : RetrofitCallback() {
            override fun onResponse(any: Any) {
                list.clear()
                list = any as ArrayList<SMSEvent>
                list.reverse()
                adapter.updateData(list)
                Constants.eventsList = list
                // recyclerViewEvents.scrollToPosition(list.size)
                //recyclerViewEvents.smoothScrollToPosition(list.size)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        fabAddEvent.setOnClickListener {
            startActivityForResult(Intent(this@EventsActivity, AddEventActivity::class.java), Constants.REQUEST_CODE)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data.let {
            if (requestCode == Constants.REQUEST_CODE) {
                getEventsFromFirebase()
            }
        }

    }
}
