package com.murach.myapplication.WithComputer

interface BotDelegate {
    fun pieceAt(square: Square) : BotPiece?
    fun movePiece(from: Square, to: Square)
}