package org.example.server.Services;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.io.*;

/**
 * Возможные улучшения - выделить контроллер для логов и сделать там
 *  потокобезопасную очередь + поставить временной шедулер для разбора очереди в файл и запись.
 */
@ThreadSafe
public class LogService {

    private static final LogService instance = new LogService();
    @GuardedBy("this") private String logPath;

    private LogService() {}

    public synchronized void setLogPath(String logPath) {
        if (this.logPath == null) this.logPath = logPath;
    }

    public static LogService get(String logPath) {
        instance.setLogPath(logPath);
        return instance;
    }

    public static void log(String logPath, String log) {
        if(instance == null) get(logPath);
        if (!log.endsWith("\n")) log += "\n";
        saveToFile(logPath, log);
    }

    public static void saveToFile(String path, String log) {
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

    public static String getLogs(String path) {
        StringBuilder sb = new StringBuilder();
        try(FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            sb.append("\n");
        } catch (Exception e) { }
        return sb.toString();
    }
}
