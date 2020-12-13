package Services;

import Models.SettingsModel;

public class SettingsService {

    private static volatile SettingsService instance;
    private SettingsService() {}
    public static SettingsService get() {
        SettingsService local = instance;
        if (local == null) {
            synchronized (SettingsService.class) {
                local = instance;
                if (local == null) local = instance = new SettingsService();
            }
        }
        return local;
    }

    public SettingsModel settings() {
        return new SettingsModel();
    }

    //ToDo save settings

}
