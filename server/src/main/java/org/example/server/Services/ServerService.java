package org.example.server.Services;

import org.example.server.Controllers.UserConnectionController;
import org.example.server.Models.ServerModel;
import org.example.server.Models.SettingsModel;
import org.example.server.Models.UserConnectionModel;
import net.jcip.annotations.ThreadSafe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ThreadSafe
public class ServerService {

    private static final ServerService instance = new ServerService();
    private static ExecutorService executeIt;

    public static ServerService get() {
        return instance;
    }

    private ServerService() {
        executeIt = Executors.newCachedThreadPool();
    }

    public ServerModel server() {
        SettingsModel st = SettingsService.get().settings();
        return new ServerModel(st.getPort(), st.getNeedLogging());
    }


    public void startServer(ServerModel server) {
        try(ServerSocket serverSocket = new ServerSocket(server.getPort())) {

            System.out.println("Server socket created");

            while(!serverSocket.isClosed()) {
                if (server.isSetToStop()) {
                    System.out.println("Main Server is shutting down...");
                    serverSocket.close();
                    break;
                }

                UserConnectionModel conn = UserConnectionService.get().connection(serverSocket.accept(), server);
                addUserConnection(server, conn);
                executeIt.execute(new UserConnectionController(conn));

                System.out.println("Connection accepted.");
            }
            executeIt.shutdown();

        } catch (IOException e) {
            System.out.println("Server is already started");
        }
    }

    public void addUserConnection(ServerModel server, UserConnectionModel conn) {
        server.getUserConns().addConnection(conn);
    }

    /**
     * To stop Server we need to set AtomicBoolean & try to connect
     * @param server
     */
    public void stopServer(ServerModel server) {
        server.setToStop(true);
        try (Socket socket = new Socket("localhost", SettingsService.get().settings().getPort())
        ) {} catch (IOException ex) { System.out.println("Server is already down"); }
    }
}
