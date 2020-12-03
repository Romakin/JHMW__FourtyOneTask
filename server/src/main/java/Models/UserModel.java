package Models;

import Repositories.MessagesRep;

public class UserModel implements Comparable<UserModel> {

    private final String username;
    private MessagesRep chart;

    public UserModel(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public MessagesRep getActiveChart() {
        return chart;
    }

    public void setChart(MessagesRep chart) {
        this.chart = chart;
    }


    @Override
    public int compareTo(UserModel o) {
        return this.username.compareTo(o.getUsername());
    }
}
