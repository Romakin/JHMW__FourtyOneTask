import org.example.server.Services.LogService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class LogServiceTest {

    @Test
    public void testLog() throws IOException {
        String logPath = "./test.log", logStr = "test log";
        LogService ls = LogService.get(logPath);
        ls.log(logPath, logStr);
        File flLg = new File(logPath);
        String content = new String(Files.readAllBytes(flLg.toPath()), Charset.defaultCharset());
        Assertions.assertTrue(flLg.exists() && ls.getLogs(logPath).equals(content + "\n"));
    }
}
