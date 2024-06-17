package com.murach.myapplication.WithWorld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.murach.myapplication.R
import com.murach.myapplication.WithWorld.ChessDelegate
import com.murach.myapplication.WithOffline.ChessPiece

import com.murach.myapplication.WithOffline.Square

class WorldGamePlayActivity : AppCompatActivity() , ChessDelegate{
    private lateinit var chessView: ChessView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_world_game_play)
        chessView = findViewById(R.id.chess_view)

    }

    override fun pieceAt(square: Square): ChessPiece? {
        TODO("Not yet implemented")
    }

    override fun movePiece(from: Square, to: Square) {
        TODO("Not yet implemented")
    }


}