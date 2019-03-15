package com.venkatesh.schoolmanagement.activity.admin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.SMSApplication
import com.venkatesh.schoolmanagement.adapter.StudentsAdapter
import com.venkatesh.schoolmanagement.model.Profiles
import com.venkatesh.schoolmanagement.model.UserInfo
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_events.*
import org.json.JSONException
import org.json.JSONObject

class TeachersActivity : AppCompatActivity() {

    var list = arrayListOf<Profiles>()
    lateinit var adapter: StudentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        title = getString(R.string.teacher)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeRecyclerView()
      //  getTeachersFromFirebase()
        getAllTeachers()
    }

    private fun initializeRecyclerView() {
        recyclerViewEvents.layoutManager = LinearLayoutManager(this)
        adapter = StudentsAdapter(this@TeachersActivity, list)
        recyclerViewEvents.adapter = adapter
    }

/*
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
*/

    private fun getAllTeachers() {
        val url = "https://school-management-system-27ab4.firebaseio.com/other_user_details.json"
        val jsonObjReq = JsonObjectRequest(
            Request.Method.GET,
            url, null,
            Response.Listener<JSONObject> {
                val iter = it.keys()
                val list = arrayListOf<Profiles>()
                while (iter.hasNext()) {
                    val profile = Profiles()
                    val key = iter.next()
                    try {
                        profile.userId = key
                        val userInfo = UserInfo()
                        val value = it.getJSONObject(key)
                        userInfo.name = value.getString("userName")
                        userInfo.phoneNumber = value.getString("phoneNumber")
                        userInfo.role = value.getString("role")
                        userInfo.url = value.getString("url")
                        userInfo.gender = value.getString("gender")
                        userInfo.className = value.getString("className")
                        profile.userInfo = userInfo
                        list.add(profile)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                val finalList = arrayListOf<Profiles>()
                list.forEach {
                    if (it.userInfo?.role == Constants.teacher)
                        finalList.add(it)
                }
                adapter.updateData(finalList)
            }, Response.ErrorListener {
                VolleyLog.d("Error", "Error: " + it.message)
            })

        // Adding request to request queue
        SMSApplication.smsApplication.addToRequestQueue(jsonObjReq, "AllStudents")
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
               // getTeachersFromFirebase()
                getAllTeachers()
            }
        }

    }
}
