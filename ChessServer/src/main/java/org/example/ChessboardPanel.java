package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ChessboardPanel extends JPanel {

//
//    private static final int SQUARE_SIZE = 50; // Size of each square (adjust as necessary)
//    private Map<String, BufferedImage> images;
//    private static final int DIMENSION = 8;
//
//    public ChessMain() {
//        images = new HashMap<>();
//        loadImages();
//
//        // Example of displaying a loaded image (optional)
//        JFrame frame = new JFrame("Loaded Image Example");
//        frame.setSize(400, 400);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JLabel label = new JLabel(new ImageIcon(images.get("wp"))); // Example: Display white pawn
//        frame.add(label, BorderLayout.CENTER);
//        JLabel label1 = new JLabel(new ImageIcon(images.get("wR"))); // Example: Display white pawn
//        frame.add(label1, BorderLayout.CENTER);
//
//        frame.setVisible(true);
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        // Initialize colors for the chessboard pattern
//        Color[] colors = {Color.WHITE, Color.GRAY};
//
//        // Loop through each row and column to draw squares
//        for (int row = 0; row < DIMENSION; row++) {
//            for (int col = 0; col < DIMENSION; col++) {
//                Color color = colors[(row + col) % 2];
//                g.setColor(color);
//                g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
//            }
//        }
//    }
//    private void loadImages() {
//        String[] pieces = {"wp", "wR", "wN", "wB", "wK", "wQ", "bp", "bR", "bN", "bB", "bK", "bQ"};
//
//        for (String piece : pieces) {
//            try {
//                String imagePath = "D:\\CongTy\\ChessAndroid\\ChessServer\\src\\main\\java\\org\\example\\images\\"+ piece+".png";
//                BufferedImage image = ImageIO.read(new File(imagePath));
//                BufferedImage scaledImage = scaleImage(image, SQUARE_SIZE, SQUARE_SIZE);
//                images.put(piece, scaledImage);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private BufferedImage scaleImage(BufferedImage img, int width, int height) {
//        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//
//        Graphics2D g2d = scaledImage.createGraphics();
//        g2d.drawImage(tmp, 0, 0, null);
//        g2d.dispose();
//
//        return scaledImage;
//    }
//
//    public static void main(String[] args) {
////        SwingUtilities.invokeLater(ChessMain::new);
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Chessboard Example");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setResizable(false);
//
//            ChessMain boardDrawer = new ChessMain();
//            frame.add(boardDrawer);
//
//            frame.pack();
//            frame.setVisible(true);
//        });
//    }

//    private static final int DIMENSION = 8; // Number of rows and columns
//    private static final int SQUARE_SIZE = 50; // Size of each square in pixels
//
//    public ChessMain() {
//        this.setPreferredSize(new Dimension(DIMENSION * SQUARE_SIZE, DIMENSION * SQUARE_SIZE));
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        // Initialize colors for the chessboard pattern
//        Color[] colors = {Color.WHITE, Color.GRAY};
//
//        // Loop through each row and column to draw squares
//        for (int row = 0; row < DIMENSION; row++) {
//            for (int col = 0; col < DIMENSION; col++) {
//                Color color = colors[(row + col) % 2];
//                g.setColor(color);
//                g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Chessboard Example");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setResizable(false);
//
//            ChessMain boardDrawer = new ChessMain();
//            frame.add(boardDrawer);
//
//            frame.pack();
//            frame.setVisible(true);
//        });
//    }
}
