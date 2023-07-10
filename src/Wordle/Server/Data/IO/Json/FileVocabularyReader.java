package Wordle.Server.Data.IO.Json;


import SwitchableDataSurce.Interfaces.IDataReader;
import VitoBarra.JavaUtil.IO.FileUtil;
import Wordle.Shared.Data.Serializable.Vocabulary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileVocabularyReader implements IDataReader<Vocabulary>
{
    final String FileName;

    public FileVocabularyReader(String fileName)
    {
        FileName = fileName;
    }


    @Override
    public List<Vocabulary> ReadDataList()
    {
        var e = new ArrayList<Vocabulary>(1);
        try
        {
            e.add(new Vocabulary(FileUtil.ReadAllLine(FileName)));
        } catch (IOException ex)
        {
            return e;
        }
        return e;
    }
}
