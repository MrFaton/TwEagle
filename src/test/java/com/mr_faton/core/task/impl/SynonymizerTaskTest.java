//package com.mr_faton.core.task.impl;
//
//import com.mr_faton.core.dao.MessageDAO;
//import com.mr_faton.core.dao.NotExistsSynonymDAO;
//import com.mr_faton.core.dao.SynonymDAO;
//import com.mr_faton.core.exception.NoSuchEntityException;
//import com.mr_faton.core.table.Message;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * Description
// *
// * @author Mr_Faton
// * @version 1.0
// * @since 16.10.2015
// */
//public class SynonymizerTaskTest {
//    private MessageDAO messageDAO;
//    private SynonymDAO synonymDAO;
//    private SynonymizerTask synonymizerTask;
//    private NotExistsSynonymDAO notExistsSynonymDAO;
//
//    @Before
//    public void setUp() {
//        messageDAO = mock(MessageDAO.class);
//        synonymDAO = mock(SynonymDAO.class);
//        notExistsSynonymDAO = mock(NotExistsSynonymDAO.class);
//        synonymizerTask = new SynonymizerTask(messageDAO, synonymDAO, notExistsSynonymDAO);
//    }
//
//
//    @Test
//    public void getWordList() {
//        String message = "Hi! This is test";
//        List<String> wordList = synonymizerTask.getWordList(message);
//        assertEquals("Hi!", wordList.get(0));
//        assertEquals("This", wordList.get(1));
//        assertEquals("is", wordList.get(2));
//        assertEquals("test", wordList.get(3));
//    }
//
//    @Test
//    public void recognizePunctuationMarksPosition() {
//        String word = "nice!))";
//        int punctuationMarkIndex = synonymizerTask.recognizePunctuationMarksPosition(word);
//        assertEquals(4, punctuationMarkIndex);
//    }
//
//    @Test
//    public void getPositionsOfPassableReplacement() throws NoSuchEntityException {
//        String message = "@Name This is message for, some increase!!) too test part_ofName";
//        List<String> wordList = new ArrayList<>();
//        Collections.addAll(wordList, message.split(" "));
//        Integer[] rightAnswers = {1, 3, 5, 6, 8};
//        List<Integer> positionsOfPassableReplacement = synonymizerTask.getPositionsOfPassableReplacement(wordList);
//
//        for (int i = 0; i < positionsOfPassableReplacement.size(); i++) {
//            assertEquals(rightAnswers[i], positionsOfPassableReplacement.get(i));
//        }
//    }
//
//    @Test
//    public void getRandomSynonym() {
//        List<String> synonymList = Arrays.asList("syn1", "syn2", "syn3");
//        String synonym = synonymizerTask.getRandomSynonym(synonymList);
//        assertTrue(synonymList.contains(synonym));
//    }
//
//    @Test
//    public void getReplacementsNumber() {
//        List<Integer> positionsOfPassableReplacements = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//        int actualReplaces = synonymizerTask.getReplacementsNumber(positionsOfPassableReplacements);
//        int minIndex = positionsOfPassableReplacements.size() * SynonymizerTask.MIN_SYN_PERCENT / 100;
//        int maxIndex = positionsOfPassableReplacements.size() * SynonymizerTask.MAX_SYN_PERCENT / 100;
//        assertTrue(actualReplaces >= minIndex && actualReplaces <= maxIndex);
//    }
//
//    @Test
//    public void replaceSynonyms() throws SQLException, NoSuchEntityException {
//        String text = "Today is a good and fine day for studying, its true!))";
//        int replacementsNumber = 3;
//        List<Integer> positionsOfPassableReplacements = new ArrayList<>();
//        Collections.addAll(positionsOfPassableReplacements, 0, 3, 5, 8, 10);
//        List<String> wordList = synonymizerTask.getWordList(text);
//
//        Synonym synonym = createSynonym();
//
//        when(synonymDAO.getSynonym(anyString())).thenReturn(synonym);
//
//        synonymizerTask.replaceSynonyms(replacementsNumber, positionsOfPassableReplacements, wordList);
//
//        int foundReplacements = 0;
//        for (String updatedWord : wordList) {
//            for (String availableSynonym : synonym.getSynonyms()) {
//                if (updatedWord.contains(availableSynonym)) {
//                    foundReplacements++;
//                    break;
//                }
//            }
//        }
//
//        assertEquals(replacementsNumber, foundReplacements);
//    }
//
//
//    @Test
//    public void execute() throws SQLException, NoSuchEntityException {
//        String text = "@Someone I think, that this summer was very nice)) and too, cool!";
//        Message message = new Message();
//        message.setOwner("Test");
//        message.setMessage(text);
//        message.setSynonymized(false);
//
//        Synonym synonymObj = createSynonym();
//
//        when(messageDAO.getUnSynonymizedMessages(anyInt())).thenReturn(Collections.singletonList(message));
//        when(synonymDAO.getSynonym(anyString())).thenReturn(synonymObj);
//
//        synonymizerTask.update();
//        synonymizerTask.execute();
//
//        boolean success = false;
//
//        String updatedText = message.getMessage();
//        for (String synonym : synonymObj.getSynonyms()) {
//            if (updatedText.contains(synonym) && message.isSynonymized()) {
//                success = true;
//                break;
//            }
//        }
//        assertTrue(success);
//    }
//
//    private Synonym createSynonym() {
//        Synonym synonym = new Synonym();
//
//        synonym.setWord("Test");
//        List<String> synonymList = Arrays.asList("syn1", "syn2", "syn3");
//        synonym.setSynonyms(synonymList);
//        synonym.setUsed(0);
//
//        return synonym;
//    }
//
//
////    @Test
////    public void execute() throws SQLException, NoSuchEntityException {
////        SynonymDAO synonymDAO = mock(SynonymDAO.class);
////        MessageDAO messageDAO = mock(MessageDAO.class);
////
////        List<String> synonymList = new ArrayList<>(4);
////        synonymList.add("synonym");
////        synonymList.add("synonym");
////        synonymList.add("synonym");
////
////        Message message = new Message();
////        message.setOwner("Test");
////        message.setMessage("@oone oone, ttwo)) tthree one");
////        List<Message> messageList = new ArrayList<>(2);
////        messageList.add(message);
////
////        when(synonymDAO.getSynonyms(anyString())).thenReturn(synonymList);
////        when(messageDAO.getUnSynonymizedMessages(anyInt())).thenReturn(messageList);
////
////        SynonymizerTask_Old synonymizerTask = new SynonymizerTask_Old(messageDAO, synonymDAO);
////
////        synonymizerTask.update();
////        synonymizerTask.execute();
////
////        Assert.assertEquals("@oone synonym, synonym)) synonym one", message.getMessage());
////    }
//
//
////    @Test
////    public void execute2() throws SQLException, NoSuchEntityException {
////        SynonymDAO synonymDAO = Mockito.mock(SynonymDAO.class);
////        MessageDAO messageDAO = Mockito.mock(MessageDAO.class);
////
////        List<String> oneList = new ArrayList<>(4);
////        oneList.add("oone+");
////        oneList.add("oone++");
////        oneList.add("oone+++");
////        List<String> twoList = new ArrayList<>(4);
////        twoList.add("ttwo+");
////        twoList.add("ttwo++");
////        twoList.add("ttwo+++");
////        List<String> threeList = new ArrayList<>(4);
////        threeList.add("tthree+");
////        threeList.add("tthree++");
////        threeList.add("tthree+++");
////
////        Message message = new Message();
////        message.setOwner("Test");
////        message.setMessage("oone, ttwo)) tthree one");
////        List<Message> messageList = new ArrayList<>(3);
////        messageList.add(message);
////
////        System.out.println(messageList);
////
////        Mockito.when(synonymDAO.getSynonyms("oone")).thenReturn(oneList);
////        Mockito.when(synonymDAO.getSynonyms("ttwo")).thenReturn(twoList);
////        Mockito.when(synonymDAO.getSynonyms("tthree")).thenReturn(threeList);
////
////        Mockito.when(messageDAO.getUnSynonymizedMessages(10)).thenReturn(messageList);
////
////        SynonymizerTask synonymizerTask = new SynonymizerTask(messageDAO, synonymDAO);
////
////        synonymizerTask.update();
////        synonymizerTask.execute();
////
////        System.out.println(messageList);
////    }
//}
