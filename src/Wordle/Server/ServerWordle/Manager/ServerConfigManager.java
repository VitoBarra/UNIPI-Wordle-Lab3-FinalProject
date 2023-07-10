package Wordle.Server.ServerWordle.Manager;

import Data.Config.ServerConfig;
import SwitchableDataSurce.Interfaces.IMemoryObjectStrategy;

public class ServerConfigManager {
    ServerConfig config;

    IMemoryObjectStrategy<ServerConfig> ConfigurationMemorization;


    public ServerConfigManager(IMemoryObjectStrategy<ServerConfig> mem) {
        ConfigurationMemorization = mem;

        config = ConfigurationMemorization.ReadObject();
        if (config != null) return;
        config = new ServerConfig();
        ConfigurationMemorization.AddOrModify(config);
        ConfigurationMemorization.Save();

    }

    public ServerConfig GetConfig() {
        return config;
    }


    public void Close() {
        ConfigurationMemorization.SaveAndClose();
    }
}
