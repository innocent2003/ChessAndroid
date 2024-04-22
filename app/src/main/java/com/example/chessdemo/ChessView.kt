package com.example.chessdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class ChessView(context: Context?, attrs:AttributeSet) : View(context, attrs) {
    private final val originX = 20f
    private final val originY = 200f
    private final val cellSide = 130f
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
    init{
        loadBitmaps()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawChessBoard(canvas)
        drawPieces(canvas)
    }
    private fun drawPieces(canvas: Canvas?){
        val chessModel = ChessModel()
        chessModel.reset()
        for(row in 0..7){
            for(col in 0..7){
//                val piece = chessModel.pieceAt(col, row)
//                if(piece != null){
//                    drawPieceAt(canvas, col, row, piece.resID)
//                }
                chessModel.pieceAt(col,row)?.let{drawPieceAt(canvas,col,row,it.resID)}
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
        for(j in 0..7){
            for(i in 0..7){
                paint.color  = if((i+j) % 2 == 1) darkColor else lightColor
                canvas?.drawRect(originX + i *cellSide, originY + j *cellSide, originX+(i+1)*cellSide,originY+(j+1)*cellSide,paint)
            }
        }
    }

//    private fun drawSquareAt(col: Int, row: Int, isDark: Boolean){
//        paint.color = if(isDark) darkColor
//    }



}