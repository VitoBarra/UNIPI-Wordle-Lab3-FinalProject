package Wordle.ClientSide.Client.StateMachine;


import Wordle.ClientSide.Client.WordleInteractionClient;
import Wordle.ClientSide.UI.ConsoleUI;
import Wordle.Shared.Data.Response.PreConditionMakeGuessResponse;

import java.io.IOException;

public class LoggedClient implements ClientState {

    String UserName;
    WordleInteractionClient WordleSessionLogger;
    ConsoleUI UI;

    public LoggedClient(String userName) {
        UserName = userName;
    }

    @Override
    public void MenageOperation(ClientContext context) throws IOException {
        UI = context.getUI();
        WordleSessionLogger = context.getMessageLogger();

        var e = UI.LoggedMenu(UserName);
        WordleSessionLogger.SendClientLoggedRequest(e);

        switch (e) {

            case MakeGuess -> MakeGuess();
            case VisualizeStat -> VisualizeStat();
            case ShareStat -> ShareStat();
            case VisualizeSharedStat -> UI.PrintReceivedStat(context.SharedStatReceiver.getSharedStatistics());
            case LogOut -> LogOut(context);
            case Close -> context.CloseConnection();
        }


    }

    private void LogOut(ClientContext context) {
        UI.LogOut();
        context.HandleLogOut();
        context.setState(new GuestClient());
    }

    private void ShareStat() throws IOException {
        var response = WordleSessionLogger.ReceiveSharedStatResponse();
        UI.ElaborateSharStatResponse(response);
    }

    private void VisualizeStat() throws IOException {
        var PlayerStat = WordleSessionLogger.ReceivePlayerStatistic();
        UI.PrintStat(PlayerStat);
    }

    private void MakeGuess() throws IOException {
        var response = WordleSessionLogger.ReceivePreConditionMakeGuess();
        var e = UI.PreMakeGuess(response);
        if (response.Code != PreConditionMakeGuessResponse.PreConditionCode.CanGuess) return;
        WordleSessionLogger.Sendln(e);
        var GuessResponse = WordleSessionLogger.ReceiveGuessResponse();
        UI.ElaborateGuessResponse(GuessResponse);
    }
}
