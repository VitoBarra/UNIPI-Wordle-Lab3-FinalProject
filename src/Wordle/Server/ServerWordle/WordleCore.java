package Wordle.Server.ServerWordle;


import Wordle.Shared.Data.HintedWord;
import Wordle.Shared.Data.Serializable.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WordleCore {
    private final WordleGame WordleGame;
    private final String[] SecretWordArray;
    private final Set<String> CharSet;

    public WordleCore(WordleGame _currentWord) {
        WordleGame = _currentWord;
        SecretWordArray = WordleGame.getSecretWord().split("");
        CharSet = new HashSet<>(List.of(SecretWordArray));
    }


    public List<HintedWord> GenerateHint(Iterable<String> WordList) {
        var HintList = new LinkedList<HintedWord>();
        for (var str : WordList)
            HintList.add(GenerateHint(str));
        return HintList;
    }

    public HintedWord GenerateHint(String Word) {

        HintedWord hintedWord = new HintedWord(Word);


        if (!ValidateLength(Word)) return null;

        var WordArr = Word.split("");
        for (int i = 0; i < SecretWordArray.length; i++) {
            var curChar = WordArr[i];
            if (CharSet.contains(curChar))
                hintedWord.UpdateStatusHint(i, SecretWordArray[i], curChar);
        }
        return hintedWord;
    }

    public boolean ValidateLength(String Word) {
        if (Word == null) return false;
        return Word.split("").length == SecretWordArray.length;
    }


    public boolean CheckWord(String str) {
        return WordleGame.getSecretWord().equals(str);
    }

    public WordleGame getWordleGame() {
        return WordleGame;
    }
}
