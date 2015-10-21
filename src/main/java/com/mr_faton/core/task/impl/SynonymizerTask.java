package com.mr_faton.core.task.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.NotExistsSynonymDAO;
import com.mr_faton.core.dao.SynonymDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.table.Synonym;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.RandomGenerator;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 12.10.2015
 */
public class SynonymizerTask implements Task{
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.task.impl.SynonymizerTask");
    public static final int MESSAGE_LIMIT = 10; //how many messages select from db
    public static final int MIN_DELAY = 1 * 60 * 1000; //minutes
    public static final int MAX_DELAY = 2 * 60 * 1000; //minutes
    public static final int MIN_SYN_PERCENT = 20;
    public static final int MAX_SYN_PERCENT = 60;
    public static final int MIN_SYN_WORD_LENGTH = 3; //if the word length less or equals it, word not synonymized

    private final MessageDAO messageDAO;
    private final SynonymDAO synonymDAO;
    private final NotExistsSynonymDAO notExistsSynonymDAO;

    private boolean status = true;
    private long nextTime = 0;
    private List<Message> messagesForSynonymize; //messages who will be synonymized
    private List<Message> synonymizedMessages = new ArrayList<>(MESSAGE_LIMIT + 1); //synonymized messages
    private boolean hasMoreMessages = true; //if db says that no messages for synonymized, then turn it to false



    public SynonymizerTask(MessageDAO messageDAO, SynonymDAO synonymDAO, NotExistsSynonymDAO notExistsSynonymDAO) {
        logger.debug("constructor");
        this.messageDAO = messageDAO;
        this.synonymDAO = synonymDAO;
        this.notExistsSynonymDAO = notExistsSynonymDAO;
    }


    /**
     * Method return work status.
     * @return Work status of task.
     */
    @Override
    public boolean getStatus() {
        return status;
    }

    /**
     * Change task's work status.
     * @param status for change task's work status (true - task is enable, fals - task is disable).
     */
    @Override
    public void setStatus(boolean status) {
        logger.info("status changed to " + status);
        this.status = status;
    }

    /**
     * Method returns the desired execution time for the task or Long.MAX_VALUE if the execution is not required.
     * @return desired execution time for the task.
     */
    @Override
    public long getTime() {
        return nextTime;
    }

    @Override
    public void setNextTime() {
        logger.debug("set next time");
        if (hasMoreMessages) {
            nextTime = RandomGenerator.getNumber(MIN_DELAY, MAX_DELAY) + System.currentTimeMillis();
        } else {
            nextTime = Long.MAX_VALUE;
        }
    }

    @Override
    public void update() throws SQLException {
        logger.debug("update");
        synonymizedMessages.clear();
        try {
            messagesForSynonymize = messageDAO.getUnSynonymizedMessages(MESSAGE_LIMIT);
        } catch (NoSuchEntityException e) {
            hasMoreMessages = false;
            nextTime = Long.MAX_VALUE;
        }
    }

    @Override
    public void save() throws SQLException {
        logger.debug("save");
        if (synonymizedMessages == null || synonymizedMessages.size() == 0) return;
        messageDAO.update(synonymizedMessages);
    }



    @Override
    public void execute() {
        logger.info("synonymize messages");
        if (messagesForSynonymize == null || messagesForSynonymize.size() == 0) return;
        try {
            for (Message message : messagesForSynonymize) {
                logger.debug("original message=" + message.getMessage());
                List<String> wordList = getWordList(message.getMessage());
                try {
                    List<Integer> positionsOfPassableReplacements = getPositionsOfPassableReplacement(wordList);
                    logger.debug("possible replacements = " + positionsOfPassableReplacements.size());
                    int replacementsNumber = getReplacementsNumber(positionsOfPassableReplacements);
                    logger.debug("actual replacements = " + replacementsNumber);
                    replaceSynonyms(replacementsNumber, positionsOfPassableReplacements, wordList);

                } catch (NoSuchEntityException entityEx) {
                    logger.debug("message has no one word to replace by synonym");
                }

                StringBuilder text = new StringBuilder();
                for (String word : wordList) {
                    text.append(word).append(" ");
                }
                String newMessage = text.substring(0, text.lastIndexOf(" "));
                if (newMessage.length() > 140) logger.debug("message with 1d " + message.getId() + " in result is too long, so left it old message");
                if (newMessage.length() <= 140) {
                    message.setMessage(newMessage);
                }
                message.setSynonymized(true);
                synonymizedMessages.add(message);
                logger.debug("synonymized message=" + message.getMessage());
            }
        } catch (SQLException sqlEx) {
            logger.warn("exception", sqlEx);
        }

    }



    @Override
    public void setDailyParams() throws SQLException {
        logger.debug("set next time like daily parameters");
        setNextTime();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }




    //Tested
    List<String> getWordList(String message) {
        List<String> wordList = new ArrayList<>();
        String[] wordTokens = message.split(" ");
        Collections.addAll(wordList, wordTokens);
        return wordList;
    }

    //Tested
    int recognizePunctuationMarksPosition(String word) {
        for (int counter = 0; counter < word.length(); counter++) {
            if (!Character.isLetter(word.charAt(counter))) {
                return counter;
            }
        }
        return -1;
    }

    //Tested
    List<Integer> getPositionsOfPassableReplacement(List<String> wordsList) throws NoSuchEntityException {
        List<Integer> passableReplacements = new ArrayList<>();
        for (int i = 0; i < wordsList.size(); i++) {
            String word = wordsList.get(i);
            if (word.contains("@") || word.contains("_") || word.length() <= MIN_SYN_WORD_LENGTH) continue;
            int punctuation = recognizePunctuationMarksPosition(word);
            if (punctuation >= 0 && punctuation <= MIN_SYN_WORD_LENGTH) continue;
            passableReplacements.add(i);
        }
        if (passableReplacements.isEmpty()) throw new NoSuchEntityException();
        return passableReplacements;
    }

    //Tested
    String getRandomSynonym(List<String> synonymList) {
        if (synonymList.size() == 1) return synonymList.get(0);
        int position = RandomGenerator.getNumberFromZeroToRequirement(synonymList.size() - 1);
        return synonymList.get(position);
    }

    //Tested
    int getReplacementsNumber(List<Integer> positionsOfPassableReplacements) {
        int randomPercent = RandomGenerator.getNumber(MIN_SYN_PERCENT, MAX_SYN_PERCENT);
        logger.debug("random percent for message = " + randomPercent);
        int passableReplacementsNumber = positionsOfPassableReplacements.size();
        return (int) Math.round((double)passableReplacementsNumber / (double)100 * (double)randomPercent);
    }

    //Tested
    void replaceSynonyms(int replacementsNumber, List<Integer> positionsOfPassableReplacements, List<String> wordList) throws SQLException {
        int doneReplacements = 0;
        while (doneReplacements < replacementsNumber && !positionsOfPassableReplacements.isEmpty()) {

            int indexOfReplacementWordIndex = RandomGenerator.getNumberFromZeroToRequirement(positionsOfPassableReplacements.size() - 1);

            int replacementWordIndex = positionsOfPassableReplacements.get(indexOfReplacementWordIndex);

            String replacementWord = wordList.get(replacementWordIndex);

            int punctuationIndex = recognizePunctuationMarksPosition(replacementWord);
            String replacementWordPart;
            String punctuationPart;
            if (punctuationIndex == -1) {
                replacementWordPart = replacementWord;
                punctuationPart = "";
            } else {
                replacementWordPart = replacementWord.substring(0, punctuationIndex);
                punctuationPart = replacementWord.substring(punctuationIndex, replacementWord.length());
            }

            List<String> synonyms;
            try {
                Synonym synonym = synonymDAO.getSynonym(replacementWordPart);
                synonyms = synonym.getSynonyms();
                int usedCount = synonym.getUsed();
                synonym.setUsed(++usedCount);
                synonymDAO.update(synonym);
            } catch (NoSuchEntityException e) {
                positionsOfPassableReplacements.remove(indexOfReplacementWordIndex);
                notExistsSynonymDAO.addWord(replacementWordPart);
                continue;
            }
            String synonym = getRandomSynonym(synonyms);
            String synonymizedWord = synonym + punctuationPart;

            wordList.set(replacementWordIndex, synonymizedWord);

            positionsOfPassableReplacements.remove(indexOfReplacementWordIndex);

            doneReplacements++;
        }
    }
}
