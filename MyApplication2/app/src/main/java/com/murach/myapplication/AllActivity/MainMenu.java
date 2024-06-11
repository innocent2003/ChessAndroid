package com.murach.myapplication.AllActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.murach.myapplication.WithOffline.MainActivity;
import com.murach.myapplication.R;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    Button play_offline_button, play_online_button, match_history_button, settings_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        getButton();
        play_offline_button.setOnClickListener(this);
        match_history_button.setOnClickListener(this);
        settings_button.setOnClickListener(this);
    }

    private void getButton() {
        play_offline_button = findViewById(R.id.play_offline_button);
        match_history_button = findViewById(R.id.match_history_button);
        settings_button = findViewById(R.id.settings_button);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.play_offline_button) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


        if (id == R.id.match_history_button) {
            Intent intent = new Intent(this, MatchHistory.class);
            startActivity(intent);
        }

        if (id == R.id.settings_button) {
            Intent intent = new Intent(this, ChessSettings.class);
            startActivity(intent);
        }
    }
}