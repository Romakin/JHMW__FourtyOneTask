package Services;

import Models.SettingsModel;

public class SettingsService {

    private static SettingsService instance;
    private SettingsService() {}
    public static SettingsService get() {
        if(instance == null) instance = new SettingsService();
        return instance;
    }

    public SettingsModel settings() {
        return new SettingsModel();
    }

    //ToDo save settings

}
