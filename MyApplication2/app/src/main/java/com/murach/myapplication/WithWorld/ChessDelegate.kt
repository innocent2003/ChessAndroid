package com.murach.myapplication.WithWorld



interface ChessDelegate {
    fun pieceAt(square: Square) : ChessPiece?
    fun movePiece(from: Square, to: Square)

}