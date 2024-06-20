package com.murach.myapplication.WithOffline

import com.murach.myapplication.enums.Chessman
import com.murach.myapplication.enums.Player

data class ChessPiece(
    var player: Player,
    val chessman: Chessman,
    val resID: Int,
)