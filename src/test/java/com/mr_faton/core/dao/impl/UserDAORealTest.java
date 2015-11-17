package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DBFiller;
import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.table.User;
import com.mr_faton.core.util.TimeWizard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.Counter;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 14.10.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/test/daoTestConfig.xml"))
public class UserDAORealTest {
    private static final String BASE_NAME = "UserDAOReal";
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void getUserByName() throws Exception {
        final User original = createDefaultUser();
        userDAO.save(original);
        User extracted = userDAO.getUserByName(original.getName());
        equalsUsers(original, extracted);
    }

    @Test
    public void getUserList() throws Exception {
        final User user1 = createDefaultUser();
        final User user2 = createDefaultUser();
        final List<User> userList = Arrays.asList(user1, user2);

        userDAO.save(userList);

        List<User> extractedUserList = userDAO.getUserList();
        assertTrue(extractedUserList.size() >= 2);
    }





    @Test
    public void saveAndUpdate() throws Exception {
        //Test save
        final User original = createDefaultUser();
        DBFiller.fill("/test/data_set/user/afterSave.xml", jdbcTemplate);
        DBFiller.generateSchemaDTD(jdbcTemplate);
        userDAO.save(original);
        User extracted = userDAO.getUserByName(original.getName());
        equalsUsers(original, extracted);



        //Test update
//        original.setPassword("pass_sdokgmr");
//        original.setEmail("asdfs@jois.ru");
//        original.setMessages(5476);
//        original.setFollowing(56765);
//        original.setFollowers(48745);
//        original.setConsumerKey("kjfafien");
//        original.setConsumerSecret("sajoarn");
//        original.setAccessToken("juihbgifbn");
//        original.setAccessTokenSecret("skjifbh");
//
//        userDAO.update(original);
//
//        extracted = userDAO.getUserByName(original.getName());
//
//        equalsUsers(original, extracted);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        //Test save
        final User original1 = createDefaultUser();
        final User original2 = createDefaultUser();
        List<User> userList = Arrays.asList(original1, original2);

        userDAO.save(userList);

        User extracted1 = userDAO.getUserByName(original1.getName());
        User extracted2 = userDAO.getUserByName(original2.getName());

        equalsUsers(original1, extracted1);
        equalsUsers(original2, extracted2);

        //Test update
        original1.setPassword("adsjfoiv");
        original1.setEmail("ouip@asf.ua");
        original1.setMessages(1948);
        original1.setFollowing(6581);
        original1.setFollowers(5971);
        original1.setConsumerKey("rqw");
        original1.setConsumerSecret("zgvchagsv");
        original1.setAccessToken("dhjdkfl");
        original1.setAccessTokenSecret("ohjopg");

        original2.setPassword("jdbkjd");
        original2.setEmail("iuyrie@hty.hu");
        original2.setMessages(8812);
        original2.setFollowing(2168);
        original2.setFollowers(1028);
        original2.setConsumerKey("trqt");
        original2.setConsumerSecret("bnbm");
        original2.setAccessToken("ngfyc");
        original2.setAccessTokenSecret("khigf");

        userDAO.update(userList);

        extracted1 = userDAO.getUserByName(original1.getName());
        extracted2 = userDAO.getUserByName(original2.getName());

        equalsUsers(original1, extracted1);
        equalsUsers(original2, extracted2);
    }



    private User createDefaultUser() {
        User user = new User();

        user.setName(BASE_NAME + Counter.getNextNumber());
        user.setPassword("secret");
        user.setEmail("testmail@test.com");
        user.setMale(true);
        user.setCreationDate(new Date());
        user.setMessages(100);
        user.setFollowing(200);
        user.setFollowers(300);
        user.setConsumerKey("consumer key");
        user.setConsumerSecret("consumer secret");
        user.setAccessToken("access token");
        user.setAccessTokenSecret("access token secret");

        return user;
    }

    private void equalsUsers(User original, User extracted) {
        assertEquals(original.getName(), extracted.getName());
        assertEquals(original.getPassword(), extracted.getPassword());
        assertEquals(original.getEmail(), extracted.getEmail());
        assertEquals(original.isMale(), extracted.isMale());

        String formattedOriginal = TimeWizard.formatDate(original.getCreationDate().getTime());
        String formattedExtracted = TimeWizard.formatDate(extracted.getCreationDate().getTime());
        assertEquals(formattedOriginal, formattedExtracted);

        assertEquals(original.getMessages(), extracted.getMessages());
        assertEquals(original.getFollowing(), extracted.getFollowing());
        assertEquals(original.getFollowers(), extracted.getFollowers());

        assertEquals(original.getConsumerKey(), extracted.getConsumerKey());
        assertEquals(original.getConsumerSecret(), extracted.getConsumerSecret());
        assertEquals(original.getAccessToken(), extracted.getAccessToken());
        assertEquals(original.getAccessTokenSecret(), extracted.getAccessTokenSecret());
    }

    void compareDataSets(String expectedFileName, String actualTableName) {
        Connection connection = null;
//        try {
//
//        }
    }

}
