package Wordle.Server.Data.IO.Json;

import SwitchableDataSurce.Interfaces.IDataReader;
import SwitchableDataSurce.Interfaces.IDataSaver;
import VitoBarra.JavaUtil.IO.FileUtil;
import VitoBarra.JavaUtil.String.StringUtil;
import Wordle.Shared.Data.Serializable.WordleGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonWodleGameSerializer
        implements IDataReader<WordleGame>, IDataSaver<WordleGame>
{
    final String FileName;

    public JsonWodleGameSerializer(String fileName) {
        FileName = fileName;
    }


    @Override
    public List<WordleGame> ReadDataList() {
        var JW = new Gson();
        Type WordleGame = new TypeToken<ArrayList<WordleGame>>() {
        }.getType();

        ArrayList<WordleGame> a = null;

        try {
            a = JW.fromJson(StringUtil.Stringer(FileUtil.ReadAllLine(FileName), ""), WordleGame);
        } catch (IOException e) {
            return new ArrayList<>();
        }

        return a == null ? new ArrayList<>() : a;
    }

    @Override
    public void FlushData(List<WordleGame> DataList) {
        var JW = new Gson();
        FileUtil.WriteFile(FileName, JW.toJson(DataList));
    }

}
