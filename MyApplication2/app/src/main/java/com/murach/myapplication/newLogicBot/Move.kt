package com.murach.myapplication.newLogicBot

data class Move(
    val startRow: Int,
    val startCol: Int,
    val endRow: Int,
    val endCol: Int,
    val pieceMoved: Char,
    val capturedPiece: Char
)
