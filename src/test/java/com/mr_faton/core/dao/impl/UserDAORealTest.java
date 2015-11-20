package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.DBTestHelper;
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

import java.util.ArrayList;
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
    private static final String TABLE = "users";
    private static final String EMPTY_DATA_SET = "/test/data_set/user/empty.xml";

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    public void getUserByName() throws Exception {
        String beforeGetUserByName = "/test/data_set/user/beforeGetUserByName.xml";
        DBTestHelper.fill(beforeGetUserByName, jdbcTemplate);
        User expected = tableToUser(DBTestHelper.getTableFromFile(TABLE, beforeGetUserByName), 0);
        User actual = userDAO.getUserByName(expected.getName());

        equalUsers(expected, actual);
    }

    @Test
    public void getUserList() throws Exception {
        String beforeGetUserList = "/test/data_set/user/beforeGetUserList.xml";
        DBTestHelper.fill(beforeGetUserList, jdbcTemplate);
        ITable expectedTable = DBTestHelper.getTableFromFile(TABLE, beforeGetUserList);
        User expected1 = tableToUser(expectedTable, 0);
        User expected2 = tableToUser(expectedTable, 1);

        List<User> actualUsers = userDAO.getUserList();

        equalUsers(expected1, actualUsers.get(0));
        equalUsers(expected2, actualUsers.get(1));
    }

    @Test
    public void saveAndUpdate() throws Exception {
        final String afterSaveTable = "/test/data_set/user/afterSave.xml";
        final String afterUpdateTable = "/test/data_set/user/afterUpdate.xml";
        User user;
        ITable expected;
        ITable actual;
        DBTestHelper.fill(EMPTY_DATA_SET, jdbcTemplate);

        //Test save
        expected = DBTestHelper.getTableFromFile(TABLE, afterSaveTable);
        user = tableToUser(expected, 0);
        userDAO.save(user);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEquals(expected, actual);

        //Test update
        expected = DBTestHelper.getTableFromFile(TABLE, afterUpdateTable);
        user = tableToUser(expected, 0);
        userDAO.update(user);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
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
        DBTestHelper.fill(EMPTY_DATA_SET, jdbcTemplate);

        //Test save
        expected = DBTestHelper.getTableFromFile(TABLE, afterSaveListTable);
        user1 = tableToUser(expected, 0);
        user2 = tableToUser(expected, 1);
        userList.add(user1);
        userList.add(user2);
        userDAO.save(userList);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEquals(expected, actual);

        //Test update
        expected = DBTestHelper.getTableFromFile(TABLE, afterUpdateListTable);
        user1 = tableToUser(expected, 0);
        user2 = tableToUser(expected, 1);
        userList.clear();
        userList.add(user1);
        userList.add(user2);
        userDAO.update(userList);
        actual = DBTestHelper.getTableFromSchema(TABLE, jdbcTemplate);
        Assertion.assertEquals(expected, actual);

    }


    private User tableToUser(ITable table, int rowNum) throws Exception {
        String datePattern = "yyyy-MM-dd";
        User user = new User();

        user.setName((String) table.getValue(rowNum, "u_name"));
        user.setPassword((String) table.getValue(rowNum, "u_password"));
        user.setEmail((String) table.getValue(rowNum, "email"));
        user.setMale(Boolean.valueOf((String) table.getValue(rowNum, "male")));
        user.setCreationDate(TimeWizard.stringToDate((String) table.getValue(rowNum, "creation_date"), datePattern));
        user.setMessages(Integer.valueOf((String) table.getValue(rowNum, "messages")));
        user.setFollowing(Integer.valueOf((String) table.getValue(rowNum, "following")));
        user.setFollowers(Integer.valueOf((String) table.getValue(rowNum, "followers")));
        user.setConsumerKey((String) table.getValue(rowNum, "consumer_key"));
        user.setConsumerSecret((String) table.getValue(rowNum, "consumer_secret"));
        user.setAccessToken((String) table.getValue(rowNum, "access_token"));
        user.setAccessTokenSecret((String) table.getValue(rowNum, "access_token_secret"));

        return user;
    }
    private void equalUsers(User expected, User actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.isMale(), actual.isMale());
        assertEquals(expected.getCreationDate(), actual.getCreationDate());

        assertEquals(expected.getMessages(), actual.getMessages());
        assertEquals(expected.getFollowing(), actual.getFollowing());
        assertEquals(expected.getFollowers(), actual.getFollowers());

        assertEquals(expected.getConsumerKey(), actual.getConsumerKey());
        assertEquals(expected.getConsumerSecret(), actual.getConsumerSecret());
        assertEquals(expected.getAccessToken(), actual.getAccessToken());
        assertEquals(expected.getAccessTokenSecret(), actual.getAccessTokenSecret());
    }
}
