import org.example.client.Client;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class ClientTest {

    Client client;

    @Before
    public void initTest() {
        client = new Client(new String[] {"1111", "./logs/test/ClientTest.log"});
    }

    @After
    public void clearTest() {
        if (client != null) {
            File lgFile = new File(client.getSettings().getLogPath());
            if (lgFile.exists())
                lgFile.delete();
        }
    }

    @Test
    public void testIO() throws IOException {
        String commands = "test";
        InputStream in = new ByteArrayInputStream(commands.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true);
        client.setInStream(in);
        client.setOutStream(out);
        client.readInput(new BufferedReader(new InputStreamReader(in)));

        Assert.assertTrue(baos.toString().trim().equals(commands));
    }
}
