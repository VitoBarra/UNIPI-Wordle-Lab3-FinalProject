package Wordle.ClientSide.Client;


import Wordle.ClientSide.Data.Config.ClientConfig;
import Wordle.ClientSide.Data.Manager.ClientConfigManager;

public class ClientManager {

    private static ClientManager instance;
    ClientConfigManager ConfigManager;

    public ClientManager() {
        instance = this;
        ConfigManager = new ClientConfigManager(ClientConfig.ConfigurationFactoryMethod("ClientConfig.json"));
    }

    static public ClientManager Ins() {
        if (instance == null)
            new ClientManager();
        return instance;
    }

    static public ClientConfig GetConfig() {
        return Ins().ConfigManager.GetConfig();
    }


}
