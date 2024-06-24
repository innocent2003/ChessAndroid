package org.example.logicCode;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
public class ChessAI {
    private static final Map<Character, Integer> pieceScore;
    private static final double[][] knightScores, bishopScores, rookScores, queenScores;

    static {
        pieceScore = new HashMap<>();
        pieceScore.put('K', 0);
        pieceScore.put('Q', 9);
        pieceScore.put('R', 5);
        pieceScore.put('B', 3);
        pieceScore.put('N', 3);
        pieceScore.put('p', 1);

        knightScores = new double[][] {
                {0.0, 0.1, 0.2, 0.2, 0.2, 0.2, 0.1, 0.0},
                {0.1, 0.2, 0.3, 0.3, 0.3, 0.3, 0.2, 0.1},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.1, 0.2, 0.3, 0.3, 0.3, 0.3, 0.2, 0.1},
                {0.0, 0.1, 0.2, 0.2, 0.2, 0.2, 0.1, 0.0}
        };

        bishopScores = new double[][] {
                {0.0, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.0},
                {0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.2},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.2},
                {0.0, 0.2, 0.2, 0.2, 0.2, 0.2, 0.2, 0.0}
        };

        rookScores = new double[][] {
                {0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25},
                {0.25, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.25},
                {0.25, 0.35, 0.45, 0.45, 0.45, 0.45, 0.35, 0.25},
                {0.25, 0.35, 0.45, 0.45, 0.45, 0.45, 0.35, 0.25},
                {0.25, 0.35, 0.45, 0.45, 0.45, 0.45, 0.35, 0.25},
                {0.25, 0.35, 0.45, 0.45, 0.45, 0.45, 0.35, 0.25},
                {0.25, 0.35, 0.35, 0.35, 0.35, 0.35, 0.35, 0.25},
                {0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25}
        };

        queenScores = new double[][] {
                {0.0, 0.2, 0.2, 0.3, 0.3, 0.2, 0.2, 0.0},
                {0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.2},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.3, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.3},
                {0.3, 0.4, 0.4, 0.4, 0.4, 0.4, 0.4, 0.3},
                {0.2, 0.3, 0.4, 0.4, 0.4, 0.4, 0.3, 0.2},
                {0.2, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.2},
                {0.0, 0.2, 0.2, 0.3, 0.3, 0.2, 0.2, 0.0}
        };
    }

    public static Move findRandomMove(List<Move> validMoves) {
        Random rand = new Random();
        return validMoves.get(rand.nextInt(validMoves.size()));
    }

    public static Move findBestMove(GameState gameState, List<Move> validMoves) {
        Move bestMove = null;
        int highestScore = Integer.MIN_VALUE;
        for (Move move : validMoves) {
            gameState.makeMove(move);
            int score = -negaMax(gameState, 3, Integer.MIN_VALUE, Integer.MAX_VALUE);
            gameState.undoMove();
            if (score > highestScore) {
                highestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private static int negaMax(GameState gameState, int depth, int alpha, int beta) {
        if (depth == 0) {
            return scoreBoard(gameState);
        }
        List<Move> validMoves = gameState.getValidMoves();
        int maxScore = Integer.MIN_VALUE;
        for (Move move : validMoves) {
            gameState.makeMove(move);
            int score = -negaMax(gameState, depth - 1, -beta, -alpha);
            gameState.undoMove();
            if (score > maxScore) {
                maxScore = score;
            }
            if (score > alpha) {
                alpha = score;
            }
            if (alpha >= beta) {
                break;
            }
        }
        return maxScore;
    }

    private static int scoreBoard(GameState gameState) {
        int score = 0;
        char[][] board = gameState.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                char piece = board[row][col];
                if (piece != '--') {
                    int pieceScore = ChessAI.pieceScore.get(piece);
                    score += pieceScore;
                    // Add positional score if necessary
                }
            }
        }
        return score;
    }
}
