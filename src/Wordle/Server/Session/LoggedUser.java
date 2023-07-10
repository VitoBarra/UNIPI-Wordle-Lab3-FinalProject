package Wordle.Server.Session;

import Wordle.Server.ServerWordle.ServerManager;
import Wordle.Server.ServerWordle.SharedStatSender;
import Wordle.Server.ServerWordle.WordleCore;
import Wordle.Shared.Data.HintedWord;
import Wordle.Shared.Data.PlayerStatistic;
import Wordle.Shared.Data.Response.*;
import Wordle.Shared.Data.Serializable.*;
import Wordle.Shared.Data.SharedStatistic;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class LoggedUser {
    public UserData UserData;
    private GameRecord CurrentGame;

    public LoggedUser(UserData user) {
        UserData = user;
    }

    public PlayerStatistic GetStat() {

        List<GameRecord> games;
        var GameRecordsList = ServerManager.GameRecordManager().GetRecordOfUser(UserData.getName());

        if (CurrentGame == null || CurrentGame.isClosed())
            games = GameRecordsList;
        else
            games = GameRecordsList.stream().filter(x -> x.getID() != CurrentGame.getID())
                    .collect(Collectors.toCollection(LinkedList::new));

        return UserData.SetStat(games);
    }


    public List<HintedWord> CurrentHintedWord() {
        return ServerManager.GenerateHint(CurrentGame.getGuessedWord());
    }

    public WordleCore JoinCurrentGame() {
        var CurrentGameID = ServerManager.WordleGameManager().GetCurrentGameID();
        if (CurrentGame == null || CurrentGame.getGameID() != CurrentGameID)
            CurrentGame = ServerManager.GameRecordManager().GetRecordOrCreate(UserData.getName(), CurrentGameID);
        return ServerManager.WordleGameManager().getCurrentGame();

    }

    public boolean PlayedCurrentGame() {
        var CurrentGameID = ServerManager.WordleGameManager().GetCurrentGameID();
        if (CurrentGame != null && CurrentGame.getGameID() == CurrentGameID) return true;

        CurrentGame = ServerManager.GameRecordManager().getRecordIfExist(UserData.getName(), CurrentGameID);
        return CurrentGame != null;
    }

    public void ShareStat() {
        SharedStatSender.getInstance().Share(GenerateGameStatistic());
    }


    public SharedStatistic GenerateGameStatistic() {
        return new SharedStatistic(UserData.getName(), CurrentHintedWord());
    }

    public void LogOut() {
        if (CurrentGame != null)
            CurrentGame.SetClosed();
        ServerManager.UserManager().RemoveFromLogged(UserData);
    }

    public MakeGuessResponse MakeGuess(String word, WordleCore guestForGame) {
        return ServerManager.WordleGameManager().MakeGuess(word, CurrentGame, guestForGame);
    }

    public PreConditionMakeGuessResponse PreConditionGuess() {
        var e = new PreConditionMakeGuessResponse();

        if (CurrentGame.haveWon())
            e.Code = PreConditionMakeGuessResponse.PreConditionCode.AlreadyWon;
        else
            e.Code = CurrentGame.isClosed() ? PreConditionMakeGuessResponse.PreConditionCode.Closed : PreConditionMakeGuessResponse.PreConditionCode.CanGuess;

        e.Attempts = CurrentGame.getAttempts();
        e.hintedWords = CurrentHintedWord();
        e.MaxAttempt = ServerManager.GetConfiguration().MaxTry();

        return e;
    }

    public boolean IsCurrentGameClosed()
    {
        if(CurrentGame != null)
            return CurrentGame.isClosed();
        else return false;
    }
}
