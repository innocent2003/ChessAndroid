package com.murach.myapplication.WithOffline

interface ChessDelegate {
    fun pieceAt(square: Square) : ChessPiece?
    fun movePiece(from: Square, to: Square)

}