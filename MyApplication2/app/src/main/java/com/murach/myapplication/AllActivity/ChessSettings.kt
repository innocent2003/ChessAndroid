package com.murach.myapplication.AllActivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.murach.myapplication.R
import java.util.Locale

class ChessSettings : AppCompatActivity() {
    private lateinit var swMusic: Switch
    private lateinit var swSound: Switch
    private lateinit var englishButton: Button
    private lateinit var vietnameseButton: Button
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager

    private var soundEffectsEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // EdgeToEdge.enable(this) // Ensure this is correctly implemented if it's custom
        setContentView(R.layout.chess_settings)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0)

        swMusic = findViewById(R.id.SwMusic)
        mediaPlayer = MediaPlayer.create(this, R.raw.mymusic).apply {
            isLooping = true
        }

        swMusic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) mediaPlayer.start() else {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
            }
        }

        swSound = findViewById(R.id.SwSound)
        swSound.setOnCheckedChangeListener { _, isChecked ->
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
    }

    private fun setLanguage(language: String) {
        val resources: Resources = resources
        val configuration: Configuration = resources.configuration
        val locale = Locale(language)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}
