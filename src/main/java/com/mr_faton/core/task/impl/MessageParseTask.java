package com.mr_faton.core.task.impl;

import com.mr_faton.core.api.TwitterAPI;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.exception.BadUserException;
import com.mr_faton.core.exception.DataSequenceReachedException;
import com.mr_faton.core.exception.LimitExhaustedException;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.RandomGenerator;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 4.6
 * @since 07.10.2015
 */
public class MessageParseTask implements Task {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.task.impl.MessageParseTask");

    public static final int MIN_DELAY = 2 * 60 * 1000; //minutes
    public static final int MAX_DELAY = 4 * 60 * 1000; //minutes
    public static final String SOURCE_USER = "Mr_Faton";
    public static final int MESSAGES_PER_PAGE = 200;
    public static final int PAGES_PER_ONE_SEARCH = 10;
    private static final String MESSAGE_lANG = "ru";
    private static final String COMMERCIAL_PARAM = "http";
    private static final String MENTION_PARAM = "@";
    private static final List<Message> messageList = new ArrayList<>();

    @Autowired
    private TwitterAPI twitterAPI;
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private DonorUserDAO donorUserDAO;

    private boolean status = true;
    private long nextTime = 0;
    private DonorUser donorUser = null;
    private int searchedPage = 1;
    private boolean hasMoreDonorUsers = true;


    @Override
    public boolean getStatus() {
        logger.debug("status is " + status);
        return status;
    }

    @Override
    public void setStatus(boolean status) {
        logger.info("status changed to " + status);
        this.status = status;
    }

    @Override
    public long getTime() {
        logger.debug("next parsing will begin in " + TimeWizard.formatDateWithTime(nextTime));
        return nextTime;
    }

    @Override
    public void setNextTime() {
        logger.debug("set next time");
        if (hasMoreDonorUsers) {
            nextTime = System.currentTimeMillis() + RandomGenerator.getNumber(MIN_DELAY, MAX_DELAY);
            logger.debug("next parsing has been set up on " + TimeWizard.formatDateWithTime(nextTime));
        } else {
            nextTime = Long.MAX_VALUE;
            logger.debug("next parsing hes been set up on maximum time, cause no donor user found for parse message");
        }

    }

    @Override
    public void update() throws SQLException {
        logger.debug("update");
        if (!hasMoreDonorUsers) return;
        if (donorUser == null) {
            try {
                donorUser = donorUserDAO.getDonorForMessage();
                searchedPage = 1;
                logger.debug("donorUser updated to " + donorUser.getName());

                donorUser.setTakeMessageDate(new Date());

                donorUserDAO.update(donorUser);
            } catch (NoSuchEntityException ex) {
                logger.debug("no donorUser for parse messages");
                hasMoreDonorUsers = false;
            }
        } else {
            logger.debug("donorUser = " + donorUser.getName() + ", so don't need to update it");
        }
    }

    @Override
    public void save() throws SQLException {
        logger.debug("save " + messageList.size() + " parsed messages");
        if (messageList.size() == 0) return;
        messageDAO.save(messageList);
        messageList.clear();
    }


    @Override
    public void execute() {

        if (donorUser != null) {
            logger.info("parse messages from user " + donorUser.getName());
        } else {
            logger.info("user for parsing messages is null, stop parsing");
            return;
        }

        try {
            for (int counter = 0; counter < PAGES_PER_ONE_SEARCH; counter++) {
                Paging paging = new Paging(searchedPage, MESSAGES_PER_PAGE);
                ResponseList<Status> statusList = twitterAPI.getUserTimeLine(donorUser.getName(), paging, SOURCE_USER);
                searchedPage++;
                handleUserTimeLineList(statusList);
            }
        } catch (BadUserException badUserEx) {
            logger.debug(badUserEx.getMessage());
            donorUser = null;
        } catch (DataSequenceReachedException lastTweetEx) {
            donorUser = null;
        } catch (LimitExhaustedException limitEx) {
            logger.debug("twitter limits exceeded, so continue next time");
        } catch (TwitterException twitterEx) {
            logger.debug("twitter exception", twitterEx);
        } catch (NoSuchEntityException noEntityEx) {
            logger.warn("exception, seems like problems with source account", noEntityEx);
        } catch (SQLException sqlEx) {
            logger.warn("some sql exception", sqlEx);
        }
    }


    @Override
    public void setDailyParams() throws SQLException {
        logger.debug("set next time like daily params");
        setNextTime();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }



    void handleUserTimeLineList (ResponseList<Status> statusList) throws DataSequenceReachedException{
        if (statusList.size() == 0) {
            logger.debug("response status list = 0, stop handle this user");
            throw new DataSequenceReachedException();
        }

        for (Status status : statusList) {
            if (!MESSAGE_lANG.equals(status.getLang())) continue;
            if (status.isRetweet()) continue;

            String text = status.getText();
            if (text.contains(COMMERCIAL_PARAM)) continue;

            String recipient = null;
            if (text.contains(MENTION_PARAM)) {
                recipient = status.getInReplyToScreenName();
                if (recipient == null || !mentionValidator(text)) continue;
            }

            Date statusDate = status.getCreatedAt();

            Message message = new Message();
            message.setMessage(text);
            message.setOwner(donorUser.getName());
            message.setOwnerMale(donorUser.isMale());
            message.setRecipient(recipient);
            message.setPostedDate(statusDate);

            messageList.add(message);
        }
    }

    boolean mentionValidator(String mention) {
        int firstIndex = mention.indexOf(MENTION_PARAM);
        int lastIndex = mention.lastIndexOf(MENTION_PARAM);
        return firstIndex == lastIndex;
    }
}
