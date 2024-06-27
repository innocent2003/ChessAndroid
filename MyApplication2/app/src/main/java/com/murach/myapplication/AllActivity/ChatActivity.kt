package com.murach.myapplication.AllActivity

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.murach.myapplication.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ChatActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: ArrayAdapter<String>
    private val messages = mutableListOf<String>()

    private var socket: Socket? = null
    private var out: PrintWriter? = null
    private var `in`: BufferedReader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        listView = findViewById(R.id.chatListView)
        messageEditText = findViewById(R.id.messageEditText)
        sendButton = findViewById(R.id.sendButton)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messages)
        listView.adapter = adapter

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitNetwork().build())

        startChatThread()
    }

    private fun startChatThread() {
        Thread {
            try {
                socket = Socket("YOUR_SERVER_IP", 12345) // Replace YOUR_SERVER_IP with the actual IP address
                out = PrintWriter(socket!!.getOutputStream(), true)
                `in` = BufferedReader(InputStreamReader(socket!!.getInputStream()))

                runOnUiThread {
                    sendButton.setOnClickListener {
                        val message = messageEditText.text.toString()
                        if (message.isNotBlank()) {
                            sendMessage(message)
                            messageEditText.text.clear()
                        }
                    }
                }

                var message: String?
                while (`in`!!.readLine().also { message = it } != null) {
                    runOnUiThread {
                        messages.add(message!!)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Log.e("ChatActivity", "Error", e)
                runOnUiThread {
                    Toast.makeText(this, "Error connecting to server", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun sendMessage(message: String) {
        if (out != null) {
            Thread {
                try {
                    out!!.println(message)
                } catch (e: Exception) {
                    Log.e("ChatActivity", "Error sending message", e)
                }
            }.start()
        } else {
            Log.e("ChatActivity", "Output stream is not initialized")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            socket?.close()
        } catch (e: Exception) {
            Log.e("ChatActivity", "Error closing socket", e)
        }
    }
}
