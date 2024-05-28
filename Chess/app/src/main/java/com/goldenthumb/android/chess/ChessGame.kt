package com.goldenthumb.android.chess

import kotlin.math.abs

object ChessGame {
    private var piecesBox: MutableMap<Square, ChessPiece>
    var turn = Player.WHITE
    private val hasMoved: MutableSet<Square>
    private var hasKingMoved = mutableMapOf<Player, Boolean>(Player.WHITE to false, Player.BLACK to false)
    private var hasRookMoved = mutableMapOf<Square, Boolean>()

    init {
        hasRookMoved[Square(0, 0)] = false
        hasRookMoved[Square(7, 0)] = false
        hasRookMoved[Square(0, 7)] = false
        hasRookMoved[Square(7, 7)] = false
        piecesBox = mutableMapOf()
        hasMoved = mutableSetOf()
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
        if (movingPiece.chessman == Chessman.KING && canCastle(from, to)) {
            performCastling(from, to)
        }
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
                    return from.col == to.col && isClearVerticallyBetween(from, to) ||
                            from.row == to.row && isClearHorizontallyBetween(from, to)

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
                    if (abs(deltaCol) <= 1 && abs(deltaRow) <= 1) return true

                }
            }
        }
        return false
    }
    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val range = if (from.row < to.row) (from.row + 1 until to.row) else (to.row + 1 until from.row)
        return range.none { pieceAt(Square(from.col, it)) != null }
    }

    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val range = if (from.col < to.col) (from.col + 1 until to.col) else (to.col + 1 until from.col)
        return range.none { pieceAt(Square(it, from.row)) != null }
    }
//
//    private fun canCastle(from: Square, to: Square): Boolean {
//        val king = pieceAt(from)
//        val rook = pieceAt(to)
//
//        // Check if the king and rook are in their initial positions and haven't moved
//        val kingInitialPosition = if (king?.player == Player.WHITE) Square(4, 0) else Square(4, 7)
//        val rookInitialPosition = if (king?.player == Player.WHITE) Square(0, 0) else Square(0, 7)
//
//        if (from != kingInitialPosition || to != rookInitialPosition) return false
//        if (king == null || rook == null) return false
//
//        // Check if the squares between the king and rook are empty
//        val direction = if (to.col > from.col) 1 else -1
//        for (col in from.col + direction until to.col) {
//            if (pieceAt(Square(col, from.row)) != null) return false
//        }
//
//        // Check if the squares that the king moves through or ends up on are not under attack
//        val newKingSquare = Square(from.col + 2 * direction, from.row)
//        return !isCheck(king.player) && !isCheckAfterMove(king.player, from, newKingSquare)
//    }
//
//    private fun performCastling(from: Square, to: Square) {
//        val king = pieceAt(from)
//        val rook = pieceAt(to)
//
//        // Determine the direction of castling (left or right)
//        val direction = if (to.col > from.col) 1 else -1
//
//        // Update the positions of the king and rook
//        val newKingSquare = Square(from.col + 2 * direction, from.row)
//        val newRookSquare = Square(to.col + direction, from.row)
//
//        piecesBox.remove(from)
//        piecesBox.remove(to)
//        piecesBox[newKingSquare] = king!!
//        piecesBox[newRookSquare] = rook!!
//
//        // Update turn
//        turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
//    }

    private fun canCastle(from: Square, to: Square): Boolean {
        val king = pieceAt(from) ?: return false
        val rook = pieceAt(to) ?: return false

        if (king.chessman != Chessman.KING || rook.chessman != Chessman.ROOK) return false
        if (king.player != turn || rook.player != turn) return false

        // Check if the king or rook have moved
        if (from in hasMoved || to in hasMoved) return false

        // Check if the squares between the king and rook are empty
        val direction = if (to.col > from.col) 1 else -1
        for (col in from.col + direction until to.col step direction) {
            if (pieceAt(Square(col, from.row)) != null) return false
        }

        // Check if the squares that the king moves through or ends up on are not under attack
        val kingPath = listOf(from, Square(from.col + direction, from.row), Square(from.col + 2 * direction, from.row))
        if (kingPath.any { isCheckAfterMove(king.player, from, it) }) return false

        return true
    }

    private fun performCastling(from: Square, to: Square) {
        val direction = if (to.col > from.col) 1 else -1
        val newKingSquare = Square(from.col + 2 * direction, from.row)
        val newRookSquare = Square(from.col + direction, from.row)

        piecesBox[newKingSquare] = piecesBox.remove(from)!!
        piecesBox[newRookSquare] = piecesBox.remove(to)!!

        hasMoved.add(newKingSquare)
        hasMoved.add(newRookSquare)

        turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
    }

    private fun isCheckAfterMove(player: Player, from: Square, to: Square): Boolean {
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
                wouldBeInCheckAfterMove(player, from, to)
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

