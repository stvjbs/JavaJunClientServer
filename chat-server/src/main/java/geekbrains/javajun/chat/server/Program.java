package geekbrains.javajun.chat.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Program {
    public static void main(String[] args) throws IOException {  /// try\catch
        ServerSocket socket = new ServerSocket(5665);
        Server server = new Server(socket);
        server.runServer();
    }
}
