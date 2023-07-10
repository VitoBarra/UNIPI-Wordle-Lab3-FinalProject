package Data.Config;

import java.io.Serializable;

public class ServerConfig implements Serializable {
    private String User_Filename = "User.json";
    private String GamesRecords_Filename = "GamesRecords.json";
    private String Vocabulary_Filename = "words.txt";
    private String WordleGame_Filename = "WordleGame.json";

    private int WelcomePort = 3838;
    private String MultiCastGroup = "230.36.37.30";
    private int MulticastPort = 5000;

    private int NewGameAfterMinutes = 5;

    private int MaxTry = 12;

    private int WelcomeTimeoutSeconds = 15;


    public String USER_FILENAME() {
        return User_Filename;
    }

    public String GAMES_FILENAME() {
        return GamesRecords_Filename;
    }

    public String VOCABULARY_FILENAME() {
        return Vocabulary_Filename;
    }

    public String WORDLEGAME_FILENAME() {
        return WordleGame_Filename;
    }

    public int WelcomePort() {
        return WelcomePort;
    }

    public String MultiCastGroup() {
        return MultiCastGroup;
    }

    public int MulticastPort() {
        return MulticastPort;
    }

    public int NewGameAfterMinutes() {
        return NewGameAfterMinutes;
    }

    public int MaxTry() {
        return MaxTry;
    }

    public int WelcomeTimeoutSeconds() {
        return WelcomeTimeoutSeconds * 1000;
    }
}
