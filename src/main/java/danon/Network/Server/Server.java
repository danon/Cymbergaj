package danon.Network.Server;

import com.google.common.collect.ImmutableList;
import danon.Network.Message.Message;
import danon.Network.Message.StartMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    public static final int PORT = 9801;

    private final List<ServerThread> serverThreads = new CopyOnWriteArrayList<>();
    private final ServerSocket serverSocket;
    private final ServerPanel panel;

    public static void main(String[] args) throws IOException {
        System.out.println("Binding to port " + PORT + ", please wait  ...");

        new Server(new ServerSocket(PORT)).start();
    }

    private Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.panel = new ServerPanel(this::closeServer);
    }

    private void start() {
        panel.showWindow();
        listenForConnections();
    }

    private void listenForConnections() {
        System.out.println("Server is Running...");
        try {
            ServerThread playerX = new ServerThread(this, serverSocket.accept());
            playerX.open();
            System.out.println("Accepted first " + playerX.toString());
            serverThreads.add(playerX);
            panel.updateList(ImmutableList.copyOf(serverThreads));

            ServerThread playerO = new ServerThread(this, serverSocket.accept());
            playerO.open();
            System.out.println("Accepted both" + playerO.toString());
            serverThreads.add(playerO);
            panel.updateList(ImmutableList.copyOf(serverThreads));

            playerX.send(0, new StartMessage('L'));
            playerO.send(0, new StartMessage('R'));

            playerX.start();
            playerO.start();
            System.out.println("Started game");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void handle(int ID, Message message) {
        serverThreads.forEach(serverThread -> serverThread.send(ID, message));
    }

    synchronized void removeClient(ServerThread toTerminate) {
        System.out.println("Removing client thread " + toTerminate.getID());
        serverThreads.remove(toTerminate);
        panel.updateList(ImmutableList.copyOf(serverThreads));
    }

    private void closeServer() {
        serverThreads.forEach(ServerThread::pleaseClose);
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
    }
}
