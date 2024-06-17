package org.example;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class SwingExample {

    private JFrame frame;
    private JLabel label;
    private JButton button;

    public SwingExample() {
        // Create and set up the frame
        frame = new JFrame("Swing Example");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a label and add it to the frame
        label = new JLabel("Hello, Swing!");
        frame.add(label);

        // Create a button and add an action listener
        button = new JButton("Click Me");
        button.addActionListener(e -> {
            label.setText("Button Clicked!");
        });
        frame.add(button);

        // Set layout manager (null layout for simplicity)
        frame.setLayout(null);

        // Position components manually (not recommended in practice)
        label.setBounds(20, 30, 150, 30);
        button.setBounds(20, 80, 100, 30);

        // Make the frame visible
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run the GUI construction on the Event-Dispatching Thread (EDT)
        SwingUtilities.invokeLater(() -> new SwingExample());
    }
}
