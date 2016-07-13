package danon.Network;

import java.net.*;
import java.io.*;

public class Client implements Runnable {
    private final Socket socket;
    private Thread thread;
    private ObjectOutputStream streamOut;
    private ClientThread clientThread;
    private BufferedReader console;

    public static void main(String[] args) throws IOException {
        System.out.println("Establishing connection. Please wait ...");
        Client client = new Client(new Socket(InetAddress.getLocalHost(), Server.PORT));
        client.start();
    }

    private Client(Socket socket) {
        this.socket = socket;
    }

    private void start() throws IOException {
        console = new BufferedReader(new InputStreamReader(System.in));
        streamOut = new ObjectOutputStream(socket.getOutputStream());

        clientThread = new ClientThread(this, socket);
        clientThread.open();
        clientThread.start();

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()) {
            try {
                streamOut.writeObject(new TextMessage(console.readLine()));
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
                finnish();
            }
        }
    }

    void handle(String msg) {
        if (msg.equals(".bye")) {
            System.out.println("Good bye. Press RETURN to exit ...");
            finnish();
        } else
            System.out.println(msg);
    }

    void finnish() {
        thread.interrupt();

        try {
            if (console != null) console.close();
            if (streamOut != null) streamOut.close();
            if (socket != null) socket.close();
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
        clientThread.interrupt();
        clientThread.close();
    }
}
