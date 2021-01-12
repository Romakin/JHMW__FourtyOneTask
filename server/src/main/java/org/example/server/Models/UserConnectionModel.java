package org.example.server.Models;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class UserConnectionModel implements Comparable<UserConnectionModel> {

    private Socket socket;
    private ServerModel server;
    private UserModel user;
    private ArrayBlockingQueue<String> newMessages = new ArrayBlockingQueue(5000);

    public UserConnectionModel(Socket socket, ServerModel server) {
        this.socket = socket;
        this.server = server;
    }

    public Socket getSocket() {
        return socket;
    }

    public ServerModel getServer() {
        return server;
    }

    public boolean setUser(String username) {
        for(UserConnectionModel connU : this.server.getUserConns().asList()) {
            if (connU.getUser() != null && connU.getUser().getUsername().equals(username))
                return false;
        }
        this.user = new UserModel(username);
        return true;
    }

    public UserModel getUser() {
        return user;
    }

    public List<String> getNewMessages() {
        List<String> msgs = new ArrayList<>();
        newMessages.drainTo(msgs);
        return msgs;
    }

    public void setNewMessage(String newMsg) {
        this.newMessages.add(newMsg);
    }

    @Override
    public int compareTo(UserConnectionModel o) {
        return (this.user != null ? this.user.getUsername() : "").compareTo(
                o.getUser() != null ? o.getUser().getUsername() : ""
        );
    }
}
