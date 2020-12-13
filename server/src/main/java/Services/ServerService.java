package Services;

import Controllers.UserConnectionController;
import Models.ServerModel;
import Models.SettingsModel;
import Models.UserConnectionModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerService {

    private static volatile ServerService instance;
    private static ExecutorService executeIt;

    private ServerService() {
        executeIt = Executors.newCachedThreadPool();
    }

    public static ServerService get() {
        ServerService local = instance;
        if (local == null) {
            synchronized (ServerService.class) {
                local = instance;
                if (local == null) local = instance = new ServerService();
            }
        }
        return local;
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
