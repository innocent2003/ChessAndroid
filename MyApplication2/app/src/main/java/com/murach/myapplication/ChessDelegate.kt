package com.murach.myapplication

interface ChessDelegate {
    fun pieceAt(square: Square) : ChessPiece?
    fun movePiece(from: Square, to: Square)
    fun onPawnPromotion(square: Square)
}