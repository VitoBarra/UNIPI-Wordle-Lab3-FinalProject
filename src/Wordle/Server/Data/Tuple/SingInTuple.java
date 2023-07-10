package Wordle.Server.Data.Tuple;


import Wordle.Shared.Data.Response.SingInResponse;
import Wordle.Server.Session.LoggedUser;

public class SingInTuple
{
    public SingInResponse Response;
    public LoggedUser LoggedUser;

    public SingInTuple(SingInResponse _response, LoggedUser loggedUser) {
        LoggedUser = loggedUser;
        Response = _response;
    }
}
