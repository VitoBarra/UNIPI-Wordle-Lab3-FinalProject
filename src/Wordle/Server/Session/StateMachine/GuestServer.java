package Wordle.Server.Session.StateMachine;


import Wordle.Server.ServerWordle.ServerManager;
import Wordle.Server.ServerWordle.WordleInteractionServer;
import Wordle.Server.Session.LoggedUser;

import java.io.IOException;

public class GuestServer implements SessionState {

    private WordleInteractionServer ClientInterface;

    @Override
    public void MenageOperation(SessionContext context) throws IOException {

        ClientInterface = context.getClientInterface();
        try {
            switch (ClientInterface.ReceiveGuestRequest()) {
                case LogIn -> SignIn(TryLogIn(), context);
                case SignIn -> SignIn(TrySingIn(), context);
                case Close -> CloseInteraction(context);
            }
        } catch (IOException e) {
            CloseInteraction(context);
            throw e;
        }

    }

    void CloseInteraction(SessionContext context) {
        context.CloseInteraction();
    }

    private void SignIn(LoggedUser LoggedUser, SessionContext context) {
        if (LoggedUser != null)
            context.setState(new LoggedServer(LoggedUser));
    }


    private LoggedUser TryLogIn() throws IOException {
        var e = ServerManager.UserManager().LogIn(ClientInterface.ReadLine(), ClientInterface.ReadLine());
        ClientInterface.SendLogInResponse(e.Response);
        return e.LoggedUser;
    }

    private LoggedUser TrySingIn() throws IOException {

        var e = ServerManager.UserManager().SignIn(ClientInterface.ReadLine(), ClientInterface.ReadLine());
        ClientInterface.SendRegisterResponse(e.Response);
        return e.LoggedUser;
    }

}
