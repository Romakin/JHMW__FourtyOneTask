package org.example.server.Models;

import org.example.server.Repositories.ChartsRep;
import org.example.server.Repositories.MessagesRep;
import org.example.server.Repositories.UserConnectionRep;

import java.util.concurrent.atomic.AtomicBoolean;

public class ServerModel {

    private int port;
    private boolean needLogging;
    private ChartsRep charts;
    private UserConnectionRep userConns;
    private AtomicBoolean isSetToStop = new AtomicBoolean(false);

    public ServerModel(int port, boolean needLogging) {
        this.port = port;
        this.needLogging = needLogging;
        this.charts = new ChartsRep();
        this.userConns = new UserConnectionRep();
    }

    public ChartsRep getCharts() {
        return charts;
    }

    public int getPort() {
        return port;
    }

    public boolean isSetToStop() {
        return isSetToStop.get();
    }

    public void setToStop(boolean isSetToStop) {
        this.isSetToStop.set(isSetToStop);
    }

    public MessagesRep getChart(String name) {
        return this.charts.getOrCreate(name);
    }

    public UserConnectionRep getUserConns() {
        return userConns;
    }

    public boolean isNeedLogging() {
        return needLogging;
    }
}
