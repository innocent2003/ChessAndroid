package com.murach.myapplication

import com.murach.myapplication.enums.Chessman

data class ChessPiece(
    val player: Player,
    val chessman: Chessman,
    val resID: Int,
)