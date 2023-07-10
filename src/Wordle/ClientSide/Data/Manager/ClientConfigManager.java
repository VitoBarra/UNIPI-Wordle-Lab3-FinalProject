package Wordle.ClientSide.Data.Manager;


import SwitchableDataSurce.Interfaces.IMemoryObjectStrategy;
import Wordle.ClientSide.Data.Config.ClientConfig;

public class ClientConfigManager {
    ClientConfig config;

    IMemoryObjectStrategy<ClientConfig> ConfigurationMemorization;


    public ClientConfigManager(IMemoryObjectStrategy<ClientConfig> mem) {
        ConfigurationMemorization = mem;
        config = ConfigurationMemorization.ReadObject();
        if (config != null) return;
        config = new ClientConfig();
        ConfigurationMemorization.AddOrModify(config);
        ConfigurationMemorization.Save();

    }

    public ClientConfig GetConfig() {
        return config;
    }
}
