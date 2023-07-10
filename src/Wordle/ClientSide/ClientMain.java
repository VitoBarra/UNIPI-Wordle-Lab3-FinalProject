package Wordle.ClientSide;

import Wordle.ClientSide.Client.Client;
import Wordle.ClientSide.Client.ClientManager;

public class ClientMain {
    public static void main(String[] args) {
        new ClientManager();
        var e = new Client();
        e.StartSession();
    }
}