package danon.Cymbergaj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        System.out.println("Server is Running...");
        try (ServerSocket listener = new ServerSocket(8901)) {
            while (true) {
                Player playerX = new Player(listener.accept(), 'L');
                Player playerO = new Player(listener.accept(), 'R');

                playerX.start();
                playerO.start();
            }
        }
    }

    private static class Player extends Thread {
        final char mark;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + mark);
                output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        public void run() {
            try {
                output.println("MESSAGE All players connected");

                while (true) {
                    String command = input.readLine();
                    if (command.startsWith("KEYS")) {
                        output.println(command);
                    } else if (command.startsWith("MOVE")) {

                    } else if (command.startsWith("QUIT")) {
                        return;
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
