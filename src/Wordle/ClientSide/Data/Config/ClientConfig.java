package Wordle.ClientSide.Data.Config;

import SwitchableDataSurce.DefaultImplementation.DataIOManager;
import SwitchableDataSurce.DefaultImplementation.ObjectMemorization;
import SwitchableDataSurce.DefaultImplementation.RawMemorization;
import SwitchableDataSurce.Interfaces.IMemoryObjectStrategy;
import Wordle.ClientSide.Data.IO.JsonClientConfigSerializer;

public class ClientConfig {

    private String ServerIp = "127.0.0.1";
    private int ServerPort = 3838;
    private int TimedOutStatReceiverSeconds = 15;

    private ConsoleUIConfig configUI;

    public ClientConfig() {
        configUI = new ConsoleUIConfig();
    }

    public ConsoleUIConfig getConfigUI() {
        return configUI;
    }

    public String ServerIp() {
        return ServerIp;
    }

    public int ServerPort() {
        return ServerPort;
    }


    public static IMemoryObjectStrategy<ClientConfig> ConfigurationFactoryMethod(String s) {
        var ClientConf = new JsonClientConfigSerializer(s);
        return new ObjectMemorization<>(new RawMemorization<>(new DataIOManager<>(ClientConf, ClientConf)));
    }

    public int StatReceiverTimeOutSeconds() {
        return TimedOutStatReceiverSeconds *1000;
    }
}
