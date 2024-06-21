package org.example;

import java.util.ArrayList;

public class ChessEngine {
    private String[][] board;
    public boolean whiteToMove = true;
//    public ArrayList<String> moveLog;


    public ChessEngine() {
        this.board = new String[][] {
                {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR"},
                {"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
                {"--", "--", "--", "--", "--", "--", "--", "--"},
                {"--", "--", "--", "--", "--", "--", "--", "--"},
                {"--", "--", "--", "--", "--", "--", "--", "--"},
                {"--", "--", "--", "--", "--", "--", "--", "--"},
                {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
                {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR"}
        };
    }

    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        ChessEngine chessBoard = new ChessEngine();
        chessBoard.printBoard();
    }
}
