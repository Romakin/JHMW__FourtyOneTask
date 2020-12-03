package Repositories;

import Models.UserConnectionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class UserConnectionRep {

    private ConcurrentSkipListSet<UserConnectionModel> connections;

    public UserConnectionRep() {
        this.connections = new ConcurrentSkipListSet<>();
    }

    public boolean addConnection(UserConnectionModel connection) {
        return connections.add(connection);
    }

    public List<UserConnectionModel> asList() {
        UserConnectionModel[] ucm = connections.toArray(new UserConnectionModel[]{});
        return ucm.length > 0 ? Arrays.asList(ucm) : new ArrayList<>();
    }
}
