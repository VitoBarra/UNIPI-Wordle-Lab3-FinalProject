package Wordle.Server.Data.IO.MemorizationFactory;

import Data.Config.ServerConfig;
import SwitchableDataSurce.Interfaces.IMemoryListStrategy;
import SwitchableDataSurce.Interfaces.IMemoryObjectStrategy;
import Wordle.Shared.Data.Serializable.*;

public interface AbstractMemorizationFactory {
    IMemoryListStrategy<GameRecord> CreateGameRecordMem(ServerConfig Config);

    IMemoryListStrategy<UserData> CreateUserDataMemorization(ServerConfig Config);

    IMemoryListStrategy<WordleGame> CreateWordleGameMem(ServerConfig Config);

    IMemoryObjectStrategy<ServerConfig> CreateConfigurationMem(String ConfigurationFile);

    IMemoryObjectStrategy<Vocabulary> CreateVocabularyMem(ServerConfig Config);
}
