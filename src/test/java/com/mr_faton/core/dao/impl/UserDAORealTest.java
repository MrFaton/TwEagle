package com.mr_faton.core.dao.impl;

import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.table.User;
import com.mr_faton.core.util.TimeWizard;
import org.dbunit.Assertion;
import org.dbunit.dataset.ITable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import util.DBTestHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 14.10.2015
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = ("classpath:/DaoTestConfig.xml"))
@Transactional
public class UserDAORealTest {
    private static final String TABLE = "users";
    private static final String COMMON_DATA_SET = "/data_set/user/common.xml";
    private static final String EMPTY_TABLE = "/data_set/user/empty.xml";

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private DBTestHelper dbTestHelper;

    @Before
    @Transactional
    @Rollback(false)
    public void before() throws Exception {
        dbTestHelper.fill(COMMON_DATA_SET);
    }

    @Test
    @Transactional
    public void getUserByName() throws Exception {
        String name = "Den";
        User user = userDAO.getUserByName(name);
        Assert.assertEquals(name, user.getName());
    }

    @Test
    @Transactional
    public void getUserList() throws Exception {
        ITable expectedTable = dbTestHelper.getTableFromFile(TABLE, COMMON_DATA_SET);
        List<User> expectedUserList = new ArrayList<>();
        for (int i = 0; i < expectedTable.getRowCount(); i++) {
            expectedUserList.add(rowToUser(expectedTable, i));
        }
        List<User> actualUserList = userDAO.getUserList();
        Assert.assertEquals(expectedUserList, actualUserList);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void saveAndUpdate() throws Exception {
        System.out.println("save start");
        final String afterSaveTable = "/data_set/user/afterSave.xml";
        final String afterUpdateTable = "/data_set/user/afterUpdate.xml";
        User user;
        ITable expected;
        ITable actual;
        dbTestHelper.fill(EMPTY_TABLE);

        //Test save
        expected = dbTestHelper.getTableFromFile(TABLE, afterSaveTable);
        user = rowToUser(expected, 0);
        userDAO.save(user);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEquals(expected, actual);

        //Test update
//        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdateTable);
//        user = rowToUser(expected, 0);
//        userDAO.update(user);
//        actual = dbTestHelper.getTableFromSchema(TABLE);
//        Assertion.assertEquals(expected, actual);
//        System.out.println("save end");
    }

    @Test
    @Transactional
    public void saveAndUpdateList() throws Exception {
        final String afterSaveListTable = "/data_set/user/afterSaveList.xml";
        final String afterUpdateListTable = "/data_set/user/afterUpdateList.xml";
        User user1;
        User user2;
        List<User> userList = new ArrayList<>(2);
        ITable expected;
        ITable actual;
        dbTestHelper.fill(EMPTY_TABLE);

        //Test save
        expected = dbTestHelper.getTableFromFile(TABLE, afterSaveListTable);
        user1 = rowToUser(expected, 0);
        user2 = rowToUser(expected, 1);
        userList.add(user1);
        userList.add(user2);
        userDAO.save(userList);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEquals(expected, actual);

        //Test update
        expected = dbTestHelper.getTableFromFile(TABLE, afterUpdateListTable);
        user1 = rowToUser(expected, 0);
        user2 = rowToUser(expected, 1);
        userList.clear();
        userList.add(user1);
        userList.add(user2);
        userDAO.update(userList);
        actual = dbTestHelper.getTableFromSchema(TABLE);
        Assertion.assertEquals(expected, actual);

    }


    private User rowToUser(ITable table, int rowNum) throws Exception {
        User user = new User();

        user.setName((String) table.getValue(rowNum, "u_name"));
        user.setPassword((String) table.getValue(rowNum, "u_password"));
        user.setEmail((String) table.getValue(rowNum, "email"));
        user.setMale(Boolean.valueOf((String) table.getValue(rowNum, "male")));
        user.setCreationDate(TimeWizard.stringToDate((String) table.getValue(rowNum, "creation_date"), DBTestHelper.DATE_PATTERN));
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
