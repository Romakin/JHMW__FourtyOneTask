package Services;

import java.io.*;

/**
 * Возможные улучшения - выделить контроллер для логов и сделать там
 *  потокобезопасную очередь + поставить временной шедулер для разбора очереди в файл и запись.
 */
public class LogService {

    private static volatile LogService instance;
    private static String logPath;

    private LogService(String logPath) {
        this.logPath = logPath;
    }

    public static LogService get(String logPath) {
        LogService local = instance;
        if (local == null) {
            synchronized (LogService.class) {
                local = instance;
                if (local == null) local = instance = new LogService(logPath);
            }
        }
        return local;
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
