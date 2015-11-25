package net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Leonid on 25/11/15.
 */
public class Chat implements Runnable {
    List<SocketHandler> clients = new CopyOnWriteArrayList<SocketHandler>();
    BlockingQueue<Pair<SocketHandler, String> > messages = new LinkedBlockingQueue<Pair<SocketHandler, String>>();

    public void addClient(SocketHandler handler) {
        clients.add(handler);
    }

    public void removeClient(SocketHandler handler) {
        clients.remove(handler);
    }

    public void broadcast(SocketHandler exclude, String message) {
        messages.offer(new Pair<SocketHandler, String>(exclude, message));
    }

    private void realBroadcast(SocketHandler exclude, String message) {
        for(SocketHandler socketHandler : clients) {
            if(socketHandler != exclude) {
                socketHandler.send(message);
            }
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                Pair<SocketHandler, String> message = messages.take();
                realBroadcast(message.getFirst(), message.getSecond());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
