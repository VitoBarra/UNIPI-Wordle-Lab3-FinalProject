package Wordle.Server.Session.StateMachine;

import java.io.IOException;
import java.net.Socket;
import VitoBarra.JavaUtil.State.StateGenericContext;
import Wordle.Server.ServerWordle.WordleInteractionServer;

public class SessionContext extends StateGenericContext<SessionState>
{

    Socket ConnectionSocket;
    WordleInteractionServer ClientInterface;
    public boolean InteractionClose = false;


    public SessionContext(Socket _connectionSocket) throws IOException {
        ConnectionSocket = _connectionSocket;
        ClientInterface = new WordleInteractionServer(ConnectionSocket.getInputStream(), ConnectionSocket.getOutputStream());
        State = new GuestServer();
    }

    public void CloseInteraction() {
        InteractionClose = true;
        try {
            ConnectionSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WordleInteractionServer getClientInterface() {
        return ClientInterface;
    }
}
