package org.example.server.Services;

import org.example.server.Models.ServerModel;
import org.example.server.Models.UserConnectionModel;
import net.jcip.annotations.ThreadSafe;

import java.net.Socket;

@ThreadSafe
public class UserConnectionService {

    private static final UserConnectionService instance = new UserConnectionService();
    private UserConnectionService() {}
    public static UserConnectionService get() {
        return instance;
    }

    public UserConnectionModel connection(Socket sc, ServerModel server) {
        return new UserConnectionModel(sc, server);
    }

    public void sendMessage(String message, UserConnectionModel conn) {
        conn.getUser().getActiveChart().addMessage(conn.getUser(), message);
        for(UserConnectionModel ucm : conn.getServer().getUserConns().asList()) {
            if (conn.getUser().getActiveChart().equals(ucm.getUser().getActiveChart())) {
                ucm.setNewMessage(message);
                System.out.println("setNewMessage");
            }
        }
    }

}
