package com.mr_faton.core.task.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.SynonymDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
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
public class SynonymizerTask_Old implements Task{
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.task.impl.SynonymizerTask");
    private static final int MESSAGE_LIMIT = 10; //how many messages select from db
    private static final int MIN_DELAY = 1 * 60 * 1000; //minutes
    private static final int MAX_DELAY = 2 * 60 * 1000; //minutes
    private static final int MIN_SYN_WORD_LENGTH = 3; //if the word length less or equals it, word not synonymized

    private final MessageDAO messageDAO;
    private final SynonymDAO synonymDAO;

    private boolean status = true;
    private long nextTime = 0;
    private List<Message> messagesForSynonymize; //messages who will be synonymized
    private List<Message> synonymizedMessages = new ArrayList<>(MESSAGE_LIMIT + 1); //synonymized messages
    private boolean hasMoreMessages = true; //if db says that no messages for synonymized, then turn it to false



    public SynonymizerTask_Old(MessageDAO messageDAO, SynonymDAO synonymDAO) {
        logger.debug("constructor");
        this.messageDAO = messageDAO;
        this.synonymDAO = synonymDAO;
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
                List<String> wordList = getWordList(message);
                List<String> synonymizedWordList = getSynonymizedWordList(wordList);
                StringBuilder text = new StringBuilder();
                for (String word : synonymizedWordList) {
                    text.append(word).append(" ");
                }
                String newMessage = text.substring(0, text.lastIndexOf(" "));
                if (newMessage.length() > 140) logger.debug("message with 1d " + message.getId() + " in result is too long, so left it old message");
                if (newMessage.length() <= 140) {
                    message.setMessage(newMessage);
                }
                message.setSynonymized(true);
                synonymizedMessages.add(message);
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


    private List<String> getWordList(Message message) {
        List<String> wordList = new ArrayList<>();
        String rowWords = message.getMessage();
        String[] wordTokens = rowWords.split(" ");
        Collections.addAll(wordList, wordTokens);
        return wordList;
    }

    private List<String> getSynonymizedWordList(List<String> wordList) throws SQLException {
        for (int counter = 0; counter < wordList.size(); counter++) {
            String word = wordList.get(counter);

            if (word.contains("@") || word.contains("_") || word.length() <= MIN_SYN_WORD_LENGTH) continue;

            int punctuationMarkPosition = recognizePunctuationMarksPosition(word);
            if (punctuationMarkPosition == 0) continue;
            String wordPart;
            String punctuationPart;
            if (punctuationMarkPosition < 0) {
                wordPart = word;
                punctuationPart = "";
            } else {
                wordPart = word.substring(0, punctuationMarkPosition);
                punctuationPart = word.substring(punctuationMarkPosition, word.length());
            }

            List<String> synonymList;
            try {
                synonymList = synonymDAO.getSynonyms(wordPart);
            } catch (NoSuchEntityException entityEx) {
                continue;
            }

            String synonym = getRandomSynonym(synonymList);
            String newWord = synonym + punctuationPart;

            wordList.set(counter, newWord);
        }
        return wordList;
    }

    private static int recognizePunctuationMarksPosition(String word) {
        for (int counter = 0; counter < word.length(); counter++) {
            if (!Character.isLetter(word.charAt(counter))) {
                return counter;
            }
        }
        return -1;
    }

    private String getRandomSynonym(List<String> synonymList) {
        int position = RandomGenerator.getNumberFromZeroToRequirement(synonymList.size() - 1);
        return synonymList.get(position);
    }
}
