package geekbrains.javajun.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientManager implements Runnable {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String name;


    public static ArrayList<ClientManager> clients = new ArrayList<>();

    public ClientManager(Socket socket) {
        try {
            this.socket = socket;
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clients.add(this);
            name = bufferedReader.readLine();
            System.out.println(name + " joined to chat.");
            broadcastMessage("Server: " + name + " joined to chat.");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClient();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient() {
        clients.remove(this);
        System.out.println(name + " покинул чат.");
        try {
            broadcastMessage("Server: " + name + " disconnect");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void broadcastMessage(String message) throws IOException {
        if (privateMessageFinder(message) != null) {
            for (ClientManager client : clients) {
                if (client.name.equals(privateMessageFinder(message))) {
                    bufWriter(message, client);
                    return;
                }
            }
            System.out.println("No user " + privateMessageFinder(message));
        } else {
            for (ClientManager client2 : clients) {
                if (!client2.name.equals(this.name) && message != null) {
                    bufWriter(message, client2);
                }
            }
        }
    }

    private void bufWriter(String message, ClientManager client) throws IOException {
        client.bufferedWriter.write(message);
        client.bufferedWriter.newLine();
        client.bufferedWriter.flush();
    }

    private String privateMessageFinder(String message) {
        String[] wordsOfMessage = message.split(" ");
        if (wordsOfMessage[1].charAt(0) == '@') {
            return wordsOfMessage[1].replace('@', ' ').trim();
        }
        return null;
    }

    @Override
    public void run() {
        String messageFromClient;
        while (!socket.isClosed()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
}




