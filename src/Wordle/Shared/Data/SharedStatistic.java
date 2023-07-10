package Wordle.Shared.Data;

import Wordle.Shared.Data.HintedWord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class SharedStatistic implements Serializable {
    public String UserName;
    public List<HintedWord> HintedWords;


    public SharedStatistic(String userName, List<HintedWord> currentHintedWord) {
        UserName = userName;
        HintedWords = new LinkedList<>();

        for (var e : currentHintedWord)
            HintedWords.add(e.CreateBlankHint());
    }

    public static SharedStatistic CreateFromJson(String s) {
        if (s == null) return null;
        var JW = new Gson();
        Type Type = new TypeToken<SharedStatistic>() {
        }.getType();
        return JW.fromJson(s, Type);
    }


}
