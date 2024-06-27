package org.example;

import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Server");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.setVisible(true);

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            textArea.append("Server started on port 12345\n");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, textArea);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private JTextArea textArea;

        public ClientHandler(Socket socket, JTextArea textArea) {
            this.socket = socket;
            this.textArea = textArea;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                String message;
                while ((message = in.readLine()) != null) {
                    textArea.append("Received: " + message + "\n");
                    broadcastMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler clientHandler : clientHandlers) {
                if (clientHandler != this) {
                    clientHandler.out.println(message);
                }
            }
        }
    }
}
