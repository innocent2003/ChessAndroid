package com.murach.myapplication.WithWorld

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.murach.myapplication.R

import com.murach.myapplication.enums.Chessman
import com.murach.myapplication.enums.Player
import java.io.PrintWriter
import java.net.ServerSocket


class WorldGamePlayActivity : AppCompatActivity() ,
   ChessDelegate {

    private lateinit var chessView: ChessView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_game_play)

        chessView = findViewById(R.id.chess_view)
        chessView.chessDelegate = this
}

    override fun pieceAt(square: Square): ChessPiece? {
        TODO("Not yet implemented")
    }

    override fun movePiece(from: Square, to: Square) {
        TODO("Not yet implemented")
    }
}