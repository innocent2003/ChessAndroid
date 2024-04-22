package com.example.chessdemo

class ChessModel {
    var pieceBox = mutableSetOf<ChessPiece>()
    init {
        reset()
    }
     fun reset(){
        pieceBox.removeAll(pieceBox)
        for(i in 0..1){
            pieceBox.add(ChessPiece(0+i*7,0,ChessPlayer.WHITE, ChessRank.ROOK, R.drawable.rook_white ))
            pieceBox.add(ChessPiece(0+i*7,7,ChessPlayer.BLACK, ChessRank.ROOK, R.drawable.rook_black ))

            pieceBox.add(ChessPiece(1+i*5,0,ChessPlayer.WHITE, ChessRank.KNIGHT, R.drawable.knight_white ))
            pieceBox.add(ChessPiece(1+i*5,7,ChessPlayer.BLACK, ChessRank.KNIGHT, R.drawable.knight_black ))

            pieceBox.add(ChessPiece(2+i*3,0,ChessPlayer.WHITE, ChessRank.BISHOP, R.drawable.bishop_white ))
            pieceBox.add(ChessPiece(2+i*3,7,ChessPlayer.BLACK, ChessRank.BISHOP, R.drawable.bishop_black ))
        }
        for(i in 0..7){
            pieceBox.add(ChessPiece(i,1,ChessPlayer.WHITE, ChessRank.PAWN, R.drawable.pawn_white ))
            pieceBox.add(ChessPiece(i,6,ChessPlayer.BLACK, ChessRank.PAWN, R.drawable.pawn_black ))
        }
        pieceBox.add(ChessPiece(3,0,ChessPlayer.WHITE, ChessRank.QUEEN, R.drawable.queen_white ))
        pieceBox.add(ChessPiece(3,7,ChessPlayer.BLACK, ChessRank.QUEEN, R.drawable.queen_black ))

        pieceBox.add(ChessPiece(4,0,ChessPlayer.WHITE, ChessRank.KING, R.drawable.king_white ))
        pieceBox.add(ChessPiece(4,7,ChessPlayer.BLACK, ChessRank.KING, R.drawable.king_black ))

    }

     fun pieceAt(col: Int, row: Int) : ChessPiece?{
        for(piece in pieceBox){
            if(col == piece.col && row == piece.row){
                return piece;
            }
        }
        return null;
    }
    override fun toString() : String{
        var desc = "\n"
        for(row in 7 downTo 0){
            desc += "${row}"
            for(col in 0..7){
                val piece = pieceAt(col, row)
                if(piece == null){
                    desc += " ."

                }else{
                    val white = piece.player == ChessPlayer.WHITE
                    desc += " "
                    when(piece.Rank){
                        ChessRank.KING -> {
                            desc += if(white) "k" else "K"
                        }
                        ChessRank.QUEEN  -> {
                            desc += if(white) "q" else "Q"
                        }
                        ChessRank.KNIGHT-> {
                            desc += if(white) "n" else "N"
                        }
                        ChessRank.ROOK -> {
                            desc += if(white) "r" else "R"
                        }
                        ChessRank.BISHOP-> {
                            desc += if(white) "b" else "B"
                        }
                        ChessRank.PAWN -> {
                            desc += if(white) "p" else "P"
                        }

                    }
                }

            }
            desc +="\n"
        }
        desc += " 0 1 2 3 4 5 6 7"
        return desc;
    }
}