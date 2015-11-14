package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.MessageService;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 12.11.2015
 */
public class MessageServiceImpl implements MessageService {
    @Autowired
    DonorUserDAO donorUserDAO;
    @Autowired
    MessageDAO messageDAO;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Message getTweet(boolean ownerMale) throws SQLException, NoSuchEntityException {
        return messageDAO.getTweet(ownerMale);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Message getTweet(boolean ownerMale, Calendar minCalendar, Calendar maxCalendar)
            throws SQLException, NoSuchEntityException {
        return messageDAO.getTweet(ownerMale, minCalendar, maxCalendar);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Message getTweet(String ownerName) throws SQLException, NoSuchEntityException {
        return messageDAO.getTweet(ownerName);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Message getTweet(String ownerName, Calendar minCalendar, Calendar maxCalendar)
            throws SQLException, NoSuchEntityException {
        return messageDAO.getTweet(ownerName, minCalendar, maxCalendar);
    }



    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Message getMention(boolean ownerMale, boolean recipientMale, Calendar minCalendar, Calendar maxCalendar)
            throws SQLException, NoSuchEntityException {
        return messageDAO.getMention(ownerMale, recipientMale, minCalendar, maxCalendar);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Message> getUnSynonymizedMessages(int limit) throws SQLException, NoSuchEntityException {
        return messageDAO.getUnSynonymizedMessages(limit);
    }



    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(Message message) throws SQLException {
        if (message.getRecipient() != null) {
            DonorUser donorUser = new DonorUser();
            donorUser.setName(message.getRecipient());
            donorUser.setMale(message.isRecipientMale());

            donorUserDAO.save(donorUser);
        }
        messageDAO.save(message);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void save(List<Message> messageList) throws SQLException {

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(Message message) throws SQLException {

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void update(List<Message> messageList) throws SQLException {

    }
}
