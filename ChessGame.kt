package com.goldenthumb.android.chess

import kotlin.math.abs

object ChessGame {
    private var piecesBox: MutableMap<Square, ChessPiece>
    var turn = Player.WHITE

    init {
        piecesBox = mutableMapOf()
        reset()
    }

    fun reset() {
        piecesBox.clear()
        turn = Player.WHITE

        // Setup initial pieces configuration
        // Add pawns
        for (i in 0 until 8) {
            piecesBox[Square(i, 1)] = ChessPiece(Player.WHITE, Chessman.PAWN, R.drawable.pawn_white)
            piecesBox[Square(i, 6)] = ChessPiece(Player.BLACK, Chessman.PAWN, R.drawable.pawn_black)
        }

        // Add rooks
        piecesBox[Square(0, 0)] = ChessPiece(Player.WHITE, Chessman.ROOK, R.drawable.rook_white)
        piecesBox[Square(7, 0)] = ChessPiece(Player.WHITE, Chessman.ROOK, R.drawable.rook_white)
        piecesBox[Square(0, 7)] = ChessPiece(Player.BLACK, Chessman.ROOK, R.drawable.rook_black)
        piecesBox[Square(7, 7)] = ChessPiece(Player.BLACK, Chessman.ROOK, R.drawable.rook_black)

        // Add knights
        piecesBox[Square(1, 0)] = ChessPiece(Player.WHITE, Chessman.KNIGHT, R.drawable.knight_white)
        piecesBox[Square(6, 0)] = ChessPiece(Player.WHITE, Chessman.KNIGHT, R.drawable.knight_white)
        piecesBox[Square(1, 7)] = ChessPiece(Player.BLACK, Chessman.KNIGHT, R.drawable.knight_black)
        piecesBox[Square(6, 7)] = ChessPiece(Player.BLACK, Chessman.KNIGHT, R.drawable.knight_black)

        // Add bishops
        piecesBox[Square(2, 0)] = ChessPiece(Player.WHITE, Chessman.BISHOP, R.drawable.bishop_white)
        piecesBox[Square(5, 0)] = ChessPiece(Player.WHITE, Chessman.BISHOP, R.drawable.bishop_white)
        piecesBox[Square(2, 7)] = ChessPiece(Player.BLACK, Chessman.BISHOP, R.drawable.bishop_black)
        piecesBox[Square(5, 7)] = ChessPiece(Player.BLACK, Chessman.BISHOP, R.drawable.bishop_black)

        // Add queens
        piecesBox[Square(3, 0)] = ChessPiece(Player.WHITE, Chessman.QUEEN, R.drawable.queen_white)
        piecesBox[Square(3, 7)] = ChessPiece(Player.BLACK, Chessman.QUEEN, R.drawable.queen_black)

        // Add kings
        piecesBox[Square(4, 0)] = ChessPiece(Player.WHITE, Chessman.KING, R.drawable.king_white)
        piecesBox[Square(4, 7)] = ChessPiece(Player.BLACK, Chessman.KING, R.drawable.king_black)
    }

    fun pieceAt(square: Square): ChessPiece? = piecesBox[square]

    fun movePiece(from: Square, to: Square) {
        val movingPiece = pieceAt(from) ?: return
        if (movingPiece.player == turn && canMove(from, to)) {
            piecesBox[to] = movingPiece
            piecesBox.remove(from)
            turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
        }
    }

    private fun canMove(from: Square, to: Square): Boolean {
        if (from == to) return false
        pieceAt(from)?.let { movingPiece ->
            // check if the move is valid for the piece
            val deltaCol = to.col - from.col
            val deltaRow = to.row - from.row
            val pieceAtDestination = pieceAt(to)
            if (pieceAtDestination?.player == movingPiece.player) return false

            when (movingPiece.chessman) {
                Chessman.PAWN -> {
                    val direction = if (movingPiece.player == Player.WHITE) 1 else -1
                    if (deltaCol == 0 && deltaRow == direction && pieceAtDestination == null) {
                        return true
                    }
                    if (deltaCol == 0 && deltaRow == 2 * direction && pieceAtDestination == null && (from.row == 1 || from.row == 6)) {
                        return true
                    }
                    if (abs(deltaCol) == 1 && deltaRow == direction && pieceAtDestination != null) {
                        return true
                    }
                }
                Chessman.ROOK -> {
                    if (deltaCol == 0 || deltaRow == 0) {
                        if (isPathClear(from, to)) return true
                    }
                }
                Chessman.KNIGHT -> {
                    if (abs(deltaCol * deltaRow) == 2) return true
                }
                Chessman.BISHOP -> {
                    if (abs(deltaCol) == abs(deltaRow)) {
                        if (isPathClear(from, to)) return true
                    }
                }
                Chessman.QUEEN -> {
                    if (deltaCol == 0 || deltaRow == 0 || abs(deltaCol) == abs(deltaRow)) {
                        if (isPathClear(from, to)) return true
                    }
                }
                Chessman.KING -> {
                    if (abs(deltaCol) <= 1 && abs(deltaRow) <= 1) return true
                }
            }
        }
        return false
    }


    private fun isPathClear(from: Square, to: Square): Boolean {
        val deltaCol = to.col - from.col
        val deltaRow = to.row - from.row
        val stepCol = Integer.signum(deltaCol)
        val stepRow = Integer.signum(deltaRow)
        var currentCol = from.col + stepCol
        var currentRow = from.row + stepRow

        while (currentCol != to.col || currentRow != to.row) {
            if (pieceAt(Square(currentCol, currentRow)) != null) return false
            currentCol += stepCol
            currentRow += stepRow
        }
        return true
    }

    fun isCheck(player: Player): Boolean {
        val kingPosition = findKing(player) ?: return false
        return piecesBox.any { (square, piece) ->
            piece.player != player && canMove(square, kingPosition)
        }
    }

    fun isCheckmate(player: Player): Boolean {
        if (!isCheck(player)) return false
        return piecesBox.filter { it.value.player == player }.all { (from, _) ->
            getAllPossibleMoves(from).all { to ->
                !wouldBeInCheckAfterMove(player, from, to)
            }
        }
    }

    fun isStalemate(player: Player): Boolean {
        if (isCheck(player)) return false
        return piecesBox.filter { it.value.player == player }.all { (from, _) ->
            getAllPossibleMoves(from).all { to ->
                wouldBeInCheckAfterMove(player, from, to)
            }
        }
    }

    private fun getAllPossibleMoves(from: Square): List<Square> {
        return (0 until 8).flatMap { col ->
            (0 until 8).mapNotNull { row ->
                val to = Square(col, row)
                if (canMove(from, to)) to else null
            }
        }
    }

    private fun wouldBeInCheckAfterMove(player: Player, from: Square, to: Square): Boolean {
        val pieceAtDestination = piecesBox[to]
        piecesBox[to] = piecesBox[from]!!
        piecesBox.remove(from)

        val inCheck = isCheck(player)

        piecesBox[from] = piecesBox[to]!!
        if (pieceAtDestination != null) {
            piecesBox[to] = pieceAtDestination
        } else {
            piecesBox.remove(to)
        }

        return inCheck
    }

    private fun findKing(player: Player): Square? {
        return piecesBox.entries.find { it.value.player == player && it.value.chessman == Chessman.KING }?.key
    }
}

