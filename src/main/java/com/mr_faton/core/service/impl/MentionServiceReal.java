package com.mr_faton.core.service.impl;

import com.mr_faton.core.dao.DonorUserDAO;
import com.mr_faton.core.dao.MentionDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.service.MentionService;
import com.mr_faton.core.table.DonorUser;
import com.mr_faton.core.table.Mention;
import com.mr_faton.core.util.TimeWizard;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @since 05.12.2015
 */
public class MentionServiceReal implements MentionService {
    private static final Logger logger = Logger.getLogger("" +
            "com.mr_faton.core.service.impl.MentionServiceReal");

    @Autowired
    private DonorUserDAO donorUserDAO;
    @Autowired
    private MentionDAO mentionDAO;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Mention getMention(boolean ownerMale, boolean recipientMale) throws SQLException, NoSuchEntityException {
        logger.debug("begin search mention with parameters: " +
                "owner male = " + ownerMale + " and recipient male = " + recipientMale);
        Mention mention = mentionDAO.getMention(ownerMale, recipientMale);
        logger.debug("found mention: " + mention);
        return mention;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public Mention getMention(boolean ownerMale, boolean recipientMale, Date minDate, Date maxDate)
            throws SQLException, NoSuchEntityException {
        logger.debug("begin search mention with parameters: " +
                "owner male = " + ownerMale + ", recipient male = " + recipientMale + ", " +
                "min date = " + TimeWizard.formatDateWithTime(minDate.getTime()) + ", " +
                "max date = " + TimeWizard.formatDateWithTime(maxDate.getTime()));
        Mention mention = mentionDAO.getMention(ownerMale, recipientMale, minDate, maxDate);
        logger.debug("found mention: " + mention);
        return mention;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Mention mention) throws SQLException {
        logger.debug("begin save " + mention);
        DonorUser recipient = new DonorUser();
        recipient.setName(mention.getRecipient());
        donorUserDAO.save(recipient);
        mentionDAO.save(mention);
        logger.debug("end save");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<Mention> mentionList) throws SQLException {
        logger.debug("begin save " + mentionList.size() + " mentions: " + mentionList);
        List<DonorUser> recipientList = new ArrayList<>();
        for (Mention mention : mentionList) {
            DonorUser recipient = new DonorUser();
            recipient.setName(mention.getRecipient());
            recipientList.add(recipient);
        }
        donorUserDAO.save(recipientList);
        mentionDAO.save(mentionList);
        logger.debug("end save list");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Mention mention) throws SQLException {
        logger.debug("begin update mention " + mention);
        mentionDAO.update(mention);
        logger.debug("end update");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(List<Mention> mentionList) throws SQLException {
        logger.debug("begin update " + mentionList.size() + " mentions: " + mentionList);
        mentionDAO.update(mentionList);
        logger.debug("end update mention list");
    }
}
