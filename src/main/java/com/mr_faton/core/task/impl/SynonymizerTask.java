package com.mr_faton.core.task.impl;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.SynonymizerDAO;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.RandomGenerator;
import org.apache.log4j.Logger;

import java.sql.SQLException;
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

    private final MessageDAO messageDAO;
    private final SynonymizerDAO synonymizerDAO;

    private boolean status = true;
    private long nextTime = 0;
    private List<Message> messagesForSynonymize;



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
        logger.info("status changed to " + (status ? "on":"off"));
        this.status = status;
    }

    @Override
    public long getTime() {
        return nextTime;
    }

    @Override
    public void setNextTime() {
        nextTime = RandomGenerator.getNumber(MIN_DELAY, MAX_DELAY) + System.currentTimeMillis();
    }

    @Override
    public void update() throws SQLException {
        messagesForSynonymize = messageDAO.getUnSynonymizedMessages(MESSAGE_LIMIT);
    }

    @Override
    public void save() throws SQLException {
        messageDAO.updateSynonymizedMessages(messagesForSynonymize);
    }

    @Override
    public void execute() {

    }

    @Override
    public void setDailyParams() throws SQLException {
        setNextTime();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
