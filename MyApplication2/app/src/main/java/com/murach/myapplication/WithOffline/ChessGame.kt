package com.murach.myapplication.WithOffline


import android.content.Context
import android.icu.text.Transliterator
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow

import android.widget.Toast
import com.murach.myapplication.R


import com.murach.myapplication.enums.Chessman
import com.murach.myapplication.enums.Player


import kotlin.math.abs

object ChessGame {
//    val anchorView = findViewById<View>(R.id.white_promote)
    var piecesBox: MutableMap<Square, ChessPiece>
    var turn = Player.WHITE
    private val hasMoved: MutableSet<Square>
    private var hasKingMoved =
        mutableMapOf<Player, Boolean>(Player.WHITE to false, Player.BLACK to false)
    private var hasRookMoved = mutableMapOf<Square, Boolean>()
    private lateinit var appContext: Context

    // Other properties and functions

    fun initialize(context: Context) {
        appContext = context.applicationContext
        // Other initialization code
    }
    var chessDelegate: ChessDelegate? = null


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
//check stalemate

//        piecesBox[Square(2,3)] = ChessPiece(Player.WHITE, Chessman.QUEEN,R.drawable.queen_white)
//        piecesBox[Square(0,0)] = ChessPiece(Player.BLACK,Chessman.KING,R.drawable.king_black);
//        piecesBox[Square(7,7)] = ChessPiece(Player.WHITE,Chessman.KING,R.drawable.king_white);

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


    fun pieceAt(square: Square): ChessPiece? {
        return piecesBox[square]
    }
    private fun anotherWayToCastle(from: Square, to: Square){


    }
    private fun performCastling(from: Square, to: Square) {
        val direction = if (to.col > from.col) 1 else -1
        val newKingSquare = Square(from.col + 2 * direction, from.row)
        val newRookSquare = Square(from.col + direction, from.row)
        val rookSquare = Square(if (direction == 1) 7 else 0, from.row)

        piecesBox[newKingSquare] = piecesBox.remove(from)!!
        piecesBox[newRookSquare] = piecesBox.remove(rookSquare)!!

        hasMoved.add(newKingSquare)
        hasMoved.add(newRookSquare)

        hasKingMoved[piecesBox[newKingSquare]!!.player] = true
        hasRookMoved[rookSquare] = true

        turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
    }

    private fun canCastle(from: Square, to: Square): Boolean {
        val movingPiece = pieceAt(from) ?: return false
        if (movingPiece.chessman != Chessman.KING) return false

        // Check if the king or the rook has moved
        if (hasKingMoved[movingPiece.player] == true) return false

        val direction = if (to.col > from.col) 1 else -1
        val rookCol = if (direction == 1) 7 else 0
        val rookSquare = Square(rookCol, from.row)
        val rookPiece = pieceAt(rookSquare) ?: return false
        if (rookPiece.chessman != Chessman.ROOK || hasRookMoved[rookSquare] == true) return false

        // Check if the path between the king and rook is clear
        for (i in 1 until abs(to.col - from.col)) {
            if (pieceAt(Square(from.col + i * direction, from.row)) != null) {
                return false
            }
        }
//        for(i in 1 until 4){
//            if(pieceAt(Square(0,i)) != null) return false;
//            if(pieceAt(Square(7,i)) != null) return false;
//        }
//        for(j in 5 until 7){
//            if(pieceAt(Square(0,j)) != null) return false;
//            if(pieceAt(Square(7,j)) != null) return false;
//        }

        // Check if the king is in check or passes through a square that is under attack
        // For simplicity, this example does not implement the check detection function.
        // You would need to implement a function `isSquareUnderAttack` to complete this part.

        return true
    }

    var isEnPassantMove: Boolean = false

    private fun canEnPassant(from: Square, to: Square) {
        val movingPiece = pieceAt(from) ?: return
        val enWhite = 4
        val enBlack = 3

        for( i in 0 until 8) {
            if (movingPiece.chessman == Chessman.PAWN && movingPiece.player == Player.WHITE && from.row == enWhite && from.col == i && to.row == enWhite + 1 && to.col == i + 1) {
                piecesBox.remove(Square(i + 1, enWhite));
                piecesBox.remove(from);
                piecesBox[to] = movingPiece
                turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
                isEnPassantMove = true
            }
            if(movingPiece.chessman == Chessman.PAWN && movingPiece.player == Player.BLACK && from.row == enBlack && from.col == i && to.row == enBlack-1 && to.col == i+1){
                piecesBox.remove(Square(i+1,enBlack));
                piecesBox.remove(from);
                piecesBox[to] = movingPiece
                turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
                isEnPassantMove = true
            }
        }
        for(i in 7  downTo 0) {
            if (movingPiece.chessman == Chessman.PAWN && movingPiece.player == Player.WHITE && from.row == enWhite && from.col == i && to.row == enWhite + 1 && to.col == i - 1) {
                piecesBox.remove(Square(i - 1, enWhite));
                piecesBox.remove(from);
                piecesBox[to] = movingPiece
                turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
                isEnPassantMove = true
            }
            if(movingPiece.chessman == Chessman.PAWN && movingPiece.player == Player.BLACK && from.row == enBlack && from.col == i && to.row == enBlack-1 && to.col == i-1){
                piecesBox.remove(Square(i - 1,enBlack));
                piecesBox.remove(from);
                piecesBox[to] = movingPiece
                turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
                isEnPassantMove = true
            }
        }

    }

    fun movePiece(from: Square, to: Square) {

        val movingPiece = pieceAt(from) ?: return
        canEnPassant(from, to)

//        if (movingPiece.chessman == Chessman.KING && canCastle(from, to) && (!isCheck(Player.WHITE) || !isCheck(Player.BLACK)) ) {
//            Log.d("ChessGame", "Castling: $from to $to")
//            performCastling(from, to)
//        }
        if(movingPiece.chessman == Chessman.KING && to.col == 2 && to.row == 0 && movingPiece.player == Player.WHITE && pieceAt(Square(1,0)) == null && pieceAt(Square(2,0)) == null && pieceAt(Square(3,0)) == null && !isCheck(Player.WHITE) && hasKingMoved[movingPiece.player] == false ){

                    piecesBox.remove(Square(0,0))
                    piecesBox.remove(from)
                    piecesBox[to] = ChessPiece(Player.WHITE,Chessman.KING,R.drawable.king_white)
                    piecesBox[Square(3,0)] = ChessPiece(Player.WHITE,Chessman.ROOK,R.drawable.rook_white)
                    turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
                    Log.d("ChessGame", "Castling: $from to $to")
            Toast.makeText(appContext, "Castling: $from to $to", Toast.LENGTH_SHORT).show()
        }
        if(movingPiece.chessman == Chessman.KING && to.col == 6 && to.row == 0 && movingPiece.player == Player.WHITE && pieceAt(Square(5,0)) == null && pieceAt(Square(6,0)) == null && !isCheck(Player.WHITE)  ){

            piecesBox.remove(Square(7,0))
            piecesBox.remove(from)
            piecesBox[to] = ChessPiece(Player.WHITE,Chessman.KING,R.drawable.king_white)
            piecesBox[Square(5,0)] = ChessPiece(Player.WHITE,Chessman.ROOK,R.drawable.rook_white)
            turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
            Log.d("ChessGame", "Castling: $from to $to")
            Toast.makeText(appContext, "Castling: $from to $to", Toast.LENGTH_SHORT).show()
        }
        if(movingPiece.chessman == Chessman.KING && to.col == 2 && to.row == 7 && movingPiece.player == Player.BLACK && pieceAt(Square(1,7)) == null && pieceAt(Square(2,7)) == null && pieceAt(Square(3,7)) == null && !isCheck(Player.BLACK) && hasKingMoved[movingPiece.player] == false ){

            piecesBox.remove(Square(0,7))
            piecesBox.remove(from)
            piecesBox[to] = ChessPiece(Player.BLACK,Chessman.KING,R.drawable.king_black)
            piecesBox[Square(3,7)] = ChessPiece(Player.BLACK,Chessman.ROOK,R.drawable.rook_black)
            turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
            Log.d("ChessGame", "Castling: $from to $to")
            Toast.makeText(appContext, "Castling: $from to $to", Toast.LENGTH_SHORT).show()
        }
        if(movingPiece.chessman == Chessman.KING && to.col == 6 && to.row == 7 && movingPiece.player == Player.BLACK && pieceAt(Square(5,7)) == null && pieceAt(Square(6,7)) == null && !isCheck(Player.BLACK)  ){

            piecesBox.remove(Square(7,7))
            piecesBox.remove(from)
            piecesBox[to] = ChessPiece(Player.BLACK,Chessman.KING,R.drawable.king_black)
            piecesBox[Square(5,7)] = ChessPiece(Player.BLACK,Chessman.ROOK,R.drawable.rook_black)
            turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
            Log.d("ChessGame", "Castling: $from to $to")
            Toast.makeText(appContext, "Castling: $from to $to", Toast.LENGTH_SHORT).show()
        }
        else if (movingPiece.player == turn && canMove(from, to)) {
            Log.d("ChessGame", "Moving ${movingPiece.chessman} from $from to $to")
            Toast.makeText(appContext, "Moving ${movingPiece.chessman} from $from to $to", Toast.LENGTH_SHORT).show()
            piecesBox[to] = movingPiece
            piecesBox.remove(from)

            if (movingPiece.chessman == Chessman.KING) {
                hasKingMoved[movingPiece.player] = true
            } else if (movingPiece.chessman == Chessman.ROOK) {
                hasRookMoved[from] = true
            }

            if (movingPiece.chessman == Chessman.PAWN && to.row == 7) {
                // Pawn promotion

                    // Log pawn promotion
                    Log.d("ChessGame", "Pawn promotion at $to")

//                showPopupWindow(anchorView, from)
                    piecesBox.remove(to)
                    piecesBox[to] = ChessPiece(Player.WHITE,Chessman.QUEEN,R.drawable.queen_white)
            }
//            if (movingPiece.chessman == Chessman.PAWN && to.row == 0) {
//                // Pawn promotion
//
//                // Log pawn promotion
//                Log.d("ChessGame", "Pawn promotion at $to")
//
////                showPopupWindow(anchorView, from)
//                piecesBox.remove(to)
//                piecesBox[to] = ChessPiece(Player.BLACK,Chessman.QUEEN,R.drawable.queen_black)
//            }
            promotePawn(movingPiece, to, piecesBox)
//            promotePawn(movingPiece, to, piecesBox, )
//            if (movingPiece.chessman == Chessman.PAWN && (to.row == 0 || to.row == 7)) {
//                chessDelegate?.onPawnPromotion(to)
//            }
            turn = if (turn == Player.WHITE) Player.BLACK else Player.WHITE
        }
    }
    fun promotePawn(movingPiece: ChessPiece, to: Square, piecesBox: MutableMap<Square, ChessPiece>) {
        if (movingPiece.chessman == Chessman.PAWN && (to.row == 0 || to.row == 7)) {
            // Determine the player and promote to the appropriate queen
            val newPiece = if (movingPiece.player == Player.WHITE) {
                ChessPiece(Player.WHITE, Chessman.QUEEN, R.drawable.queen_white)
            } else {
                ChessPiece(Player.BLACK, Chessman.QUEEN, R.drawable.queen_black)
            }

            // Log pawn promotion
            Log.d("ChessGame", "Pawn promotion at $to")
            Toast.makeText(appContext, "Pawn promotion at $to", Toast.LENGTH_SHORT).show()

            // Remove the pawn from the board and replace it with a queen
            piecesBox.remove(to)
            piecesBox[to] = newPiece
        }
    }
//    fun promotePawn(movingPiece: ChessPiece, to: Square, piecesBox: MutableMap<Square, ChessPiece>, onPromotionPieceSelected: (Chessman) -> Unit) {
//        if (movingPiece.chessman == Chessman.PAWN && (to.row == 0 || to.row == 7)) {
//            Log.d("ChessGame", "Pawn promotion at $to")
//            onPromotionPieceSelected { selectedPiece ->
//                val newPiece = ChessPiece(movingPiece.player, selectedPiece, getDrawableForChessman(selectedPiece, movingPiece.player))
//                piecesBox[to] = newPiece
//            }
//        }
//    }

    private fun getDrawableForChessman(chessman: Chessman, player: Player): Int {
        return when (chessman) {
            Chessman.QUEEN -> if (player == Player.WHITE) R.drawable.queen_white else R.drawable.queen_black
            Chessman.ROOK -> if (player == Player.WHITE) R.drawable.rook_white else R.drawable.rook_black
            Chessman.BISHOP -> if (player == Player.WHITE) R.drawable.bishop_white else R.drawable.bishop_black
            Chessman.KNIGHT -> if (player == Player.WHITE) R.drawable.knight_white else R.drawable.knight_black
            else -> throw IllegalArgumentException("Invalid chessman for promotion")
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
                        val rookCol = if (to.col > from.col) 7 else 0
                        val rookSquare = Square(rookCol, from.row)
                        val rookPiece = pieceAt(rookSquare)
                        if (rookPiece != null && rookPiece.chessman == Chessman.ROOK && !hasRookMoved[rookSquare]!!) {
                            val direction = if (rookCol == 7) 1 else -1
                            var pathClear = true
                            for (i in 1 until abs(to.col - from.col)) {
                                if (pieceAt(Square(from.col + i * direction, from.row)) != null) {
                                    pathClear = false
                                    break
                                }
                            }
                            if (pathClear) return true
                        }
                    }
                    return abs(deltaCol) <= 1 && abs(deltaRow) <= 1
                }
            }
        }
        return false
    }

    private fun isClearVerticallyBetween(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val range =
            if (from.row < to.row) (from.row + 1 until to.row) else (to.row + 1 until from.row)
        return range.none { pieceAt(Square(from.col, it)) != null }
    }

    private fun isClearHorizontallyBetween(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val range =
            if (from.col < to.col) (from.col + 1 until to.col) else (to.col + 1 until from.col)
        return range.none { pieceAt(Square(it, from.row)) != null }
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


    private fun showPopupWindow(anchorView: View, from: Square) {
        val inflater = LayoutInflater.from(anchorView.context)
        val popupView = inflater.inflate(R.layout.blackpromote, null)

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        // Show the popup window
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)

        // Close the popup window on button click
//        val buttonClose: Button = popupView.findViewById(R.id.button_close)
//        buttonClose.setOnClickListener {
//            popupWindow.dismiss()
//        }
    }

}





