package Wordle.Shared.Data.Response;



public class SharedStatResponse {
    public SharedStatCode code;

    public enum SharedStatCode {
        DidntJoin,
        AlreadyFinished,
        Shared
    }
}
