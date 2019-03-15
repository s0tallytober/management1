package com.venkatesh.schoolmanagement.activity.student

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import com.google.firebase.auth.FirebaseAuth
import com.venkatesh.schoolmanagement.ApiClient
import com.venkatesh.schoolmanagement.BaseActivityToChildActivity
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.RetrofitCallback
import com.venkatesh.schoolmanagement.activity.BaseActivity
import com.venkatesh.schoolmanagement.adapter.EventsAdapter
import com.venkatesh.schoolmanagement.model.SMSEvent
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_student_dashboard.*
import java.util.*

class StudentDashboardActivity : BaseActivity(), BaseActivityToChildActivity {
    override fun signOut() {
        mAuth.signOut()
    }

    var list = arrayListOf<SMSEvent>()
    lateinit var adapter: EventsAdapter
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)
        super.attachInstance(this)
        mAuth = FirebaseAuth.getInstance()
        title=getString(R.string.student_dashboard)
        initializeRecyclerView()
        getEventsFromFirebase()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val materials = menu?.findItem(R.id.materials)
        materials?.isVisible = true
        return true
    }


    private fun initializeRecyclerView() {
        recycleView.layoutManager = LinearLayoutManager(this)
        adapter = EventsAdapter(this@StudentDashboardActivity, list)
        recycleView.adapter = adapter
    }

    private fun getEventsFromFirebase() {
        ApiClient.getEvents(this@StudentDashboardActivity, object : RetrofitCallback() {
            override fun onResponse(any: Any) {
                list.clear()
                list = any as ArrayList<SMSEvent>
                adapter.updateData(list)
                Constants.eventsList = list
                recycleView.scrollToPosition(list.size)
                recycleView.smoothScrollToPosition(list.size)
            }
        })
    }
}
