package com.goldenthumb.android.chess

import kotlin.math.abs

object ChessGame {
    private var piecesBox = mutableSetOf<ChessPiece>()
    private var hasKingMoved = mutableMapOf<Player, Boolean>(Player.WHITE to false, Player.BLACK to false)
    private var hasRookMoved = mutableMapOf<Square, Boolean>()
    private var lastMove: Pair<Square, Square>? = null



    init {
        hasRookMoved[Square(0, 0)] = false
        hasRookMoved[Square(7, 0)] = false
        hasRookMoved[Square(0, 7)] = false
        hasRookMoved[Square(7, 7)] = false
        reset()
    }

    fun clear() {
        piecesBox.clear()
    }

    fun addPiece(piece: ChessPiece) {
        piecesBox.add(piece)
    }

    private fun canKnightMove(from: Square, to: Square): Boolean {
        return abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1 ||
                abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2
    }

    private fun canRookMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && isClearVerticallyBetween(from, to) ||
            from.row == to.row && isClearHorizontallyBetween(from, to)) {
            return true
        }
        return false
    }

    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(Square(from.col, nextRow)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0 ) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(Square(nextCol, from.row)) != null) {
                return false
            }
        }
        return true
    }

    private fun isClearDiagonally(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.col - to.col) - 1
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(nextCol, nextRow) != null) {
                return false
            }
        }
        return true
    }

    private fun canBishopMove(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) == abs(from.row - to.row)) {
            return isClearDiagonally(from, to)
        }
        return false
    }

    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canRookMove(from, to) || canBishopMove(from, to)
    }

    private fun canKingMove(from: Square, to: Square): Boolean {
//        if (canQueenMove(from, to)) {
//            val deltaCol = abs(from.col - to.col)
//            val deltaRow = abs(from.row - to.row)
//            return deltaCol == 1 && deltaRow == 1 || deltaCol + deltaRow == 1
//        }
        val deltaCol = abs(from.col - to.col)
        val deltaRow = abs(from.row - to.row)
        if (deltaCol <= 1 && deltaRow <= 1) {
            return true
        }
        // Castling
        val movingPiece = pieceAt(from) ?: return false
        if (!hasKingMoved[movingPiece.player]!! && from.row == to.row && deltaCol == 2) {
            val rookCol = if (to.col == 6) 7 else 0
            val rookSquare = Square(rookCol, from.row)
            val rookPiece = pieceAt(rookSquare) ?: return false
            if (rookPiece.chessman == Chessman.ROOK && !hasRookMoved[rookSquare]!!) {
                val betweenSquares = if (to.col == 6) listOf(5, 6) else listOf(1, 2, 3)
                if (betweenSquares.all { pieceAt(Square(it, from.row)) == null }) {
                    return true
                }
            }
        }
        return false
    }

    private fun canPawnMove(from: Square, to: Square): Boolean {
        val movingPiece = pieceAt(from) ?: return false
        val direction = if (movingPiece.player == Player.WHITE) 1 else -1

        // Standard move
        if (from.col == to.col) {
            if (to.row == from.row + direction) {
                return pieceAt(to) == null // One square forward
            } else if ((from.row == 1 && direction == 1 || from.row == 6 && direction == -1) &&
                to.row == from.row + 2 * direction) {
                return pieceAt(Square(from.col, from.row + direction)) == null &&
                        pieceAt(to) == null // Two squares forward from initial position
            }
        }
        // Capture
        if (abs(from.col - to.col) == 1 && to.row == from.row + direction) {
            return pieceAt(to)?.player != movingPiece.player // Diagonal capture
        }
        // En Passant
        if (abs(from.col - to.col) == 1 && to.row == from.row + direction) {
            val last = lastMove ?: return false
            val lastFrom = last.first
            val lastTo = last.second
            if (pieceAt(lastTo)?.chessman == Chessman.PAWN &&
                abs(lastFrom.row - lastTo.row) == 2 &&
                lastTo.col == to.col && lastTo.row == from.row) {
                return true
            }
        }
        if (movingPiece?.chessman == Chessman.PAWN) {
            if (to.row == 0 || to.row == 7) {
                // Prompt for pawn promotion
                // Implement logic to show a dialog or prompt user for promotion choice
                // Example: showPawnPromotionDialog(from, to)
                // Once user selects a promotion piece, call promotePawn(to, player, chosenPiece)
                // where chosenPiece is the Chessman chosen by the user (Queen, Rook, Bishop, Knight)
                // In this example, let's assume the user always chooses Queen
                promotePawn(to, movingPiece.player, Chessman.QUEEN)
            }
        }

        lastMove = Pair(from, to)
        return false
    }

    private fun promotePawn(square: Square, player: Player, chosenPiece: Chessman) {
        piecesBox.remove(pieceAt(square))
        addPiece(ChessPiece(square.col, square.row, player, chosenPiece, getDrawableForChessman(chosenPiece, player)))
    }
    private fun getDrawableForChessman(chessman: Chessman, player: Player): Int {
        return when (chessman) {
            Chessman.KING -> if (player == Player.WHITE) R.drawable.king_white else R.drawable.king_black
            Chessman.QUEEN -> if (player == Player.WHITE) R.drawable.queen_white else R.drawable.queen_black
            Chessman.BISHOP -> if (player == Player.WHITE) R.drawable.bishop_white else R.drawable.bishop_black
            Chessman.ROOK -> if (player == Player.WHITE) R.drawable.rook_white else R.drawable.rook_black
            Chessman.KNIGHT -> if (player == Player.WHITE) R.drawable.knight_white else R.drawable.knight_black
            Chessman.PAWN -> if (player == Player.WHITE) R.drawable.pawn_white else R.drawable.pawn_black
        }
    }

    fun canMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && from.row == to.row) {
            return  false
        }
        val movingPiece = pieceAt(from) ?: return false
        return when(movingPiece.chessman) {
            Chessman.KNIGHT -> canKnightMove(from, to)
            Chessman.ROOK -> canRookMove(from, to)
            Chessman.BISHOP -> canBishopMove(from, to)
            Chessman.QUEEN -> canQueenMove(from, to)
            Chessman.KING -> canKingMove(from, to)
            Chessman.PAWN -> canPawnMove(from, to)
        }
    }

    fun movePiece(from: Square, to: Square) {
//        if (canMove(from, to)) {
//            movePiece(from.col, from.row, to.col, to.row)
//        }
        if (canMove(from, to)) {
            val movingPiece = pieceAt(from)
            if (movingPiece?.chessman == Chessman.KING) {
                hasKingMoved[movingPiece.player] = true
            } else if (movingPiece?.chessman == Chessman.ROOK) {
                hasRookMoved[from] = true
            }

            // Castling move rook
            if (movingPiece?.chessman == Chessman.KING && abs(from.col - to.col) == 2) {
                val rookFrom = if (to.col == 6) Square(7, from.row) else Square(0, from.row)
                val rookTo = if (to.col == 6) Square(5, from.row) else Square(3, from.row)
                movePiece(rookFrom.col, rookFrom.row, rookTo.col, rookTo.row)
            }

            movePiece(from.col, from.row, to.col, to.row)
            lastMove = Pair(from, to)
        }
    }

    private fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            piecesBox.remove(it)
        }

        piecesBox.remove(movingPiece)
        addPiece(movingPiece.copy(col = toCol, row = toRow))
    }

    fun reset() {
        clear()
        for (i in 0 until 2) {
            addPiece(ChessPiece(0 + i * 7, 0, Player.WHITE, Chessman.ROOK, R.drawable.rook_white))
            addPiece(ChessPiece(0 + i * 7, 7, Player.BLACK, Chessman.ROOK, R.drawable.rook_black))

            addPiece(ChessPiece(1 + i * 5, 0, Player.WHITE, Chessman.KNIGHT, R.drawable.knight_white))
            addPiece(ChessPiece(1 + i * 5, 7, Player.BLACK, Chessman.KNIGHT, R.drawable.knight_black))

            addPiece(ChessPiece(2 + i * 3, 0, Player.WHITE, Chessman.BISHOP, R.drawable.bishop_white))
            addPiece(ChessPiece(2 + i * 3, 7, Player.BLACK, Chessman.BISHOP, R.drawable.bishop_black))
        }

        for (i in 0 until 8) {
            addPiece(ChessPiece(i, 1, Player.WHITE, Chessman.PAWN, R.drawable.pawn_white))
            addPiece(ChessPiece(i, 6, Player.BLACK, Chessman.PAWN, R.drawable.pawn_black))
        }

        addPiece(ChessPiece(3, 0, Player.WHITE, Chessman.QUEEN, R.drawable.queen_white))
        addPiece(ChessPiece(3, 7, Player.BLACK, Chessman.QUEEN, R.drawable.queen_black))
        addPiece(ChessPiece(4, 0, Player.WHITE, Chessman.KING, R.drawable.king_white))
        addPiece(ChessPiece(4, 7, Player.BLACK, Chessman.KING, R.drawable.king_black))

    }

    fun pieceAt(square: Square): ChessPiece? {
        return pieceAt(square.col, square.row)
    }

    private fun pieceAt(col: Int, row: Int): ChessPiece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return  piece
            }
        }
        return null
    }

    fun pgnBoard(): String {
        var desc = " \n"
        desc += "  a b c d e f g h\n"
        for (row in 7 downTo 0) {
            desc += "${row + 1}"
            desc += boardRow(row)
            desc += " ${row + 1}"
            desc += "\n"
        }
        desc += "  a b c d e f g h"

        return desc
    }

    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            desc += boardRow(row)
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"

        return desc
    }

    private fun boardRow(row: Int) : String {
        var desc = ""
        for (col in 0 until 8) {
            desc += " "
            desc += pieceAt(col, row)?.let {
                val white = it.player == Player.WHITE
                when (it.chessman) {
                    Chessman.KING -> if (white) "k" else "K"
                    Chessman.QUEEN -> if (white) "q" else "Q"
                    Chessman.BISHOP -> if (white) "b" else "B"
                    Chessman.ROOK -> if (white) "r" else "R"
                    Chessman.KNIGHT -> if (white) "n" else "N"
                    Chessman.PAWN -> if (white) "p" else "P"
                }
            } ?: "."
        }
        return desc
    }
}

