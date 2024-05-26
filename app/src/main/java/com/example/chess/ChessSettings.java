package com.example.chess;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.content.Context;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.media.SoundPool;
import android.media.AudioManager;

import java.util.Locale;

public class ChessSettings extends AppCompatActivity {
    Switch SwMusic,SwSound;
    SoundPool soundPool;
    int soundId;
    boolean soundEffectsEnabled = true;
    MediaPlayer mediaPlayer;

    Button EnglishButton,VietnameseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.chess_settings);


        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);


        SwMusic = findViewById(R.id.SwMusic);
        mediaPlayer = MediaPlayer.create(this, R.raw.mymusic);
        mediaPlayer.setLooping(true);


         SwMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if(isChecked) mediaPlayer.start();
                 else {
                     mediaPlayer.pause();
                     mediaPlayer.seekTo(0);
                 }
             }
         });

        SwSound = findViewById(R.id.SwSound);

//        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
//        soundId = soundPool.load(this, R.raw.soundeffect, 1);
//        soundPool.play(soundId, 1, 1, 0, 0, 1);
        SwSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
                else audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
            }
        });

    EnglishButton = findViewById(R.id.EnglishButton);
    VietnameseButton = findViewById(R.id.VietnameseButton);

    EnglishButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             setLanguage("en");
            startActivity(new Intent(ChessSettings.this,ChessSettings.class));
        }
    });

    VietnameseButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setLanguage("vn");
            startActivity(new Intent(ChessSettings.this,ChessSettings.class));
        }
    });
    }
    private void setLanguage(String lg){
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(lg);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}
