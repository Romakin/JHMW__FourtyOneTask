import Models.SettingsModel;
import Services.SettingsService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SettingsServiceTest {

    @Test
    public void testDefaultValues() {
        SettingsModel sm = SettingsService.get().settings();
        Assertions.assertTrue(
                sm.getNeedLogging() == true &&
                sm.getPort() == 8081 &&
                sm.getLogPath().equals("./logs/server.log")
                );
    }
}
