package Wordle.ClientSide.Client.StateMachine;

import Wordle.ClientSide.Client.WordleInteractionClient;
import VitoBarra.JavaUtil.State.StateGenericContext;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import Wordle.ClientSide.Client.*;
import Wordle.ClientSide.UI.ConsoleUI;
import Wordle.Shared.Protocol.InteractionProtocol;

public class ClientContext extends StateGenericContext<ClientState> implements Closeable {

    ConsoleUI UI;
    WordleInteractionClient MessageLogger;
    SharedStatReceiver SharedStatReceiver;

    public boolean IsConnectionAlive = true;

    public ClientContext(ConsoleUI ui, Socket Socket) throws IOException {
        State = null;
        UI = ui;
        MessageLogger = new WordleInteractionClient(Socket.getInputStream(), Socket.getOutputStream());

    }


    public ConsoleUI getUI() {
        return UI;
    }

    public WordleInteractionClient getMessageLogger() {
        return MessageLogger;
    }


    public String HandleLogin(String LoginData) {
        var Data = LoginData.split(InteractionProtocol.IpDivider);
        var e = Data[0];
        SharedStatReceiver = new SharedStatReceiver(Data[0], Data[1], Integer.parseInt(Data[2]), 8);
        return e;
    }

    public void CloseConnection() {
        IsConnectionAlive = false;
    }

    public void HandleLogOut() {
        if (SharedStatReceiver == null) return;
        SharedStatReceiver.close();
        SharedStatReceiver = null;
    }

    @Override
    public void close() {
        HandleLogOut();

        try {
            MessageLogger.close();
            UI.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
