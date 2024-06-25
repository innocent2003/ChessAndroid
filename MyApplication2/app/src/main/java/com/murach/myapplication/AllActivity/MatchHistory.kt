package com.murach.myapplication.AllActivity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.murach.myapplication.R

class MatchHistory : AppCompatActivity() {
    private lateinit var iconBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_history)

        iconBack = findViewById(R.id.IconBack)
        iconBack.setOnClickListener {
            startActivity(Intent(this, MainMenu::class.java))
        }
    }
}
