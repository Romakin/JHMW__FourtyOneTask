import org.example.client.Client;
import org.example.client.ClientSettings;
import org.example.client.ClientsRep;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ClientsRepTest {

    ClientsRep clients = new ClientsRep();
    int defaultPort;
    String defaultLogPath;

    @Before
    public void init() {

        String client1 = "test 1",
                client2 = null;
        defaultPort = Integer.parseInt(clients.getDefaultPort());
        defaultLogPath = clients.getDefaultLogPathTemp();
        clients = new ClientsRep(new String[]{ client1, client2});
    }

    @Test
    public void testCheckClients() {
        List<Client> clList = clients.getClients();

        ClientSettings[] cls = new ClientSettings[] {
                new ClientSettings(defaultPort, defaultLogPath),
                new ClientSettings()
        };

        Assert.assertEquals(cls.length, clList.size());

        for (int i = 0; i < cls.length; i++) {
            Assert.assertEquals(clList.get(i).getSettings().getPort(), cls[i].getPort());
            Assert.assertEquals(clList.get(i).getSettings().getLogPath(), cls[i].getLogPath());
        }

        Assert.assertNotEquals(clList.get(0).getInStream(), System.in);
        Assert.assertEquals(clList.get(1).getInStream(), System.in);
    }
}
