package Wordle.Shared.Data;

import VitoBarra.JavaUtil.Data.DataUtil;
import Wordle.Shared.Data.Serializable.GameRecord;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayerStatistic {
    public int GamePlayed;
    public int GamedWon = 0;
    public int Streak = 0;
    public int MaxStreak = 0;

    public Map<Integer, Integer> WonDistribution;

    public PlayerStatistic(List<GameRecord> games) {

        if (games == null) return;
        games.sort(Comparator.comparingInt(GameRecord::getID));
        GamePlayed = games.size();
        for (var e : games)
            AddGame(e);
        WonDistribution = new HashMap<>();
        CalculateWonGuessDistribution(games);
    }

    private void AddGame(GameRecord e) {
        if (e.haveWon()) {
            GamedWon++;
            Streak++;
            if (MaxStreak < Streak)
                MaxStreak = Streak;
        } else
            Streak = 0;

    }

    private void CalculateWonGuessDistribution(List<GameRecord> games) {

        for (int i = 0; i < 12; i++)
            WonDistribution.put(i + 1, 0);

        var e = games.stream().filter(x -> x.haveWon()).toList();

        for (var i : e)
            DataUtil.CountOccurrence(WonDistribution, i.getAttempts(), 1);

    }


}
