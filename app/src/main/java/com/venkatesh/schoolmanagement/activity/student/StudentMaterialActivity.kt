package com.venkatesh.schoolmanagement.activity.student

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.activity.admin.MaterialFragment
import com.venkatesh.schoolmanagement.utilities.Constants

class StudentMaterialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_material)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.materials)
    }

    override fun onStart() {
        super.onStart()
        val className = Constants.userProfile?.className
        title = getString(R.string.materials) + "( " + className + " )"
        supportFragmentManager?.beginTransaction()?.add(R.id.frameLayout, MaterialFragment.getInstance(className!!))?.commit()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return true
    }
}
