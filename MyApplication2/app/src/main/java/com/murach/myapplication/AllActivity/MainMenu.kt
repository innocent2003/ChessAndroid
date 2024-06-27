package com.murach.myapplication.AllActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.murach.myapplication.R
import com.murach.myapplication.WithComputer.BotActivity

import com.murach.myapplication.WithOffline.MainActivity
import com.murach.myapplication.WithWorld.WorldGamePlayActivity

class MainMenu : AppCompatActivity(), View.OnClickListener {
    private lateinit var playOfflineButton: Button
    private lateinit var playOnlineButton: Button
    private lateinit var matchHistoryButton: Button
    private lateinit var settingsButton: Button
    private lateinit var playComputerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
        getButton()
        playOfflineButton.setOnClickListener(this)
        matchHistoryButton.setOnClickListener(this)
        settingsButton.setOnClickListener(this)
        playOnlineButton.setOnClickListener(this)
        playComputerButton.setOnClickListener(this)
    }

    private fun getButton() {
        playOfflineButton = findViewById(R.id.play_offline_button)
        matchHistoryButton = findViewById(R.id.match_history_button)
        settingsButton = findViewById(R.id.settings_button)
        playOnlineButton = findViewById(R.id.play_online_button)
        playComputerButton = findViewById(R.id.play_computer_button)
    }

    override fun onClick(view: View) {
        val id = view.id
        when (id) {
            R.id.play_offline_button -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.match_history_button -> {
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }
            R.id.settings_button -> {
                val intent = Intent(this, ChessSettings::class.java)
                startActivity(intent)
            }
            R.id.play_online_button -> {
                val intent = Intent(this, WorldGamePlayActivity::class.java)
                startActivity(intent)
            }
            R.id.play_computer_button -> {
                val intent = Intent(this, BotActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
