package com.murach.myapplication.newLogicBot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.murach.myapplication.R

class ChessBoardView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private val gameState = GameState()
    private val paint = Paint()
    private val pieceDrawables = mutableMapOf<Char, Int>()
    private var selectedRow = -1
    private var selectedCol = -1
    private var validMoves = listOf<Move>()

    init {
        pieceDrawables['r'] = R.drawable.br
        pieceDrawables['n'] = R.drawable.bn
        pieceDrawables['b'] = R.drawable.bb
        pieceDrawables['q'] = R.drawable.bq
        pieceDrawables['k'] = R.drawable.bk
        pieceDrawables['p'] = R.drawable.bp
        pieceDrawables['R'] = R.drawable.wr
        pieceDrawables['N'] = R.drawable.wn
        pieceDrawables['B'] = R.drawable.wb
        pieceDrawables['Q'] = R.drawable.wq
        pieceDrawables['K'] = R.drawable.wk
        pieceDrawables['P'] = R.drawable.wp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBoard(canvas)
        drawPieces(canvas)
        drawValidMoves(canvas)
    }

    private fun drawBoard(canvas: Canvas) {
        val tileSize = width / 8
        for (r in 0 until 8) {
            for (c in 0 until 8) {
                paint.color = if ((r + c) % 2 == 0) Color.WHITE else Color.GRAY
                canvas.drawRect(c * tileSize.toFloat(), r * tileSize.toFloat(), (c + 1) * tileSize.toFloat(), (r + 1) * tileSize.toFloat(), paint)
            }
        }
    }

    private fun drawPieces(canvas: Canvas) {
        val tileSize = width / 8
        for (r in 0 until 8) {
            for (c in 0 until 8) {
                val piece = gameState.board[r][c]
                if (piece != '-') {
                    val drawableId = pieceDrawables[piece] ?: continue
                    val drawable = ContextCompat.getDrawable(context, drawableId)
                    drawable?.setBounds(c * tileSize, r * tileSize, (c + 1) * tileSize, (r + 1) * tileSize)
                    drawable?.draw(canvas)
                }
            }
        }
    }

    private fun drawValidMoves(canvas: Canvas) {
        val tileSize = width / 8
        paint.color = Color.YELLOW
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        for (move in validMoves) {
            canvas.drawRect(
                move.endCol * tileSize.toFloat(),
                move.endRow * tileSize.toFloat(),
                (move.endCol + 1) * tileSize.toFloat(),
                (move.endRow + 1) * tileSize.toFloat(),
                paint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        if (event.action == MotionEvent.ACTION_DOWN) {
//            val col = (event.x / (width / 8)).toInt()
//            val row = (event.y / (height / 8)).toInt()
//            handleTouch(row, col)
//        }
        event ?: return false
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("ChessBoardView","Down")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("ChessBoardView","Up")
            }
            MotionEvent.ACTION_MOVE ->{
                Log.d("ChessBoardView","move")
            }
        }

        return true
    }

    private fun handleTouch(row: Int, col: Int) {
        if (selectedRow == -1 && selectedCol == -1) {
            // Select the piece
            if (gameState.board[row][col] != '-') {
                selectedRow = row
                selectedCol = col
                validMoves = gameState.getValidMovesForPiece(row, col)
                Log.d("ChessBoardView", "Selected piece at ($row, $col), valid moves: $validMoves")
                invalidate()
            }
        } else {
            // Make the move
            val piece = gameState.board[selectedRow][selectedCol]
            val move = Move(selectedRow, selectedCol, row, col, piece, gameState.board[row][col])
            if (move in validMoves) {
                gameState.makeMove(move)
                Log.d("ChessBoardView", "Moved piece from ($selectedRow, $selectedCol) to ($row, $col)")
                invalidate()
            } else {
                Log.d("ChessBoardView", "Invalid move from ($selectedRow, $selectedCol) to ($row, $col)")
            }
            // Deselect the piece
            selectedRow = -1
            selectedCol = -1
            validMoves = emptyList()
            invalidate()
        }
    }
}
