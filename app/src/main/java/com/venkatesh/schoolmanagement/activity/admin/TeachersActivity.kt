package com.venkatesh.schoolmanagement.activity.admin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.venkatesh.schoolmanagement.ApiClient
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.RetrofitCallback
import com.venkatesh.schoolmanagement.adapter.StudentsAdapter
import com.venkatesh.schoolmanagement.model.UserProfile
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_events.*
import java.util.*

class TeachersActivity : AppCompatActivity() {

    var list = arrayListOf<UserProfile>()
    lateinit var adapter: StudentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        title = getString(R.string.teacher)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeRecyclerView()
        getTeachersFromFirebase()
    }

    private fun initializeRecyclerView() {
        recyclerViewEvents.layoutManager = LinearLayoutManager(this)
        adapter = StudentsAdapter(this@TeachersActivity, list)
        recyclerViewEvents.adapter = adapter
    }

    private fun getTeachersFromFirebase() {
        ApiClient.getTeachers(this, object : RetrofitCallback() {
            override fun onResponse(any: Any) {
                list.clear()
                list = any as ArrayList<UserProfile>
                adapter.updateData(list)
                Constants.teachersData = list
                recyclerViewEvents.scrollToPosition(list.size)
                recyclerViewEvents.smoothScrollToPosition(list.size)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        fabAddEvent.setOnClickListener {
            val intent = Intent(this, CreateStudentTeacherActivity::class.java)
            intent.putExtra(Constants.user, Constants.teacher)
            startActivity(intent)
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
                getTeachersFromFirebase()
            }
        }

    }
}
