package org.example;

import java.util.Map;
import java.util.HashMap;

public class ChessAI {
    public static final Map<String, Integer> pieceScore = new HashMap<>();
    static {
        pieceScore.put("K", 0);
        pieceScore.put("Q", 9);
        pieceScore.put("R", 5);
        pieceScore.put("B", 3);
        pieceScore.put("N", 3);
        pieceScore.put("p", 1);
    }

    // Knight scores
    public static final double[][] knightScores = {
            {0.0, 0.1, 0.2, 0.2, 0.2, 0.2, 0.1, 0.0},
            {0.1, 0.3, 0.5, 0.5, 0.5, 0.5, 0.3, 0.1},
            {0.2, 0.5, 0.6, 0.65, 0.65, 0.6, 0.5, 0.2},
            {0.2, 0.55, 0.65, 0.7, 0.7, 0.65, 0.55, 0.2},
            {0.2, 0.5, 0.65, 0.7, 0.7, 0.65, 0.5, 0.2},
            {0.2, 0.55, 0.6, 0.65, 0.65, 0.6, 0.55, 0.2},
            {0.1, 0.3, 0.5, 0.55, 0.55, 0.5, 0.3, 0.1},
            {0.0, 0.1, 0.2, 0.2, 0.2, 0.2, 0.1, 0.0}
    };

    // Bishop scores
    public static final double[][] bishopScores = {
            {0.0, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.0},
            {0.2, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.2},
            {0.2, 0.4, 0.5, 0.6, 0.6, 0.5, 0.4, 0.2},
            {0.2, 0.5, 0.5, 0.6, 0.6, 0.5, 0.5, 0.2},
            {0.2, 0.4, 0.6, 0.6, 0.6, 0.6, 0.4, 0.2},
            {0.2, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.2},
            {0.2, 0.5, 0.4, 0.4, 0.4, 0.4, 0.5, 0.2},
            {0.0, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.0}
    };

    // Rook scores
    public static final double[][] rookScores = {
            {0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
            {0.5, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75, 0.5},
            {0.0, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.0},
            {0.0, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.0},
            {0.0, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.0},
            {0.0, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.0},
            {0.0, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.0},
            {0.25, 0.25, 0.25, 0.5, 0.5, 0.25, 0.25, 0.25}
    };

    // Queen scores
    public static final double[][] queenScores = {
            {0.0, 0.2, 0.2, 0.3, 0.3, 0.2, 0.2, 0.0},
            {0.2, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.2},
            {0.2, 0.4, 0.5, 0.5, 0.5, 0.5, 0.4, 0.2},
            {0.3, 0.4, 0.5, 0.5, 0.5, 0.5, 0.4, 0.3},
            {0.4, 0.4, 0.5, 0.5, 0.5, 0.5, 0.4, 0.3},
            {0.2, 0.5, 0.5, 0.5, 0.5, 0.5, 0.4, 0.2},
            {0.2, 0.4, 0.5, 0.4, 0.4, 0.4, 0.4, 0.2},
            {0.0, 0.2, 0.2, 0.3, 0.3, 0.2, 0.2, 0.0}
    };

    // Pawn scores
    public static final double[][] pawnScores = {
            {0.8, 0.8, 0.8, 0.8, 0.8, 0.8, 0.8, 0.8},
            {0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7, 0.7},
            {0.3, 0.3, 0.4, 0.5, 0.5, 0.4, 0.3, 0.3},
            {0.25, 0.25, 0.3, 0.45, 0.45, 0.3, 0.25, 0.25},
            {0.2, 0.2, 0.2, 0.4, 0.4, 0.2, 0.2, 0.2},
            {0.25, 0.15, 0.1, 0.2, 0.2, 0.1, 0.15, 0.25},
            {0.25, 0.3, 0.3, 0.0, 0.0, 0.3, 0.3, 0.25},
            {0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2}
    };

    // Piece position scores
    public static final Map<String, double[][]> piecePositionScores = new HashMap<>();
    static {
        piecePositionScores.put("wN", knightScores);
        piecePositionScores.put("bN", reverseArray(knightScores));
        piecePositionScores.put("wB", bishopScores);
        piecePositionScores.put("bB", reverseArray(bishopScores));
        piecePositionScores.put("wQ", queenScores);
        piecePositionScores.put("bQ", reverseArray(queenScores));
        piecePositionScores.put("wR", rookScores);
        piecePositionScores.put("bR", reverseArray(rookScores));
        piecePositionScores.put("wp", pawnScores);
        piecePositionScores.put("bp", reverseArray(pawnScores));
    }

    // Constants
    public static final int CHECKMATE = 1000;
    public static final int STALEMATE = 0;
    public static final int DEPTH = 3;

    // Helper method to reverse a 2D array
    private static double[][] reverseArray(double[][] array) {
        double[][] reversed = new double[array.length][array[0].length];
        for (int i = 0; i < array.length; i++) {
            reversed[i] = array[array.length - 1 - i];
        }
        return reversed;
    }
}
