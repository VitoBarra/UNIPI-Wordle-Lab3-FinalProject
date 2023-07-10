package Wordle.Server.Data.IO.Json;

import SwitchableDataSurce.Interfaces.IDataReader;
import SwitchableDataSurce.Interfaces.IDataSaver;
import VitoBarra.JavaUtil.IO.FileUtil;
import VitoBarra.JavaUtil.String.StringUtil;
import Wordle.Shared.Data.PlayerStatistic;
import Wordle.Shared.Data.Serializable.UserData;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUserSerializer implements IDataReader<UserData>, IDataSaver<UserData>
{
    final String FileName;

    public JsonUserSerializer(String filename) {
        FileName = filename;
    }


    @Override
    public List<UserData> ReadDataList() {
        var JW = new Gson();
        Type WordleGame = new TypeToken<ArrayList<UserData>>() {
        }.getType();
        ArrayList<UserData> a = null;
        try {
            a = JW.fromJson(StringUtil.Stringer(FileUtil.ReadAllLine(FileName), ""), WordleGame);
        } catch (IOException e) {
            return a;
        }
        return a == null ? new ArrayList<>() : a;
    }

    @Override
    public void FlushData(List<UserData> UserList) {
        var JW = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return fieldAttributes.getDeclaredType() == new TypeToken<PlayerStatistic>() {
                }.getType();
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
        FileUtil.WriteFile(FileName, JW.toJson(UserList));
    }
}

