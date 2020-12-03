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

    private static ServerService instance;
    private static ExecutorService executeIt;

    private ServerService() {
        executeIt = Executors.newCachedThreadPool();
    }

    public static ServerService get() {
        if(instance == null) instance = new ServerService();
        return instance;
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
                server.getUserConns().addConnection(conn);
                executeIt.execute(new UserConnectionController(conn));

                System.out.println("Connection accepted.");
            }
            executeIt.shutdown();

        } catch (IOException e) {
            System.out.println("Server is already started");
        }
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
