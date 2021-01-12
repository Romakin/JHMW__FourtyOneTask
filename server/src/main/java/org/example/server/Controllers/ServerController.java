package org.example.server.Controllers;

import org.example.server.Models.ServerModel;
import org.example.server.Services.LogService;
import org.example.server.Services.ServerService;
import org.example.server.Services.SettingsService;

import java.io.*;
import java.util.Scanner;

public class ServerController implements Runnable {

    private final OutputStream out;
    private final InputStream in;
    private final ServerModel server;

    public ServerController(OutputStream out, InputStream in) {
        this.out = out;
        this.in = in;
        this.server = ServerService.get().server();
    }


    @Override
    public void run() {
        boolean exit = false;
        try (
                PrintWriter outPW = new PrintWriter(new OutputStreamWriter(out), true);
                Scanner sc = new Scanner(in)
        ) {
            while (true) {
                outPW.println("Choose menu number:\n1. start server\n2. stop server\n3.show logs\n4.exit\n");
                String inp = sc.nextLine();
                int item = 0;
                try {
                    item = Integer.parseInt(inp);
                } catch (NumberFormatException ex) {}
                switch (item) {
                    case 1:
                        new Thread(() -> {
                           ServerService.get().startServer(server);
                        }, "ServerTestStart").start();
                        outPW.println("started\n");
                        break;
                    case 2:
                        ServerService.get().stopServer(server);
                        outPW.println("SetToStop\n");
                        break;
                    case 3:
                        outPW.println(
                            LogService.getLogs(SettingsService.get().settings().getLogPath())
                        );
                        break;
                    case 4:
                        exit = true;
                        break;
                }
                if (exit) break;
            }
        }
    }

}
