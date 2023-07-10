package Wordle.Shared.Data.Response;


import Wordle.Shared.Data.HintedWord;

import java.util.List;

public class PreConditionMakeGuessResponse {
    public PreConditionCode Code;

    public int Attempts;
    public int MaxAttempt;
    public List<HintedWord> hintedWords;


    public PreConditionMakeGuessResponse() {

    }

    public enum PreConditionCode {
        AlreadyWon,
        Closed,
        CanGuess,
    }

}
