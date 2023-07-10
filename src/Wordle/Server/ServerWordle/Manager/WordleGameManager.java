package Wordle.Server.ServerWordle.Manager;

import SwitchableDataSurce.Interfaces.IMemoryListStrategy;
import VitoBarra.JavaUtil.Data.DataUtil;
import Wordle.Server.ServerWordle.ServerManager;
import Wordle.Server.ServerWordle.WordleCore;
import Wordle.Shared.Data.Response.MakeGuessResponse;
import Wordle.Shared.Data.Serializable.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WordleGameManager {
    int MaxWordleGameID;
    WordleCore WordleCurrentGame;
    List<WordleGame> WordleGames;
    IMemoryListStrategy<WordleGame> WordleGameMemorization;



    public WordleGameManager(IMemoryListStrategy<WordleGame> mem) {
        WordleGameMemorization = mem;
        WordleGames = new CopyOnWriteArrayList<>(WordleGameMemorization.ReadList());

        MaxWordleGameID = DataUtil.FindMaxId(WordleGames);
    }

    public String GetWordleGameByID(int id) {
        var t = WordleGames.stream().filter(x -> x.GameID == id).findFirst().orElse(null);
        return t != null ? t.getSecretWord() : "null";
    }

    public String CreateNewGame() {
        var newWord = ServerManager.VocabularyManager().GetRandomWord();
        var newWordleGame = new WordleGame(MaxWordleGameID + 1, newWord);
        WordleCurrentGame = new WordleCore(newWordleGame);
        WordleGames.add(newWordleGame);
        WordleGameMemorization.AddOrModify(newWordleGame);
        MaxWordleGameID++;
        return newWord;
    }

    public MakeGuessResponse MakeGuess(String word, GameRecord CurrentGameRecord, WordleCore WordlGame) {
        var response = CreateGuessResponse(word, CurrentGameRecord, WordlGame);

        switch (response.Code) {
            case Won -> {
                AddGuessToGameRecord(word, CurrentGameRecord);
                CurrentGameRecord.SetToWin();
            }
            case Valid -> AddGuessToGameRecord(word, CurrentGameRecord);
        }

        return response;
    }

    private MakeGuessResponse CreateGuessResponse(String word, GameRecord CurrentGameRecord, WordleCore WordlGame) {
        var e = new MakeGuessResponse();

        if (!CurrentGameRecord.HasAttempt()) {
            e.Code = MakeGuessResponse.GuessResponseCode.AttemptsEnded;
            return e;
        }
        if (!WordlGame.ValidateLength(word) || !ServerManager.VocabularyManager().IsInVocabulary(word)) {
            e.Code = MakeGuessResponse.GuessResponseCode.Invalid;
            return e;
        }

        if (CurrentGameRecord.ContainsWord(word)) {
            e.Code = MakeGuessResponse.GuessResponseCode.AlreadyTried;
            return e;
        }

        if (WordlGame.CheckWord(word))
            e.Code = MakeGuessResponse.GuessResponseCode.Won;
        else
            e.Code = MakeGuessResponse.GuessResponseCode.Valid;

        e.HintedWord = WordlGame.GenerateHint(word);

        return  e;
    }

    private static void AddGuessToGameRecord(String word, GameRecord CurrentGameRecord) {
        CurrentGameRecord.AddGuess(word);
        ServerManager.GameRecordManager().Notify(CurrentGameRecord);
    }

    public WordleCore getCurrentGame() {
        return WordleCurrentGame;
    }


    public int GetCurrentGameID() {
        return WordleCurrentGame.getWordleGame().getID();
    }



    public void Close() {
        WordleGameMemorization.SaveAndClose();
    }
}
