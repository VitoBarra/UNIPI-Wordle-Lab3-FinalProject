package Wordle.Server.Session;


import java.io.IOException;
import java.net.Socket;
import Wordle.Server.Session.StateMachine.SessionContext;

public class Session implements Runnable {
    SessionContext SessionContext;

    public Session(Socket accept) throws IOException {
        SessionContext = new SessionContext(accept);
    }

    @Override
    public void run() {
        while (!SessionContext.InteractionClose) {
            try {
                SessionContext.getState().MenageOperation(SessionContext);
            } catch (IOException e) {
                System.out.println("\nConnection cosed unexpectedly\n>");
            }
        }
    }

}
