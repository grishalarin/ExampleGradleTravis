package RealExample;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WordFinderUserTest {
    public static final String FILE_ANY = "file://any";
    public static final String WORD = "";
    private WordFinderUser wordFinderUser;
    private WordFinder wordFinder = Mockito.mock(WordFinder.class);
    private final static String FIRST_SENTENCE = "Doing Test";
    private final static String SECOND_SENTENCE = "Doing Second Test";


    @BeforeEach
    void setUp() {
        wordFinderUser = new WordFinderUser(wordFinder);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void doWordNullTest() {
        when(wordFinder.getSentences(any())).thenReturn(null);
        assertDoesNotThrow(()->wordFinderUser.doWork(FILE_ANY, WORD));
    }

    @Test
    void doWorkEmptySet(){
        when(wordFinder.getSentences(any())).thenReturn(new HashSet<>());
        try {
            wordFinderUser.doWork(FILE_ANY, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder,times(0)).checkIfWordInSentence(any(),any());
        verify(wordFinder,times(0)).writeSentenceToResult(any());
    }

    @Test
    void doWorkIfTrueWriteSentence(){
        when(wordFinder.getSentences(any())).thenReturn(new HashSet<>(Arrays.asList(FIRST_SENTENCE,SECOND_SENTENCE)));
        when(wordFinder.checkIfWordInSentence(FIRST_SENTENCE,WORD)).thenReturn(true);
        when(wordFinder.checkIfWordInSentence(SECOND_SENTENCE,WORD)).thenReturn(false);
        try {
            wordFinderUser.doWork(FILE_ANY, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder, times(1)).writeSentenceToResult(FIRST_SENTENCE);
        verify(wordFinder,times(0)).writeSentenceToResult(SECOND_SENTENCE);
    }

    @Test
    void doWorkCheckUrl(){
        final ArgumentCaptor<URL> argumentCaptor = ArgumentCaptor.forClass(URL.class);
        try {
            wordFinderUser.doWork(FILE_ANY, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder.getSentences(argumentCaptor.capture()));
        assertEquals(FILE_ANY,argumentCaptor.getValue().toString());
    }

    @Test
    void doWorkBadUrl(){
        assertThrows(MalformedURLException.class,()->wordFinderUser.doWork("",""));
    }
}