package Wordle.Server.Data.IO.Json;

import Data.Config.ServerConfig;
import SwitchableDataSurce.Interfaces.IDataReader;
import SwitchableDataSurce.Interfaces.IDataSaver;
import VitoBarra.JavaUtil.IO.FileUtil;
import VitoBarra.JavaUtil.String.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonServerConfigSerializer implements IDataReader<ServerConfig>, IDataSaver<ServerConfig> {

    final String FileName;

    public JsonServerConfigSerializer(String filename) {
        FileName = filename;
    }

    @Override
    public List<ServerConfig> ReadDataList() {
        var JW = new Gson();
        Type serverConfigs = new TypeToken<ArrayList<ServerConfig>>() {
        }.getType();
        ArrayList<ServerConfig> a = null;
        try {
            a = JW.fromJson(StringUtil.Stringer(FileUtil.ReadAllLine(FileName), ""), serverConfigs);
        } catch (IOException e) {
            return a;
        }
        return a == null ? new ArrayList<>() : a;
    }

    @Override
    public void FlushData(List<ServerConfig> DataList) {
        var JW = new Gson();
        FileUtil.WriteFile(FileName, JW.toJson(DataList));
    }

}
