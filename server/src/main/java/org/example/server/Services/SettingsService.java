package org.example.server.Services;

import org.example.server.Models.SettingsModel;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class SettingsService {

    private static final SettingsService instance = new SettingsService();
    public static SettingsService get() { return instance; }

    private SettingsService() {}

    public SettingsModel settings() {
        return new SettingsModel();
    }

    //ToDo save settings

}
