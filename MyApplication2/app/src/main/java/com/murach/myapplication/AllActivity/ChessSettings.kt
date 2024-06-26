package com.murach.myapplication.AllActivity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle


import android.widget.Button

import android.widget.ImageView
import android.widget.Switch
import android.content.Context


import androidx.appcompat.app.AppCompatActivity


import android.media.SoundPool
import android.media.AudioManager


import com.murach.myapplication.R

import java.util.Locale

class ChessSettings : AppCompatActivity() {
    private lateinit var swMusic: Switch
    private lateinit var swSound: Switch
    private lateinit var soundPool: SoundPool
    private var soundId: Int = 0
    private var soundEffectsEnabled: Boolean = true
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var englishButton: Button
    private lateinit var vietnameseButton: Button
    private lateinit var iconBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chess_settings)

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0)

        swMusic = findViewById(R.id.SwMusic)
        mediaPlayer = MediaPlayer.create(this, R.raw.mymusic).apply {
            isLooping = true
        }

        swMusic.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) mediaPlayer.start()
            else {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
            }
        }

        swSound = findViewById(R.id.SwSound)

        swSound.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0)
            } else {
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0)
            }
        }

        englishButton = findViewById(R.id.EnglishButton)
        vietnameseButton = findViewById(R.id.VietnameseButton)

        englishButton.setOnClickListener {
            setLanguage("en")
            startActivity(Intent(this, ChessSettings::class.java))
        }

        vietnameseButton.setOnClickListener {
            setLanguage("vn")
            startActivity(Intent(this, ChessSettings::class.java))
        }

        iconBack = findViewById(R.id.IconBack)
        iconBack.setOnClickListener {
            startActivity(Intent(this, MainMenu::class.java))
        }
    }

    private fun setLanguage(lg: String) {
        val resources = resources
        val configuration = resources.configuration
        val locale = Locale(lg)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}