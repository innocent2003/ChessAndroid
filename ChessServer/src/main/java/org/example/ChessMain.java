package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChessMain extends JPanel{


    private static final int SQUARE_SIZE = 50;
    private static final int BOARD_SIZE = 8;
    private Map<String, BufferedImage> images;
    private String[][] board; // Represents the chessboard with piece codes
    private static final String[][] START_POSITION = {
            {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR"},
            {"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
            {"--", "--", "--", "--", "--", "--", "--", "--"},
            {"--", "--", "--", "--", "--", "--", "--", "--"},
            {"--", "--", "--", "--", "--", "--", "--", "--"},
            {"--", "--", "--", "--", "--", "--", "--", "--"},
            {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
            {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR"}
    };
    private int selectedRow = -1;
    private int selectedCol = -1;

    public ChessMain() {
        images = new HashMap<>();
        loadImages();
        board = START_POSITION;
        JFrame frame = new JFrame("Chess Board");
        frame.setSize(BOARD_SIZE * SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                // Determine which square was clicked
                int row = mouseY / SQUARE_SIZE;
                int col = mouseX / SQUARE_SIZE;

                // Check if the click is within the bounds of the board
                if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
                    // If a piece is already selected, move it to the clicked position
                    if (selectedRow != -1 && selectedCol != -1) {
                        // Implement your logic for moving pieces here
                        // For now, simply swap the pieces between the selected position and the clicked position
                        String selectedPiece = board[selectedRow][selectedCol];
                        board[selectedRow][selectedCol] = "--"; // Clear the selected position
                        board[row][col] = selectedPiece; // Move the piece to the new position
                        selectedRow = -1;
                        selectedCol = -1;
                        repaint(); // Redraw the board after the move
                    } else {
                        // Select the clicked piece (if any)
                        String pieceCode = board[row][col];
                        if (!pieceCode.equals("--")) {
                            selectedRow = row;
                            selectedCol = col;
                            repaint(); // Redraw the board to highlight the selected piece
                        }
                    }
                }
            }
        });
    }

    private void loadImages() {
        String[] pieces = {"wp", "wR", "wN", "wB", "wK", "wQ", "bp", "bR", "bN", "bB", "bK", "bQ"};
        for (String piece : pieces) {
            try {
                String imagePath = "E:\\ChessAndroid\\ChessServer\\src\\main\\java\\org\\example\\images\\"+piece+".png";
                BufferedImage image = ImageIO.read(new File(imagePath));
                BufferedImage scaledImage = scaleImage(image, SQUARE_SIZE, SQUARE_SIZE);
                images.put(piece, scaledImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage scaleImage(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return scaledImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the chessboard
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Color color = ((row + col) % 2 == 0) ? Color.WHITE : Color.GRAY;
                g.setColor(color);
                g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

                // Draw piece if present
                String pieceCode = board[row][col];
                if (!pieceCode.equals("--")) {
                    BufferedImage pieceImage = images.get(pieceCode);
                    g.drawImage(pieceImage, col * SQUARE_SIZE, row * SQUARE_SIZE, this);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessMain::new);
    }
}
