package com.mr_faton.core.task.impl;

import com.mr_faton.core.api.TwitterAPI;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.exception.DataSequenceReachedException;
import com.mr_faton.core.exception.LimitExhaustedException;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.task.Task;
import com.mr_faton.core.util.RandomGenerator;
import org.apache.log4j.Logger;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 07.10.2015
 */
public class MessageParseTask implements Task {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.task.impl.MessageParseTask");

    private static final int MIN_DELAY = 40 * 60 * 1000; //minutes
    private static final int MAX_DELAY = 80 * 60 * 1000; //minutes
    private static final String SOURCE_USER = "Mr_Faton";
    private static final String MESSAGE_lANG = "ru";
    private static final String COMMERCIAL_PARAM = "http";
    private static final String MENTION_PARAM = "@";
    private static final int MESSAGES_PER_PAGE = 200;
    private static final int PAGES_PER_ONE_SEARCH = 25;
    private static final int DEPTH_SEARCH_IN_YEAR = 3; //max deep = 3 years old
    private static final List<Message> messageList = new ArrayList<>();

    private final TwitterAPI twitterAPI;
    private final MessageDAO messageDAO;
    private final DonorUserDAO donorUserDAO;

    private boolean status = true;
    private long nextTime = 0;
    private Date maxOldDate;
    private DonorUser donorUser = null;
    private int searchedPage = 0;

    public MessageParseTask(
            TwitterAPI twitterAPI,
            MessageDAO messageDAO,
            DonorUserDAO donorUserDAO) {
        logger.debug("constructor");
        this.twitterAPI = twitterAPI;
        this.messageDAO = messageDAO;
        this.donorUserDAO = donorUserDAO;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -DEPTH_SEARCH_IN_YEAR);
        maxOldDate = new Date(calendar.getTimeInMillis());
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
        logger.debug("<=");
        nextTime = System.currentTimeMillis() + RandomGenerator.getNumber(MIN_DELAY, MAX_DELAY);
        logger.debug("next time is set to " + String.format("%td-%<tm-%<tY", new Date(nextTime)));
    }

    @Override
    public void update() throws SQLException {
        logger.debug("<=");
        if (donorUser == null) {
            try {
                donorUser = donorUserDAO.getDonorForMessage();
                searchedPage = 0;
                logger.debug("donorUser updated to " + donorUser.getName());
            } catch (NoSuchEntityException ex) {
                logger.debug("no donorUser to parse messages");
                nextTime = Long.MAX_VALUE;
            }
        } else {
            logger.debug("donorUser = " + donorUser.getName() + ", so don't need to update it");
        }
    }

    @Override
    public void save() throws SQLException {
        logger.debug("<=");
        messageDAO.saveMessageList(messageList);
        messageList.clear();
    }

    @Override
    public void execute() {
        logger.debug("collect messages");
        if (donorUser == null) {
            logger.warn("donor user can't be null");
            return;
        }

        try {
            for (int counter = 0; counter < PAGES_PER_ONE_SEARCH; counter++) {
                ResponseList<Status> statusList = twitterAPI.getUserTimeLine(
                        donorUser.getName(),
                        new Paging(searchedPage),
                        MESSAGES_PER_PAGE,
                        SOURCE_USER);

                searchedPage++;

                handleUserTimeLineList(statusList);
                logger.info("collected " + messageList.size() + " messages");
            }
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

    private void handleUserTimeLineList (ResponseList<Status> statusList) throws DataSequenceReachedException{
        for (Status status : statusList) {

            Date statusDate = status.getCreatedAt();
            if (maxOldDate.after(statusDate)) throw new DataSequenceReachedException();
            if (!MESSAGE_lANG.equals(status.getLang())) continue;
            if (status.isRetweet()) continue;

            String text = status.getText();
            if (text.contains(COMMERCIAL_PARAM)) continue;

            boolean isTweet = true;
            String recipient = null;
            if (text.contains(MENTION_PARAM)) {
                isTweet = false;
                recipient = status.getInReplyToScreenName();
                if (recipient == null || !mentionValidator(text)) continue;
            }

            Message message = new Message();
            message.setMessage(text);
            message.setTweet(isTweet);
            message.setOwner(donorUser.getName());
            message.setOwnerMale(donorUser.isMale());
            message.setRecipient(recipient);
            message.setPostedDate(statusDate);

            messageList.add(message);
        }
    }

    private boolean mentionValidator(String mention) {
        int firstIndex = mention.indexOf(MENTION_PARAM);
        int lastIndex = mention.lastIndexOf(MENTION_PARAM);
        return firstIndex == lastIndex;
    }
}
