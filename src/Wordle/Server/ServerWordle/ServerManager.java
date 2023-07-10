package Wordle.Server.ServerWordle;

import Data.Config.ServerConfig;
import Wordle.Server.Data.IO.MemorizationFactory.AbstractMemorizationFactory;
import Wordle.Shared.Data.HintedWord;

import java.util.List;
import java.util.Set;

import Wordle.Server.ServerWordle.Manager.*;
import Wordle.Shared.Protocol.InteractionProtocol;

public class ServerManager {
    private static ServerManager instance;

    boolean Valid = false;


    public UserManager UserManager;
    public WordleGameManager WordleGameManager;
    public GameRecordManager GameRecordManager;

    public ServerConfigManager ConfigManager;

    public VocabularyManager VocabularyManager;


    public ServerManager(AbstractMemorizationFactory MemFactory) {
        instance = this;

        ConfigManager = new ServerConfigManager(MemFactory.CreateConfigurationMem("ServerConfig.json"));
        var e = ConfigManager.GetConfig();

        VocabularyManager = new VocabularyManager(MemFactory.CreateVocabularyMem(e));
        if (!VocabularyManager.ValidateVocabulary()) return;

        WordleGameManager = new WordleGameManager(MemFactory.CreateWordleGameMem(e));
        UserManager = new UserManager(MemFactory.CreateUserDataMemorization(e));
        GameRecordManager = new GameRecordManager(MemFactory.CreateGameRecordMem(e));

        Valid = true;
    }

    static public ServerManager Ins() {
        return instance;
    }

    static public GameRecordManager GameRecordManager() {
        return Ins().GameRecordManager;
    }

    static public WordleGameManager WordleGameManager() {
        return Ins().WordleGameManager;
    }

    static public UserManager UserManager() {
        return Ins().UserManager;
    }

    static public VocabularyManager VocabularyManager() {
        return Ins().VocabularyManager;
    }

    static public ServerConfigManager ConfigManager() {
        return Ins().ConfigManager;
    }

    static public ServerConfig GetConfiguration() {
        return ConfigManager().GetConfig();
    }

    public static String GenerateLoginMessage(String user) {
        return InteractionProtocol.LoginMessage(user,
                GetConfiguration().MultiCastGroup(),
                GetConfiguration().MulticastPort());
    }

    public static String NewWordleGame() {
        return WordleGameManager().CreateNewGame();
    }


    public static List<HintedWord> GenerateHint(Set<String> e) {
        return WordleGameManager().getCurrentGame().GenerateHint(e);
    }

    public static boolean IsValid() {
        return Ins().Valid;
    }


    private boolean ValidateManager() {
        return Ins().VocabularyManager.ValidateVocabulary();
    }

    public static void Close() {
        if (Ins().Valid) {
            UserManager().Close();
            WordleGameManager().Close();
            GameRecordManager().Close();
        }

        ConfigManager().Close();
        VocabularyManager().Close();
    }
}
