package Wordle.Server.Data.IO.MemorizationFactory;

import Data.Config.ServerConfig;
import SwitchableDataSurce.DefaultImplementation.DataIOManager;
import SwitchableDataSurce.DefaultImplementation.ObjectMemorization;
import SwitchableDataSurce.DefaultImplementation.RawMemorization;
import SwitchableDataSurce.Interfaces.IMemoryListStrategy;
import SwitchableDataSurce.Interfaces.IMemoryObjectStrategy;
import Wordle.Server.Data.IO.Json.*;
import Wordle.Shared.Data.Serializable.*;
import SwitchableDataSurce.Builder.*;

public class JsonMemorizationFactory implements AbstractMemorizationFactory
{


    public IMemoryListStrategy<GameRecord> CreateGameRecordMem(ServerConfig Config) {
        var GameRecords = new JsonGameRecordsSerializer(Config.GAMES_FILENAME());
        var mem = new RawMemorization<>(new DataIOManager<>(GameRecords, GameRecords));
        return new AutoSaverBuilder<GameRecord>().setRate(10).setName("GameRecord").createAutoSaver(mem);
    }

    public IMemoryListStrategy<UserData> CreateUserDataMemorization(ServerConfig Config) {
        var User = new JsonUserSerializer(Config.USER_FILENAME());
        var mem = new RawMemorization<>(new DataIOManager<>(User, User));
        return new AutoSaverBuilder<UserData>().setRate(10).setName("UserData").createAutoSaver(mem);
    }

    public IMemoryListStrategy<WordleGame> CreateWordleGameMem(ServerConfig Config) {
        var WordleGame = new JsonWodleGameSerializer(Config.WORDLEGAME_FILENAME());
        var mem = new RawMemorization<>(new DataIOManager<>(WordleGame, WordleGame));
        return new AutoSaverBuilder<WordleGame>().setRate(10).setName("WordleGame").createAutoSaver(mem);
    }

    public IMemoryObjectStrategy<ServerConfig> CreateConfigurationMem(String ConfigurationFile) {
        var Conf = new JsonServerConfigSerializer(ConfigurationFile);
        return new ObjectMemorization<>(new RawMemorization<>(new DataIOManager<>(Conf, Conf)));
    }

    public IMemoryObjectStrategy<Vocabulary> CreateVocabularyMem(ServerConfig Config) {
        var Vocabulary = new FileVocabularyReader(Config.VOCABULARY_FILENAME());
        return new ObjectMemorization<>(new RawMemorization<>(new DataIOManager<>(null, Vocabulary)));
    }
}
