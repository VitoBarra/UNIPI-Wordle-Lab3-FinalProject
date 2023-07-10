package Wordle.Shared.Data.Serializable;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Vocabulary implements Serializable {
    final List<String> Words;


    public Vocabulary(List<String> _words) {
        Words = _words;
    }

    public String GetRandomWord() {
        var e = ThreadLocalRandom.current();
        return Words.get(e.nextInt(0, Words.size()));
    }

    public boolean IsInVocabulary(String str) {
        return Words.contains(str);
    }

    public boolean ValidateVocabulary()
    {
        return Words.size() > 0;
    }
}
