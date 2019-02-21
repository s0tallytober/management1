package com.venkatesh.schoolmanagement.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.adapter.AdminDashboardAdapter
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.DialogCallback
import kotlinx.android.synthetic.main.activity_admin_dashboard.*

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        title = getString(R.string.admin_dashboard)

        creatingActionsInGridView()
    }

    private fun creatingActionsInGridView() {
        val actionsList = arrayListOf("Add Student", "Add Teacher", "Add Course", "Add Event")
        val adapter = AdminDashboardAdapter(actionsList)
        recyclerAdminDashboard.layoutManager = GridLayoutManager(this@AdminDashboardActivity, 2)
        recyclerAdminDashboard.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout -> {
                Commons.showAlertDialog(
                    context = this@AdminDashboardActivity,
                    nofOptions = 2,
                    positiveButtonText = getString(R.string.yes),
                    message = getString(R.string.msgLogout),
                    dialogCallback = object : DialogCallback() {
                        override fun positiveClick() {
                            val intent = Intent(this@AdminDashboardActivity, SelectLoginTypeActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            finishAffinity()
                            startActivity(intent)
                        }
                    }
                )
            }
        }
        return true
    }
}
