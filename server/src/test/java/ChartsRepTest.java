import org.example.server.Models.UserModel;
import org.example.server.Repositories.ChartsRep;
import org.example.server.Repositories.MessagesRep;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class ChartsRepTest {

    ChartsRep charts;
    UserModel user;

    @Before
    public void init() {
        charts = new ChartsRep();
        user = new UserModel("user1");
        charts.getOrCreate("first");
        charts.getOrCreate("second");
    }

    @Test
    public void testGetOrCreate() {
        MessagesRep first =  charts.getOrCreate("first");
        Assertions.assertEquals(first, charts.getOrCreate("first"));
    }

    @Test
    public void testListToString() {
        Assertions.assertEquals("first\nsecond\n", charts.listToString());
    }

    @Test
    public void userEnterTest() {
        MessagesRep first =  charts.getOrCreate("first");
        first.userEnter(user);
        Assertions.assertEquals(first.getOnlineUserNames(), user.getUsername() + ";");
    }

    @Test
    public void addMsg() {
        String message = "Hello!",
                expectedString = user.getUsername() + ": " + message + "{\\n}";
        MessagesRep second =  charts.getOrCreate("second");
        second.userEnter(user);
        second.addMessage(user, message);
        Assertions.assertEquals(second.getAllMessages(), expectedString);
    }

    @Test
    public void userLeaveTest() {
        MessagesRep first =  charts.getOrCreate("first");
        first.userLeave(user);
        Assertions.assertTrue("".equals(first.getOnlineUserNames()));
    }
}
