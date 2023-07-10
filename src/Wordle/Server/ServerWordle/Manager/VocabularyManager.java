package Wordle.Server.ServerWordle.Manager;

import SwitchableDataSurce.Interfaces.IMemoryObjectStrategy;
import Wordle.Shared.Data.Serializable.*;
public class VocabularyManager {
    final Vocabulary Vocabulary;
    IMemoryObjectStrategy<Vocabulary> VocabularyMemorization;

    public VocabularyManager(IMemoryObjectStrategy<Vocabulary> mem) {

        VocabularyMemorization = mem;
        Vocabulary = VocabularyMemorization.ReadObject();
    }

    public boolean ValidateVocabulary()
    {
        if(Vocabulary == null)
        {
            System.out.println("\nVocabulary file missing");
            return false;
        }
        var valid = Vocabulary.ValidateVocabulary();
        if (!valid) {
            System.out.println("\nVocabulary Empty supply a file with at least one word");
            System.out.println("\nif the vocabulary isn't empty check in configuration file if the file name is correct");
        }
        return valid;
    }

    public void Close() {
    }

    public String GetRandomWord() {
        return Vocabulary.GetRandomWord();
    }

    public boolean IsInVocabulary(String word) {
        return Vocabulary.IsInVocabulary(word);
    }
}
