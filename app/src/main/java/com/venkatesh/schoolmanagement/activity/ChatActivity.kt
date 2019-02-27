package com.venkatesh.schoolmanagement.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.venkatesh.schoolmanagement.ApiClient
import com.venkatesh.schoolmanagement.R
import com.venkatesh.schoolmanagement.RetrofitCallback
import com.venkatesh.schoolmanagement.adapter.ChatAdapter
import com.venkatesh.schoolmanagement.model.ChatMessage
import com.venkatesh.schoolmanagement.utilities.Commons
import com.venkatesh.schoolmanagement.utilities.Constants
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    lateinit var adapter: ChatAdapter
    var list: ArrayList<ChatMessage> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        title=getString(R.string.groupchat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeRecyclerView()
        loadPrevChatHistory()
    }

    private fun initializeRecyclerView() {
        list_of_messages.layoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(arrayListOf())
        list_of_messages.adapter = adapter
    }

    private fun loadPrevChatHistory() {
        ApiClient.getChatMessages(this, object : RetrofitCallback() {
            override fun onResponse(any: Any) {
                list.clear()
                list = any as ArrayList<ChatMessage>
                adapter.updateData(list)
                list_of_messages.scrollToPosition(list.size)
                list_of_messages.smoothScrollToPosition(list.size)
            }
        })

        /*val jsonObjReq = StringRequest(
            Request.Method.GET,
            "https://school-management-system-27ab4.firebaseio.com/chat%20messages.json",
            object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    try {
                        val obj = JSONObject(response)
                        val i = obj.keys()
                        var key = ""

                        while (i.hasNext()) {
                            key = i.next().toString()

                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                }
            }, object : Response.ErrorListener {

                override fun onErrorResponse(error: VolleyError) {
                    VolleyLog.d("Error", " " + error.message)
                }
            })
        // Adding request to request queue
        (application as SMSApplication).getInstance()?.addToRequestQueue(jsonObjReq, Constants.chats);*/
    }


    override fun onStart() {
        super.onStart()
        fabSendChat.setOnClickListener {
            val chatMessage=ChatMessage(etChat.text.toString(),
                Constants?.userProfile?.userName!!,
                Commons.getCurrentDateTime()!!,
                Constants.userProfile?.userId!!
            )
            list.add(chatMessage)
            FirebaseDatabase.getInstance().getReference(Constants.chats).setValue(list)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        etChat.setText("")
                        loadPrevChatHistory()
                        Toast.makeText(applicationContext, getString(R.string.sent), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.failToSend), Toast.LENGTH_SHORT).show()
                    }
                }
        }

/*
        FirebaseDatabase.getInstance().getReference(Constants.chats)
            .addChildEventListener(object : ValueEventListener, ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    list.add(p0.getValue(ChatMessage::class.java)!!)
                    adapter.notifyDataSetChanged()

                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    list.remove(p0.getValue(ChatMessage::class.java))
                    adapter.notifyDataSetChanged()

                }
            })
*/
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}
