import org.example.server.Models.ServerModel;
import org.example.server.Models.UserConnectionModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class UserConnectionModelTest {
    String username = "testUser";
    ServerModel sm;
    UserConnectionModel ucm;

    @Before
    public void init() {
        sm = new ServerModel(-1, false);
        ucm = new UserConnectionModel(null, sm);
    }

    @Test
    public void getUserByUsername() {
        boolean userAdded = ucm.setUser(username);
        sm.getUserConns().addConnection(ucm);
        boolean userAlreadyExists = ucm.setUser(username);

        Assertions.assertTrue(userAdded);
        Assertions.assertFalse(userAlreadyExists);
    }

    @Test
    public void messageSending() {
        String msg = "test";
        ucm.setNewMessage(msg);
        List<String> msgs = ucm.getNewMessages();
        Assertions.assertEquals(msgs.size(), 1);
        Assertions.assertEquals(msgs.get(0), msg);
    }
}
