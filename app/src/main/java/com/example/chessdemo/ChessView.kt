package com.example.chessdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.nfc.Tag
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class ChessView(context: Context?, attrs:AttributeSet) : View(context, attrs) {
    private final val scaleFactor = .9f
    private final var originX = 20f
    private final var originY = 200f
    private final var cellSide = 130f
    private final val imgResIDs = setOf(
        R.drawable.bishop_black,
        R.drawable.bishop_white,
        R.drawable.king_black,
        R.drawable.king_white,
        R.drawable.queen_white,
        R.drawable.queen_black,
        R.drawable.knight_black,
        R.drawable.knight_white,
        R.drawable.pawn_black,
        R.drawable.pawn_white,
        R.drawable.rook_black,
        R.drawable.rook_white
    )
    private final val bitmaps = mutableMapOf<Int,Bitmap>()
    private final val paint = Paint()
    private final val lightColor = Color.argb(1f,.4f,.4f,.4f)
    private final val darkColor = Color.argb(1f,.5f,.5f,.5f)

    var chessDelegate: ChessDelegate? = null
    init{
        loadBitmaps()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Log.d(TAG, "${canvas?.width}, ${canvas?.height}")
        canvas ?: return

            val chessBoardSide = min(canvas.width, canvas.height) *scaleFactor
            cellSide = chessBoardSide / 8f
            originX = (canvas.width - chessBoardSide) / 2f
            originY = (canvas.height - chessBoardSide) / 2f


        drawChessBoard(canvas)
        drawPieces(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?:return false
        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                Log.d(TAG, "down")
            }
            MotionEvent.ACTION_UP ->{
                Log.d(TAG, "up")
            }
        }
        return true
    }
    private fun drawPieces(canvas: Canvas?){
//        val chessModel = ChessModel()
//        chessModel.reset()
        for(row in 0..7){
            for(col in 0..7){
//                val piece = chessModel.pieceAt(col, row)
//                if(piece != null){
//                    drawPieceAt(canvas, col, row, piece.resID)
//                }
                chessDelegate?.pieceAt(col,row)?.let{drawPieceAt(canvas,col,row,it.resID)}
            }
        }

//        drawPieceAt(canvas, 0, 0 , R.drawable.rook_white)
//        drawPieceAt(canvas, 0, 1 , R.drawable.pawn_white)
    }
    private fun drawPieceAt(canvas: Canvas?, col: Int, row: Int, resID: Int){
        val bitmap = bitmaps[resID]!!
        canvas?.drawBitmap(bitmap, null,RectF(originX + col* cellSide,originY + (7 - row) * cellSide,originX +(col +1)* cellSide,originY+((7- row)+1)*cellSide ),paint)
    }
    private fun loadBitmaps(){
        imgResIDs.forEach{
            bitmaps[it] = BitmapFactory.decodeResource(resources,it)

        }
    }
    private fun drawChessBoard(canvas: Canvas){
        for(row in 0..7){
            for(col in 0..7){
                drawSquareAt(canvas, col, row, (col+row)%2==1)
//                paint.color  = if((i+j) % 2 == 1) darkColor else lightColor
//                canvas?.drawRect(originX + i *cellSide, originY + j *cellSide, originX+(i+1)*cellSide,originY+(j+1)*cellSide,paint)
            }
        }
    }

    private fun drawSquareAt(canvas: Canvas?,col: Int, row: Int, isDark: Boolean){
        paint.color  = if(isDark) darkColor else lightColor
        canvas?.drawRect(originX + col *cellSide, originY + row *cellSide, originX+(col+1)*cellSide,originY+(row+1)*cellSide,paint)
    }



}