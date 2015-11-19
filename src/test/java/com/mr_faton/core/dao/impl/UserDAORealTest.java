package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DBHelper;
import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.table.User;
import com.mr_faton.core.util.TimeWizard;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.Counter;

import java.util.ArrayList;
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
    private static final String TABLE = "users";
    private static final String EMPTY_TABLE = "/test/data_set/user/empty.xml";
    private static final String USER_TABLE_DATE_PATTERN = "yyyy-MM-dd";
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
        final String afterSaveTable = "/test/data_set/user/afterSave.xml";
        final String afterUpdateTable = "/test/data_set/user/afterUpdate.xml";
        User user;
        ITable expected;
        ITable actual;

        //Test save
        expected = DBHelper.getTableFromFile(TABLE, afterSaveTable);
        user = tableToUser(expected, 0);
        userDAO.save(user);
        actual = DBHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEquals(expected, actual);

        //Test update
        expected = DBHelper.getTableFromFile(TABLE, afterUpdateTable);
        user = tableToUser(expected, 0);
        userDAO.update(user);
        actual = DBHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEquals(expected, actual);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        final String afterSaveListTable = "/test/data_set/user/afterSaveList.xml";
        final String afterUpdateListTable = "/test/data_set/user/afterUpdateList.xml";
        User user1;
        User user2;
        List<User> userList = new ArrayList<>(2);
        ITable expected;
        ITable actual;

        //Test save
        expected

        //Test update

    }


    private User tableToUser(ITable table, int rowNum) throws Exception {
        User user = new User();

        user.setName((String) table.getValue(rowNum, "u_name"));
        user.setPassword((String) table.getValue(rowNum, "u_password"));
        user.setEmail((String) table.getValue(rowNum, "email"));
        user.setMale(Boolean.valueOf((String) table.getValue(rowNum, "male")));
        user.setCreationDate(TimeWizard.stringToDate((String) table.getValue(rowNum, "creation_date"), USER_TABLE_DATE_PATTERN));
        user.setMessages(Integer.valueOf((String) table.getValue(rowNum, "messages")));
        user.setFollowing(Integer.valueOf((String) table.getValue(rowNum, "following")));
        user.setFollowers(Integer.valueOf((String) table.getValue(rowNum, "followers")));
        user.setConsumerKey((String) table.getValue(rowNum, "consumer_key"));
        user.setConsumerSecret((String) table.getValue(rowNum, "consumer_secret"));
        user.setAccessToken((String) table.getValue(rowNum, "access_token"));
        user.setAccessTokenSecret((String) table.getValue(rowNum, "access_token_secret"));

        return user;
    }



    @Deprecated
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
    @Deprecated
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


}
