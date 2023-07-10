package Wordle.ClientSide.Client;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import Wordle.ClientSide.Client.StateMachine.ClientContext;
import Wordle.ClientSide.UI.ConsoleUI;

public class Client
{

    public Client()
    {
    }

    public void StartSession()
    {

        boolean ConnectionOpened = false;
        var config = ClientManager.GetConfig();
        var ConsoleUI = new ConsoleUI(config.getConfigUI());

        ConsoleUI.Clear();
        try (var server = new Socket(InetAddress.getByName(config.ServerIp()), config.ServerPort());
             var context = new ClientContext(ConsoleUI, server))
        {
            ConnectionOpened = true;
            while (context.IsConnectionAlive)
                context.getState().MenageOperation(context);
        } catch (IOException e)
        {

            if (ConnectionOpened)
                ConsoleUI.ServerUnexpectedlyClosed();
            else
                ConsoleUI.ServerWasClosed();

        }
    }


}
