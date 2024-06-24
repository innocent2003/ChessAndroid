package org.example.logicCode;

public class Move {
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;
    private char pieceMoved;
    private char pieceCaptured;

    public Move(int startRow, int startCol, int endRow, int endCol, char pieceMoved, char pieceCaptured) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.pieceMoved = pieceMoved;
        this.pieceCaptured = pieceCaptured;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getEndCol() {
        return endCol;
    }

    public char getPieceMoved() {
        return pieceMoved;
    }

    public char getCapturedPiece() {
        return pieceCaptured;
    }
}
