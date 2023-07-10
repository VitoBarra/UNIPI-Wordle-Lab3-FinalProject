package Wordle.Server.Data.IO.Json;

import SwitchableDataSurce.Interfaces.IDataReader;
import SwitchableDataSurce.Interfaces.IDataSaver;
import VitoBarra.JavaUtil.IO.FileUtil;
import VitoBarra.JavaUtil.String.StringUtil;
import Wordle.Shared.Data.Serializable.GameRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonGameRecordsSerializer
        implements IDataReader<GameRecord>, IDataSaver<GameRecord>
{
    final String FileName;

    public JsonGameRecordsSerializer(String GameRecordsFileName) {
        FileName = GameRecordsFileName;
    }

    @Override
    public List<GameRecord> ReadDataList() {
        var JW = new Gson();
        Type WordleGame = new TypeToken<ArrayList<GameRecord>>() {
        }.getType();

        ArrayList<GameRecord> a = null;
        try {
            a = JW.fromJson(StringUtil.Stringer(FileUtil.ReadAllLine(FileName), ""), WordleGame);
        } catch (IOException e) {
            return a;
        }

        return a == null ? new ArrayList<>() : a;
    }

    @Override
    public void FlushData(List<GameRecord> DataList) {
        var JW = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        FileUtil.WriteFile(FileName, JW.toJson(DataList));
    }


}
