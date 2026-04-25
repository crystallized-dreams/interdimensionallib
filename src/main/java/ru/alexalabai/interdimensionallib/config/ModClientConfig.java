package ru.alexalabai.interdimensionallib.config;

import ru.alexalabai.interdimensionallib.common.ClientGameOptions;

public class ModClientConfig {
    public boolean crawlToggleable=false;
    public boolean sitOnBlockClick=false;

    /// FUNCTIONALITY ///
    public static ModClientConfig INSTANCE = new ModClientConfig();
    public void save() {
        crawlToggleable=ClientGameOptions.crawlToggle.getValue();
        sitOnBlockClick= ClientGameOptions.sitToggle.getValue();
        ConfigManager.saveClient(this);
    }
    public static ModClientConfig load() {return ConfigManager.loadClient();}
}
