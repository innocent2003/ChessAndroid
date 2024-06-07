package com.murach.myapplication.AllActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.murach.myapplication.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
    }
}