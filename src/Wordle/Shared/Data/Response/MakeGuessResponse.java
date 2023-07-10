package Wordle.Shared.Data.Response;

import Wordle.Shared.Data.HintedWord;

public class MakeGuessResponse {
    public HintedWord HintedWord;
    public GuessResponseCode Code;

    public enum GuessResponseCode {
        Valid,
        Invalid,
        Won,
        AttemptsEnded,
        AlreadyTried
    }
}
