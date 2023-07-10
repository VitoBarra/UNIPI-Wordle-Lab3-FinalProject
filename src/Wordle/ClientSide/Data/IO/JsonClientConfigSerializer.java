package Wordle.ClientSide.Data.IO;

import SwitchableDataSurce.Interfaces.IDataReader;
import SwitchableDataSurce.Interfaces.IDataSaver;
import VitoBarra.JavaUtil.IO.FileUtil;
import VitoBarra.JavaUtil.String.StringUtil;
import Wordle.ClientSide.Data.Config.ClientConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//Classe che organizza la lettura della configurazione con l utiliza di SwitchableDataSource
public class JsonClientConfigSerializer implements IDataReader<ClientConfig>, IDataSaver<ClientConfig>
{

    final String FileName;

    public JsonClientConfigSerializer(String filename) {
        FileName = filename;
    }

    @Override
    public List<ClientConfig> ReadDataList() {
        var JW = new Gson();
        Type Configuration = new TypeToken<ArrayList<ClientConfig>>() {
        }.getType();
        ArrayList<ClientConfig> a = null;

        try {
            a = JW.fromJson(StringUtil.Stringer(FileUtil.ReadAllLine(FileName), ""), Configuration);
        } catch (IOException e) {
            return new ArrayList<>();
        }
        return a == null ? new ArrayList<>() : a;
    }

    @Override
    public void FlushData(List<ClientConfig> DataList) {
        var JW = new Gson();
        FileUtil.WriteFile(FileName, JW.toJson(DataList));
    }
}
