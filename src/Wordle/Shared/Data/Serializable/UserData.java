package Wordle.Shared.Data.Serializable;


import VitoBarra.JavaUtil.Data.IDataWithID;
import Wordle.Shared.Data.PlayerStatistic;

import java.io.Serializable;
import java.util.List;

public class UserData implements IDataWithID<UserData>, Serializable {
    private final int UserID;
    String Name;
    String Password;
    //ignered in serialization
    PlayerStatistic Stat;

    public UserData(int _userID, String _name, String _password) {
        Name = _name;
        Password = _password;
        UserID = _userID;
    }

    public boolean ValidatePassword(String _pass) {
        return Password.equals(_pass);
    }

    public String getName() {
        return Name;
    }

    public PlayerStatistic SetStat(List<GameRecord> Games) {
        Stat = new PlayerStatistic(Games);
        return Stat;
    }

    public PlayerStatistic GetStat() {
        return Stat;
    }

    @Override
    public int getID() {
        return UserID;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsById((UserData) obj);
    }
}
