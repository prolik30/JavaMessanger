package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Leonid on 25/11/15.
 */
public class Server {
    public static void main(String[] args) {

        System.out.println("start server");

        Chat chat = new Chat();
        new Thread(chat, "Chat").start();

        try {
            ServerSocket serverSocket = new ServerSocket(10000);

            //telnet localhost 10000

            System.out.println("Listening for incoming connection...");
            while(true) {
                Socket socket = serverSocket.accept();
                new Thread(new SocketHandler(socket, chat)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
