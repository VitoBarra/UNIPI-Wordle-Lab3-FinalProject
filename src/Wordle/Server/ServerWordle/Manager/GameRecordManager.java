package Wordle.Server.ServerWordle.Manager;


import SwitchableDataSurce.Interfaces.IMemoryListStrategy;
import VitoBarra.JavaUtil.Data.DataUtil;
import Wordle.Server.ServerWordle.ServerManager;
import Wordle.Shared.Data.Serializable.GameRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameRecordManager {
    int MaxGameRecordID;
    private final List<GameRecord> WordleGamesRecords;
    private final IMemoryListStrategy<GameRecord> GameRecordMemorization;

    public GameRecordManager(IMemoryListStrategy<GameRecord> mem) {
        GameRecordMemorization = mem;
        WordleGamesRecords = new CopyOnWriteArrayList<>(GameRecordMemorization.ReadList());
        MaxGameRecordID = DataUtil.FindMaxId(WordleGamesRecords);
    }


    public GameRecord getRecordIfExist(String userName, int gameId) {
        var recordAlreadyPresent = WordleGamesRecords.stream()
                .filter(x -> x.getGameID() == gameId && Objects.equals(x.getUserName(), userName))
                .findFirst().orElse(null);

        if (recordAlreadyPresent != null)
            recordAlreadyPresent.SetClosed();

        return recordAlreadyPresent;
    }

    public GameRecord GetRecordOrCreate(String userName, int gameId) {


        var recordAlreadyPresent = getRecordIfExist(userName, gameId);
        if (recordAlreadyPresent != null) return recordAlreadyPresent;

        var newRecord = new GameRecord(++MaxGameRecordID, userName, gameId, ServerManager.GetConfiguration().MaxTry());
        WordleGamesRecords.add(newRecord);
        GameRecordMemorization.AddOrModify(newRecord);
        return newRecord;

    }

    public List<GameRecord> GetRecordOfUser(String Username) {
        return new ArrayList<>(WordleGamesRecords.stream().filter(x -> x.getUserName().equals(Username)).toList());
    }


    void NotifyAll() {
        for (var e : WordleGamesRecords)
            GameRecordMemorization.AddOrModify(e);
    }

    void Notify(GameRecord e) {
        if (WordleGamesRecords.contains(e))
            GameRecordMemorization.AddOrModify(e);
    }

    public void Close() {
        NotifyAll();
        GameRecordMemorization.SaveAndClose();
    }
}
