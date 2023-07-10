package Wordle.Shared.Data.Serializable;


import VitoBarra.JavaUtil.Data.IDataWithID;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class GameRecord implements IDataWithID<GameRecord>, Serializable {
    @Expose
    private final int GameRecordsID;
    @Expose
    private String UserName;
    @Expose
    private int GameID;
    @Expose
    private boolean won = false;
    @Expose
    private int Attempts;
    @Expose
    private Set<String> GuessedWord;

    @Expose(serialize = false, deserialize = false)

    private boolean IsClosed = false;
    @Expose(serialize = false, deserialize = false)
    int MaxAttempt;

    public GameRecord(int _gameRecordsID, String userName, int _gameID,int _maxAttemps) {
        GameRecordsID = _gameRecordsID;
        UserName = userName;
        GameID = _gameID;
        GuessedWord = new LinkedHashSet<>();
        MaxAttempt = _maxAttemps;
    }

    public boolean ContainsWord(String word) {
        return GuessedWord.contains(word);
    }

    public Boolean AddGuess(String s) {
        if (!IsClosed && HasAttempt() && GuessedWord.add(s)) {
            Attempts++;
            if (!HasAttempt()) IsClosed = true;
            return true;
        } else return false;
    }


    public Boolean HasAttempt() {
        return Attempts < MaxAttempt;
    }

    @Override
    public int getID() {
        return GameRecordsID;
    }

    public boolean equals(Object obj) {
        return EqualsById((GameRecord) obj);
    }


    public void SetToWin() {

        won = true;
        SetClosed();
    }

    public boolean haveWon() {
        return won;
    }

    public void SetClosed() {
        IsClosed = true;
    }

    public int getGameID() {
        return GameID;
    }

    public String getUserName() {
        return UserName;
    }

    public boolean isClosed() {
        return IsClosed;
    }

    public Set<String> getGuessedWord() {
        return GuessedWord;
    }

    public int getAttempts() {
        return Attempts;
    }
}
