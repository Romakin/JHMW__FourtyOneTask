package org.example.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.text.SimpleDateFormat;

import java.util.Date;

public class ClientSettings {
    private final String RESOURCE_NAME = "settings.json";
    private Integer port = null;
    private String logPath;

    public ClientSettings() { readSettings(); }

    public ClientSettings(int port, String logPath) {
        this.port = port;
        this.logPath = logPath;
    }

    public int getPort() {
        return port;
    }

    public ClientSettings setPort(int port) {
        if(this.port == null) this.port = port;
        return this;
    }

    public String getLogPath() {
        return logPath;
    }

    public void readSettings() {
        try (InputStream input = Client.class.getClassLoader().getResourceAsStream(RESOURCE_NAME)) {
            assert input != null;
            try (InputStreamReader inr = new InputStreamReader(input)) {
                LinkedTreeMap<String, Object> props = new Gson().fromJson(inr, LinkedTreeMap.class);
                this.port = ((Double) props.get("port")).intValue();
                this.logPath = (String) props.get("logPath");
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            System.err.println("Something goes wrong while reading settings");
        }
    }

    public void log(String log) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        log = "<" + timeStamp + "> " + log +"\n";
        saveToFile(logPath, log);
    }

    private void saveToFile(String path, String log) {
        File lf = new File(path);
        if (!(lf.exists() && lf.canRead() && lf.canWrite())) {
            try {
                new File(lf.getParent()).mkdirs();
                if (lf.createNewFile()) {
                    System.out.println("File created");
                } else {
                    return;
                }
            } catch (IOException e) {
                System.out.println("saveToFile: " + e.getMessage());
                return;
            }
        }
        try(FileOutputStream fos = new FileOutputStream(lf, true)) {
            fos.write(log.getBytes());
        } catch (IOException e) {
            System.out.println("saveToFile: " + e.getMessage());
        }
    }
}
