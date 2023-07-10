package Wordle.Shared.Data.Serializable;


import VitoBarra.JavaUtil.Data.IDataWithID;

import java.io.Serializable;

public class WordleGame implements IDataWithID<WordleGame>, Serializable {
    public final int GameID;
    private final String SecretWord;


    public WordleGame(int gameID, String secretWord) {
        GameID = gameID;
        SecretWord = secretWord;
    }

    @Override
    public int getID() {
        return GameID;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsById((WordleGame) obj);
    }

    public String getSecretWord() {
        return SecretWord;
    }
}
