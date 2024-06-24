package org.example.logicCode;
import java.util.List;
import java.util.ArrayList;
public class ChessEngine {
    private char[][] board;
    private boolean whiteToMove;
    private List<Move> moveHistory;

    public ChessEngine() {
        board = new char[8][8];
        whiteToMove = true;
        moveHistory = new ArrayList<>();
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize the board with starting positions
        String[] startingPositions = {
                "rnbqkbnr",
                "pppppppp",
                "--------",
                "--------",
                "--------",
                "--------",
                "PPPPPPPP",
                "RNBQKBNR"
        };
        for (int i = 0; i < 8; i++) {
            board[i] = startingPositions[i].toCharArray();
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public List<Move> getValidMoves() {
        // Generate and return a list of valid moves
        return new ArrayList<>();
    }

    public void makeMove(Move move) {
        // Execute a move
        board[move.getEndRow()][move.getEndCol()] = board[move.getStartRow()][move.getStartCol()];
        board[move.getStartRow()][move.getStartCol()] = '--';
        whiteToMove = !whiteToMove;
        moveHistory.add(move);
    }

    public void undoMove() {
        if (!moveHistory.isEmpty()) {
            Move move = moveHistory.remove(moveHistory.size() - 1);
            board[move.getStartRow()][move.getStartCol()] = board[move.getEndRow()][move.getEndCol()];
            board[move.getEndRow()][move.getEndCol()] = move.getCapturedPiece();
            whiteToMove = !whiteToMove;
        }
    }

    public boolean isCheckmate() {
        // Check for checkmate
        return false;
    }

    public boolean isStalemate() {
        // Check for stalemate
        return false;
    }

}
