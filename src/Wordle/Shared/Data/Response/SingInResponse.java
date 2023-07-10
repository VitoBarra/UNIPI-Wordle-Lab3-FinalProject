package Wordle.Shared.Data.Response;


public class SingInResponse {
    public LogInResponseCode Code;
    public String Data;


    public enum LogInResponseCode {
        Logged,
        AlreadyExist,
        PasswordEmpty
    }
}
