package Wordle.Server.Session.StateMachine;

import Wordle.Server.ServerWordle.WordleInteractionServer;
import Wordle.Server.Session.LoggedUser;
import Wordle.Shared.Data.Response.PreConditionMakeGuessResponse;
import Wordle.Shared.Data.Response.SharedStatResponse;

import java.io.IOException;

public class LoggedServer implements SessionState
{

    LoggedUser User;
    private WordleInteractionServer ClientInterface;


    LoggedServer(LoggedUser user) {
        User = user;
    }

    @Override
    public void MenageOperation(SessionContext context) throws IOException {

        ClientInterface = context.getClientInterface();
        try {
            switch (ClientInterface.ReceiveLoggedRequest()) {
                case MakeGuess -> MakeGuess();
                case VisualizeStat -> SendStat();
                case ShareStat -> ShareStat();
                case LogOut -> LogOut(context);
                case Close -> CloseInteraction(context);
            }
        } catch (IOException e) {
            CloseInteraction(context);
            throw e;
        }
    }


    private void CloseInteraction(SessionContext context) {
        ChangeUser();
        context.CloseInteraction();
    }

    private void ShareStat() {
        var response = CreateSharedResponse();
        ClientInterface.SendSharedStatResponse(response);
        if (response.code == SharedStatResponse.SharedStatCode.Shared)
            User.ShareStat();
    }

    private SharedStatResponse CreateSharedResponse() {
        var e = new SharedStatResponse();
        if (!User.PlayedCurrentGame())
            e.code = SharedStatResponse.SharedStatCode.DidntJoin;
        else if (!User.IsCurrentGameClosed())
            e.code = SharedStatResponse.SharedStatCode.AlreadyFinished;
        else
            e.code = SharedStatResponse.SharedStatCode.Shared;
        return e;
    }

    private void LogOut(SessionContext context) {
        ChangeUser();
        context.setState(new GuestServer());
    }

    private void ChangeUser() {
        User.LogOut();
        User = null;
    }

    private void SendStat() {
        ClientInterface.SendPlayerStatistic(User.GetStat());
    }

    private void MakeGuess() throws IOException {

        var GuestForGame = User.JoinCurrentGame();

        var preCon = User.PreConditionGuess();
        ClientInterface.SendPreConditionMakeGuess(preCon);
        if (preCon.Code != PreConditionMakeGuessResponse.PreConditionCode.CanGuess) return;

        var word = ClientInterface.ReadLine();
        ClientInterface.SendGuessResponse(User.MakeGuess(word, GuestForGame));
    }


}
