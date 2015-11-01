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
 * @version 4.6
 * @since 07.10.2015
 */
public class MessageParseTask implements Task {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.task.impl.MessageParseTask");

    public static final int MIN_DELAY = 0 * 60 * 1000; //minutes
    public static final int MAX_DELAY = 1 * 60 * 1000; //minutes
    public static final String SOURCE_USER = "Mr_Faton";
    public static final int MESSAGES_PER_PAGE = 200;
    public static final int PAGES_PER_ONE_SEARCH = 10;
    public static final int DEPTH_SEARCH_IN_YEAR = 3; //max deep = 3 years old
    private static final String MESSAGE_lANG = "ru";
    private static final String COMMERCIAL_PARAM = "http";
    private static final String MENTION_PARAM = "@";
    private static final List<Message> messageList = new ArrayList<>();

    private final TwitterAPI twitterAPI;
    private final MessageDAO messageDAO;
    private final DonorUserDAO donorUserDAO;

    private boolean status = true;
    private long nextTime = 0;
    private Date maxOldDate;
    private DonorUser donorUser = null;
    private int searchedPage = 1;
    private boolean hasMoreDonorUsers = true;

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
        if (hasMoreDonorUsers) {
            nextTime = System.currentTimeMillis() + RandomGenerator.getNumber(MIN_DELAY, MAX_DELAY);
            logger.debug("next time is set to " + TimeWizard.convertToFuture(nextTime));
        } else {
            nextTime = Long.MAX_VALUE;
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
                logger.debug("no donorUser to parse messages");
                hasMoreDonorUsers = false;
            }
        } else {
            logger.debug("donorUser = " + donorUser.getName() + ", so don't need to update it");
        }
    }

    @Override
    public void save() throws SQLException {
        logger.debug("save");
        if (messageList.size() == 0) return;
        messageDAO.save(messageList);
        messageList.clear();
    }


    @Override
    public void execute() {
        logger.info("collect messages");
        if (donorUser == null) return;

        try {
            for (int counter = 0; counter < PAGES_PER_ONE_SEARCH; counter++) {
                try {
                    ResponseList<Status> statusList = twitterAPI.getUserTimeLine(
                            donorUser.getName(),
                            new Paging(searchedPage, MESSAGES_PER_PAGE),
                            SOURCE_USER);

                    searchedPage++;

                    handleUserTimeLineList(statusList);
                } catch (BadUserException badUserEx) {
                    logger.debug(badUserEx.getMessage());
                    donorUser = null;
                    donorUserDAO.deleteUser(donorUser.getName());
                }

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

    void handleUserTimeLineList (ResponseList<Status> statusList) throws DataSequenceReachedException{
        if (statusList.size() == 0) {
            logger.debug("response status list = 0, stop handle this user");
            throw new DataSequenceReachedException();
        }

        for (Status status : statusList) {
            Date statusDate = status.getCreatedAt();
            if (maxOldDate.after(statusDate)) throw new DataSequenceReachedException();
            if (!MESSAGE_lANG.equals(status.getLang())) continue;
            if (status.isRetweet()) continue;

            String text = status.getText();
            if (text.contains(COMMERCIAL_PARAM)) continue;

            String recipient = null;
            if (text.contains(MENTION_PARAM)) {
                recipient = status.getInReplyToScreenName();
                if (recipient == null || !mentionValidator(text)) continue;
            }

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
