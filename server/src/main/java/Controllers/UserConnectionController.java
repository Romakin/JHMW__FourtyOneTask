package Controllers;

import Models.UserConnectionModel;
import Repositories.MessagesRep;
import Services.LogService;
import Services.SettingsService;
import Services.UserConnectionService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class UserConnectionController implements Runnable {

    private UserConnectionModel conn;

    public UserConnectionController(UserConnectionModel conn) {
        this.conn = conn;
    }

    @Override
    public void run() {
        try(
                PrintWriter out = new PrintWriter(conn.getSocket().getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getSocket().getInputStream()))
        ) {

            while (!conn.getSocket().isClosed()) {
                if (conn.getServer().isSetToStop()) break;

                if (conn.getUser() == null) {
                    if (setNameAndEnter(out, in)) break;
                } else {

                    if (conn.getUser().getActiveChart() == null) {
                        if (chooseChart(out, in)) break;
                    } else {
                        try {
                            activitiesInChart(out, in);
                        } catch(Exception e) {}

                    }
                }

            }

            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            conn.getSocket().close();

            System.out.println("Closing connections & channels - DONE.");

        } catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
    }

    private boolean setNameAndEnter(PrintWriter out, BufferedReader in) throws IOException, InterruptedException {
        out.println("Enter your username or \"/exit\": \n");
        String inp = in.readLine();
        if ("/exit".equals(inp)) {
            log("<User Exit>");
            out.println("Goodbye! ");
            Thread.sleep(1000);
            return true;
        } else {
            if (!"".equals(inp.trim()) && conn.setUser(inp))
                log("<User Enter>");
            else out.println("Invalid username");
        }
        return false;
    }

    private boolean activitiesInChart(PrintWriter out, BufferedReader in) throws IOException {
        String inp = in.readLine();
        if ("/back".equals(inp)) {
            MessagesRep actChart = conn.getUser().getActiveChart();
            log("<User Leave Chart \"" + actChart.getChartName() + "\">");
            actChart.userLeave(conn.getUser());
            return true;
        }
        if ("/online".equals(inp)) {
            out.println(
                conn.getUser().getActiveChart().getOnlineUserNames() + "\n"
            );
            return false;
        }
        if ("/history".equals(inp)) {
            out.println(
                conn.getUser().getActiveChart().getAllMessages() + "\n"
            );
            return false;
        }
        if (!"/update".equals(inp)) {
            UserConnectionService.get().sendMessage(new String(inp.getBytes()), conn);
            log(inp);
        }
        List<String> msgs = conn.getNewMessages();
        if(!msgs.isEmpty()) {
            String outS = msgs.stream().collect(Collectors.joining("\n"));
            out.println(outS + "\n");
        } else {
            out.println("\n");
        }
        return false;
    }

    private boolean chooseChart(PrintWriter out, BufferedReader in) throws IOException, InterruptedException {
        out.println("Choose chart or put different name to create new: \n" +
                conn.getServer().getCharts().listToString());
        String inp = in.readLine();
        if ("/exit".equals(inp)) {
            log("<User Exit>");
            out.println("Goodbye! ");
            Thread.sleep(1000);
            return true;
        } else {
            if ("".equals(inp.trim())) return false;
            conn.getServer().getChart(inp).userEnter(conn.getUser());
            log("<User Entered Chart \"" + conn.getUser().getActiveChart().getChartName() + "\">");
            out.println("Welcome to \"" + inp + "\" chart! \n Put \"/back\" to go out. \n" );
        }
        return false;
    }

    private void log(String msg) {
        if(conn.getServer().isNeedLogging()) {
            System.out.println("log");
            String path = SettingsService.get().settings().getLogPath(),
                    username = conn.getUser() == null ? "undefined" : conn.getUser().getUsername(),
                    timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            LogService.log(path, "<" + username + " at " + timeStamp + "> " + msg);
        }
    }

}
