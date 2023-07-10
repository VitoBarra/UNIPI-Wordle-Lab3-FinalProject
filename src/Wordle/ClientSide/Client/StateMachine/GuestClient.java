package Wordle.ClientSide.Client.StateMachine;

import Wordle.ClientSide.Client.StateMachine.ClientContext;
import Wordle.ClientSide.Client.WordleInteractionClient;
import Wordle.ClientSide.UI.ConsoleUI;
import Wordle.Shared.Data.Response.LogInResponse;
import Wordle.Shared.Data.Response.SingInResponse;

import java.io.IOException;

public class GuestClient implements ClientState {

    WordleInteractionClient WordleSessionLogger;
    ConsoleUI UI;

    @Override
    public void MenageOperation(ClientContext context) throws IOException {
        UI = context.getUI();
        WordleSessionLogger = context.getMessageLogger();

        var e = UI.GuessMenu();
        WordleSessionLogger.SendClientGuestRequest(e);
        switch (e) {
            case LogIn -> LogIn(context);
            case SignIn -> SingIn(context);
            case Close -> context.CloseConnection();
        }

    }

    private void LogIn(ClientContext context) throws IOException {
        WordleSessionLogger.SendUserAndPassword(UI.getUserName(), UI.getPassword());


        var response = WordleSessionLogger.ReceiveLogInResponse();
        UI.ElaborateLogInResponse(response);
        if (response.Code != LogInResponse.LogInResponseCode.Logged) return;

        var username = context.HandleLogin(response.Data);
        context.setState(new LoggedClient(username));
    }

    private void SingIn(ClientContext context) throws IOException {
        WordleSessionLogger.SendUserAndPassword(UI.getUserName(), UI.getPassword());

        var response = WordleSessionLogger.ReceiveSingInResponse();
        UI.ElaborateSingInResponse(response);
        if (response.Code != SingInResponse.LogInResponseCode.Logged) return;

        var username = context.HandleLogin(response.Data);
        context.setState(new LoggedClient(username));
    }
}
