package com.murach.myapplication

import com.murach.myapplication.enums.Chessman
import com.murach.myapplication.enums.Player

data class ChessPiece(
    val player: Player,
    val chessman: Chessman,
    val resID: Int,
)