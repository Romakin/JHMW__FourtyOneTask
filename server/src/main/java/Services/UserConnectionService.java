package Services;

import Models.ServerModel;
import Models.UserConnectionModel;

import java.net.Socket;

public class UserConnectionService {

    private static UserConnectionService instance;
    private UserConnectionService() {}
    public static UserConnectionService get() {
        if(instance == null) instance = new UserConnectionService();
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
