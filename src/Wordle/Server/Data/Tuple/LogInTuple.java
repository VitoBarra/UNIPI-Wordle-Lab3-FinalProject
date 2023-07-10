package Wordle.Server.Data.Tuple;


import Wordle.Shared.Data.Response.LogInResponse;
import Wordle.Server.Session.LoggedUser;

public class LogInTuple
{
    public LogInResponse Response;
    public LoggedUser LoggedUser;


    public LogInTuple(LogInResponse response, LoggedUser loggedUser) {
        LoggedUser = loggedUser;
        Response =response;
    }
}
