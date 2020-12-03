package Models;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SettingsModel {

    private final String RESOURCE_NAME = "settings.json";

    private int port = 8080;
    private boolean log = false;
    private String logPath = "";

    public SettingsModel() {
        trySetValuesWithSettings();
    }

    private void trySetValuesWithSettings() {
        try (InputStream input = SettingsModel.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);
             InputStreamReader inr = new InputStreamReader(input)) {
            LinkedTreeMap<String, Object> props = new Gson().fromJson(inr, LinkedTreeMap.class);
            this.port = ((Double) props.get("priorityServerPort")).intValue();
            this.log = (boolean) props.get("log");
            this.logPath = (String) props.get("logPath");
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            System.err.println("Something goes wrong while reading settings");
        }
    }

    public int getPort() {
        return port;
    }

    public String getLogPath() {
        return log ? logPath : null;
    }

    public boolean getNeedLogging() {
        return log;
    }
}
