package org.example.server.Repositories;

import java.util.concurrent.ConcurrentSkipListSet;

public class ChartsRep {

    private ConcurrentSkipListSet<MessagesRep> charts;


    public ChartsRep() {
        this.charts = new ConcurrentSkipListSet<>();
    }

    public MessagesRep getOrCreate(String name) {
        for (MessagesRep chart: charts) {
            if (chart.getChartName().equals(name))
                return chart;
        }
        MessagesRep chart = new MessagesRep(name);
        if (charts.add(chart))
            return chart;
        else
            return null;
    }

    public String listToString() {
        StringBuilder sb = new StringBuilder();
        for (MessagesRep chart: charts) {
            sb.append(chart.getChartName() + "\n");
        }
        return sb.toString();
    }

}
