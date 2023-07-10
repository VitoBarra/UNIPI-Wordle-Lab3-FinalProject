package Wordle.ClientSide.Data.Config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ConsoleUIConfig {

    private int PaddingSpace = 65;
    private char PaddingCharacter = '-';
    private int WordLength = 10;


    public ConsoleUIConfig() {
    }


    public int getPaddingSpace() {
        return PaddingSpace;
    }

    public char getPaddingCharacter() {
        return PaddingCharacter;
    }

    public int CalculateMiddle() {
        return (PaddingSpace - WordLength) / 2;
    }


    public static ConsoleUIConfig CreateFromJson(String s) {
        if (s == null) return null;
        var JW = new Gson();
        Type Type = new TypeToken<ConsoleUIConfig>() {
        }.getType();
        return JW.fromJson(s, Type);
    }
}
