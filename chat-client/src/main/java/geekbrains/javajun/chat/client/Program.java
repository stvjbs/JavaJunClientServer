package geekbrains.javajun.chat.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) throws IOException { // try\catch
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите ваше имя: ");
        String name = scanner.nextLine();
        InetAddress address = InetAddress.getLocalHost();
        Socket socket = new Socket(address, 5665);
        Client client = new Client(socket, name);

        InetAddress inetAddress = socket.getInetAddress();
        System.out.println("InetAddress: " + inetAddress);
        String remoteIP = inetAddress.getHostAddress();
        System.out.println("RemoteIP: " + remoteIP);
        System.out.println("LocalPort:" + socket.getLocalPort());

        client.listenForMessage();
        client.sendMessage();
    }
}
