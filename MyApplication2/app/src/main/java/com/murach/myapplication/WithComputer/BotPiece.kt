package com.murach.myapplication.WithComputer

import com.murach.myapplication.enums.Chessman
import com.murach.myapplication.enums.Player

data class BotPiece (
    val player : Player,
    val chessman: Chessman,
    val resID: Int,
)