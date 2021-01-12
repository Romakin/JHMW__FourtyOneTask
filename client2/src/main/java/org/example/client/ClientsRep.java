package org.example.client;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class ClientsRep {

    private List<Client> clients = new ArrayList<>();

    private final String defaultPort = "8081";
    private String defaultLogPathTemp = "./logs/clients/client{i}.log";

    public void setDefaultLogPathTemp(String defaultLogPathTemp) {
        this.defaultLogPathTemp = defaultLogPathTemp;
    }

    public String getDefaultLogPathTemp() {
        return  defaultLogPathTemp.replaceFirst("\\{i\\}", String.valueOf(clients.size() + 1));
    }

    public String getDefaultPort() {
        return defaultPort;
    }

    public ClientsRep() {}

    public ClientsRep(String[] clientsCommands) {
        for (String cmd: clientsCommands)
            addClient(cmd);
    }

    public void addClient(String commands) {
        Client client = commands != null ?
            new Client( new String[] {
                    defaultPort,
                    getDefaultLogPathTemp()
            }) :
                new Client(new String[]{});
        if (commands != null) client.setInStream(new ByteArrayInputStream(commands.getBytes()));
        clients.add(client);
    }

    public List<Client> getClients() {
        return clients;
    }
}
