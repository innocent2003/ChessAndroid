package com.murach.myapplication.WithOffline

import android.content.Context
import android.content.Intent
import android.icu.text.Transliterator
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.murach.myapplication.AllActivity.ChessSettings
import com.murach.myapplication.R


import com.murach.myapplication.enums.Chessman
import com.murach.myapplication.enums.Player

import kotlin.math.abs

class WhitePromotion : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.white_promote)
    }
}