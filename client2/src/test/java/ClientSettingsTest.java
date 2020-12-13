import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;

public class ClientSettingsTest {

    private ClientSettings csDef;
    private ClientSettings cs;

    @Before
    public void initTest() {
        csDef = new ClientSettings();
        cs = new ClientSettings(8089, "./log_test.log");
    }

    @After
    public void clearTest() {
        File[] logFiles = new File[] {
                new File(csDef.getLogPath()),
                new File(cs.getLogPath())
        };
        for (File fl: logFiles)
            if (fl.exists()) fl.delete();
    }

    @Test
    public void testDefaultAutoSet() {
        Assertions.assertTrue(csDef.getPort() == 8081 && csDef.getLogPath().equals("./logs/clients/client2.log"));
    }

    @Test
    public void testLog() {
        cs.log("test log");
        Assertions.assertTrue(new File(cs.getLogPath()).exists());
    }

    @Test
    public void testDefaultLog() {
        csDef.log("test default log");
        Assertions.assertTrue(new File(csDef.getLogPath()).exists());
    }


}
