package com.murach.myapplication.WithWorld

import com.murach.myapplication.WithOffline.ChessPiece
import com.murach.myapplication.WithOffline.Square

interface ChessDelegate {
    fun pieceAt(square: Square) : ChessPiece?
    fun movePiece(from: Square, to: Square)

}