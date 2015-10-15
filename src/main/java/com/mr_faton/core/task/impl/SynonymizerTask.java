package com.mr_faton.core.task.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.SynonymizerDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.RandomGenerator;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
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
    private static final int MESSAGE_LIMIT = 10;
    private static final int MIN_DELAY = 40 * 60 * 1000; //minutes
    private static final int MAX_DELAY = 80 * 60 * 1000; //minutes
    private static final int MIN_SYN_WORD_LENGTH = 3;
    private static final int MIN_SYN_PERCENT = 30;
    private static final int MAX_SYN_PERCENT = 80;

    private final MessageDAO messageDAO;
    private final SynonymizerDAO synonymizerDAO;

    private boolean status = true;
    private long nextTime = 0;
    private List<Message> messagesForSynonymize;
    private boolean hasMoreMessages = true;



    public SynonymizerTask(MessageDAO messageDAO, SynonymizerDAO synonymizerDAO) {
        logger.debug("constructor");
        this.messageDAO = messageDAO;
        this.synonymizerDAO = synonymizerDAO;
    }



    @Override
    public boolean getStatus() {
        return status;
    }

    @Override
    public void setStatus(boolean status) {
        logger.info("status changed to " + status);
        this.status = status;
    }

    @Override
    public long getTime() {
        return nextTime;
    }

    @Override
    public void setNextTime() {
        logger.debug("set next time");
        if (hasMoreMessages) {
            nextTime = RandomGenerator.getNumber(MIN_DELAY, MAX_DELAY) + System.currentTimeMillis();
            logger.debug("next time is set to " + TimeWizard.convertToFuture(nextTime));
        } else {
            nextTime = Long.MAX_VALUE;
        }
    }

    @Override
    public void update() throws SQLException {
        logger.debug("update");
        if (messagesForSynonymize != null) messagesForSynonymize.clear();
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
        if (messagesForSynonymize == null || messagesForSynonymize.size() == 0) return;
        messageDAO.update(messagesForSynonymize);
    }

    @Override
    public void execute() {
        logger.info("synonymize messages");
        if (messagesForSynonymize == null || messagesForSynonymize.size() == 0) return;
        for (Message message : messagesForSynonymize) {
            List<String> wordList = getWordList(message);
            List<Integer> passableReplacementsList = null;
            try {
                passableReplacementsList = getPositionsOfPassableReplacement(wordList);
            } catch (NoSuchEntityException entityEx) {
                logger.debug("in message '" + message.getMessage() + "' no one word can't be potentiality replaced by synonym");
                return;
            }
            /*TODO continue and finish*/
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
        for (String word : wordTokens) {
            wordList.add(word);
        }
        return wordList;
    }

    private List<Integer> getPositionsOfPassableReplacement(List<String> wordList) throws NoSuchEntityException{
        List<Integer> passableReplacements = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            String potentialReplacementWord = wordList.get(i);
            if (potentialReplacementWord.length() > MIN_SYN_WORD_LENGTH) {
                passableReplacements.add(i);
            }
        }
        if (passableReplacements.isEmpty()) {
            throw new NoSuchEntityException();
        }
        return passableReplacements;
    }
}
