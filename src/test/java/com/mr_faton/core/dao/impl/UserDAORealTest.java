package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.table.User;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import util.Counter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 14.10.2015
 */
public class UserDAORealTest {
    private static final String BASE_NAME = "UserDAOReal";
    private static final JdbcTemplate JDBC_TEMPLATE = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");
    private static final UserDAO USER_DAO = (UserDAO) AppContext.getBeanByName("userDAO");


//    @AfterClass
//    public static void tearDown() throws Exception {
//        final String SQL = "" +
//                "DELETE FROM tweagle.users WHERE name LIKE 'UserDAOReal%';";
//
//        JDBC_TEMPLATE.update(SQL);
//    }




    @Test
    public void getUserByName() throws Exception {
        final User user = createDefaultUser();
        USER_DAO.save(user);
        USER_DAO.getUserByName(user.getName());
    }

    @Test
    public void getUserList() throws Exception {

    }


    @Test
    public void saveAndUpdate() throws Exception {
        final User user = createDefaultUser();
        USER_DAO.save(user);
        int followers = 55;
        user.setFollowers(followers);
        USER_DAO.update(user);
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        final User user1 = createDefaultUser();
        final User user2 = createDefaultUser();

        final List<User> userList = new ArrayList<>(3);
        userList.add(user1);
        userList.add(user2);

        USER_DAO.save(userList);

        user1.setMessages(35);
        user2.setMessages(40);

        USER_DAO.update(userList);
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
}
