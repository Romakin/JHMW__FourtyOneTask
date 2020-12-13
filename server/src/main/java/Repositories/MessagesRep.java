package Repositories;

import Models.UserModel;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class MessagesRep implements Comparable<MessagesRep> {

    private final String chartName;
    private ConcurrentLinkedQueue<String> messages = new ConcurrentLinkedQueue();
    private ConcurrentSkipListSet<UserModel> users = new ConcurrentSkipListSet<>();

    public MessagesRep(String chartName) {
        this.chartName = chartName;
    }

    public String getChartName() {
        return chartName;
    }

    public void userEnter(UserModel user) {
        this.users.add(user);
        user.setChart(this);
    }

    public void userLeave(UserModel user) {
        this.users.remove(user);
        user.setChart(null);
    }

    public void addMessage(UserModel user, String msg) {
        messages.offer(user.getUsername() + ": " + msg);
    }

    public String getAllMessages() {
        StringBuilder sb = new StringBuilder();
        for(String msg : messages) {
            sb.append(msg + "{\\n}");
        }
        return sb.toString();
    }

    public String getOnlineUserNames() {
        StringBuilder sb = new StringBuilder();
        for(UserModel user : users) {
            sb.append(user.getUsername() + ";");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessagesRep that = (MessagesRep) o;
        return Objects.equals(chartName, that.chartName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chartName, messages.size(), users.size());
    }

    @Override
    public int compareTo(MessagesRep o) {
        return this.chartName.compareTo(o.getChartName());
    }
}
