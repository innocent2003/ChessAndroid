package org.example.logicCode;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
public class ChessMain {
    private ChessEngine gameState;
    private ChessAI ai;

    public ChessMain() {
        this.gameState = new ChessEngine();
        this.ai = new ChessAI();
        initializeUI();
    }

    private void initializeUI() {
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(this);
        frame.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawPieces(g);
    }

    private void drawBoard(Graphics g) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if ((r + c) % 2 == 0) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.GRAY);
                }
                g.fillRect(c * 75, r * 75, 75, 75);
            }
        }
    }

    private void drawPieces(Graphics g) {
        char[][] board = gameState.getBoard();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                char piece = board[r][c];
                if (piece != '--') {
                    drawPiece(g, piece, r, c);
                }
            }
        }
    }

    private void drawPiece(Graphics g, char piece, int row, int col) {
        String pieceImage = "images/" + piece + ".png";
        ImageIcon icon = new ImageIcon(pieceImage);
        g.drawImage(icon.getImage(), col * 75, row * 75, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessMain());
    }
}
