package com.murach.myapplication.newLogicBot

class GameState {
    val board: Array<CharArray> = Array(8) { CharArray(8) }
    var whiteToMove: Boolean = true
    private val moveHistory: MutableList<Move> = mutableListOf()

    init {
        initializeBoard()
    }

    private fun initializeBoard() {
        val startingPositions = arrayOf(
            "rnbqkbnr",
            "pppppppp",
            "--------",
            "--------",
            "--------",
            "--------",
            "PPPPPPPP",
            "RNBQKBNR"
        )
        for (i in startingPositions.indices) {
            board[i] = startingPositions[i].toCharArray()
        }
    }

    fun getValidMoves(): List<Move> {
        val validMoves = mutableListOf<Move>()
        for (r in board.indices) {
            for (c in board[r].indices) {
                val piece = board[r][c]
                if (piece != '-' && isPieceWhite(piece) == whiteToMove) {
                    validMoves.addAll(getValidMovesForPiece(r, c))
                }
            }
        }
        return validMoves
    }

    fun getValidMovesForPiece(row: Int, col: Int): List<Move> {
        val piece = board[row][col]
        val moves = mutableListOf<Move>()
        if (piece == 'P' || piece == 'p') {
            val direction = if (piece == 'P') -1 else 1
            val startRow = if (piece == 'P') 6 else 1
            if (board[row + direction][col] == '-') {
                moves.add(Move(row, col, row + direction, col, piece, '-'))
                if (row == startRow && board[row + 2 * direction][col] == '-') {
                    moves.add(Move(row, col, row + 2 * direction, col, piece, '-'))
                }
            }
            if (col - 1 >= 0 && board[row + direction][col - 1] != '-' && isPieceWhite(board[row + direction][col - 1]) != whiteToMove) {
                moves.add(Move(row, col, row + direction, col - 1, piece, board[row + direction][col - 1]))
            }
            if (col + 1 < 8 && board[row + direction][col + 1] != '-' && isPieceWhite(board[row + direction][col + 1]) != whiteToMove) {
                moves.add(Move(row, col, row + direction, col + 1, piece, board[row + direction][col + 1]))
            }
        }
        // Add logic for other pieces
        return moves
    }

    private fun isPieceWhite(piece: Char): Boolean {
        return piece.isUpperCase()
    }

    fun makeMove(move: Move) {
        board[move.endRow][move.endCol] = board[move.startRow][move.startCol]
        board[move.startRow][move.startCol] = '-'
        whiteToMove = !whiteToMove
        moveHistory.add(move)
    }

    fun undoMove() {
        if (moveHistory.isNotEmpty()) {
            val move = moveHistory.removeAt(moveHistory.size - 1)
            board[move.startRow][move.startCol] = board[move.endRow][move.endCol]
            board[move.endRow][move.endCol] = move.capturedPiece
            whiteToMove = !whiteToMove
        }
    }

    fun isCheckmate(): Boolean {
        // Check for checkmate
        return false
    }

    fun isStalemate(): Boolean {
        // Check for stalemate
        return false
    }
}
