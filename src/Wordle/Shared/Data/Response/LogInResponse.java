package Wordle.Shared.Data.Response;


public class LogInResponse {
    public LogInResponseCode Code;
    public String Data;




    public enum LogInResponseCode {
        Logged,
        AlreadyLogged,
        WrongPassword,
        UserDontExist,
    }
}
