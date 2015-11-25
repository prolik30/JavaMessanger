package net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Leonid on 25/11/15.
 */
public class SocketHandler implements Runnable {

    public static final int N_THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
    static Executor sender = Executors.newFixedThreadPool(N_THREADS);
    private Socket socket;
    private Chat chat;
    private PrintWriter printWriter;

    public SocketHandler(Socket socket, Chat chat) throws IOException {
        this.socket = socket;
        this.chat = chat;
        chat.addClient(this);
        printWriter = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {

        try {
            System.out.println(socket);

            try (Scanner scanner = new Scanner(socket.getInputStream())) {
                    while (scanner.hasNextLine()) {
                        String message = scanner.nextLine();
                        chat.broadcast(this, message);
                    }
            }
            socket.close();

            System.out.println("finished " + socket);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            chat.removeClient(this);
        }
    }

    public void send(final String message) {

        sender.execute(new Runnable() {
            @Override
            public void run() {
                printWriter.println(message);
            }
        });

    }
}
