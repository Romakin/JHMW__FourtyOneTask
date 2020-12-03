import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {

    private final String RESOURCE_NAME = "settings.json";
    int port;
    String logPath;

    public Client() {
        readSettings();
    }

    public void start() {
        try (
                Socket socket = new Socket("localhost", port);
        ) {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                    Scanner scanner = new Scanner(System.in)
            ) {
                String msg;
                while (true) {

                    if (in.ready()) {
                        String line, response = "";
                        while ((line = in.readLine()) != null) {
                            if ("".equals(line.trim())) break;
                            response += line + "\n";
                        }
                        System.out.println(response);
                        log("<response> " + response);
                        msg = scanner.nextLine();
                        if ("end".equals(msg)) {
                            System.out.println("client shutdown");
                            break;
                        }
                        log("<request> " + msg);
                        out.println(msg);
                        out.flush();
                    }

                }
            }
        } catch (IOException ex) {
            System.out.println("Can not connect to this port");
        }
    }

    public void readSettings() {
        try (InputStream input = Client.class.getClassLoader().getResourceAsStream(RESOURCE_NAME);
             InputStreamReader inr = new InputStreamReader(input)) {
            LinkedTreeMap<String, Object> props = new Gson().fromJson(inr, LinkedTreeMap.class);
            this.port = ((Double) props.get("port")).intValue();
            this.logPath = (String) props.get("logPath");
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            System.err.println("Something goes wrong while reading settings");
        }
    }

    public void log(String log) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        log = "<" + timeStamp + "> " + log;
        saveToFile(logPath, log);
    }

    public void saveToFile(String path, String log) {
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