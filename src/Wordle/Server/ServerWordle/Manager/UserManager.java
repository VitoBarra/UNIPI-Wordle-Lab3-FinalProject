package Wordle.Server.ServerWordle.Manager;

import SwitchableDataSurce.Interfaces.IMemoryListStrategy;
import VitoBarra.JavaUtil.Data.DataUtil;
import Wordle.Server.Data.Tuple.LogInTuple;
import Wordle.Server.Data.Tuple.SingInTuple;
import Wordle.Server.ServerWordle.ServerManager;
import Wordle.Server.Session.LoggedUser;
import Wordle.Shared.Data.Response.LogInResponse;
import Wordle.Shared.Data.Response.SingInResponse;
import Wordle.Shared.Data.Serializable.UserData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserManager {
    int MaxUserID;

    Map<Integer, UserData> CurrentLoggedUser ;
    List<UserData> RegisteredUser;
    public static IMemoryListStrategy<UserData> UserMemorization;

    public UserManager(IMemoryListStrategy<UserData> mem) {
        UserMemorization = mem;
        RegisteredUser = new CopyOnWriteArrayList<>(UserMemorization.ReadList());
        MaxUserID = DataUtil.FindMaxId(RegisteredUser);
        CurrentLoggedUser = new ConcurrentHashMap<>();
    }

    private boolean CheckNameAvailability(String name) {
        return RegisteredUser.stream().noneMatch(x -> x.getName().equals(name));
    }

    public SingInTuple SignIn(String name, String Password) {

        var response = GenerateSingInResponse(name, Password);


        if (response.Code != SingInResponse.LogInResponseCode.Logged) return new SingInTuple(response,null);

        response.Data = ServerManager.GenerateLoginMessage(name);

        var user = new UserData(++MaxUserID, name, Password);
        RegisteredUser.add(user);
        UserMemorization.AddOrModify(user);

        AddToLogged(user);
        return new SingInTuple(response, new LoggedUser(user));
    }

    private SingInResponse GenerateSingInResponse(String name, String Password) {
        var e = new SingInResponse();

        if (!CheckNameAvailability(name))
            e.Code = SingInResponse.LogInResponseCode.AlreadyExist;
        else if (Password.isBlank())
            e.Code = SingInResponse.LogInResponseCode.PasswordEmpty;
        else
            e.Code = SingInResponse.LogInResponseCode.Logged;

        return e;
    }

    public LogInTuple LogIn(String name, String Password) {

        var user = RegisteredUser.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElse(null);

        var response = CreateLoginResponse(Password, user);

        if (response.Code != LogInResponse.LogInResponseCode.Logged)
            return new LogInTuple(response, null);


        AddToLogged(user);
        response.Data = ServerManager.GenerateLoginMessage(user.getName());
        return new LogInTuple(response, new LoggedUser(user));

    }

    private static LogInResponse CreateLoginResponse(String Password, UserData user) {
        var e = new LogInResponse();

        if (user == null)
            e.Code = LogInResponse.LogInResponseCode.UserDontExist;
        else if (!user.ValidatePassword(Password))
            e.Code = LogInResponse.LogInResponseCode.WrongPassword;

        else if (ServerManager.UserManager().IsUserLogged(user))
            e.Code = LogInResponse.LogInResponseCode.AlreadyLogged;

        else
            e.Code = LogInResponse.LogInResponseCode.Logged;
        return e;

    }

    public boolean IsUserLogged(UserData user) {
        return CurrentLoggedUser.get(user.getID()) != null;
    }

    public void AddToLogged(UserData data) {
        if (!IsUserLogged(data))
            CurrentLoggedUser.put(data.getID(), data);
    }

    public void RemoveFromLogged(UserData data) {
        CurrentLoggedUser.remove(data.getID());
    }

    public void Close() {
        UserMemorization.SaveAndClose();
    }
}
