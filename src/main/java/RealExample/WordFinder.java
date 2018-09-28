package RealExample;

import java.net.URL;
import java.util.Set;

public interface WordFinder {
    Set<String> getSentences(URL resources);
    boolean checkIfWordInSentence(String sentence, String Word);
    void writeSentenceToResult(String sentence);
}
