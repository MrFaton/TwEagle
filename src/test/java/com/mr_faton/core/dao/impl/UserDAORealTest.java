package com.mr_faton.core.dao.impl;

import com.mr_faton.core.context.AppContext;
import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.table.User;
import org.junit.Test;
import util.Counter;

import java.sql.SQLException;
import java.util.Date;

/**
 * Description
 *
 * @author root
 * @version 1.0
 * @since 14.10.2015
 */
public class UserDAORealTest {
    private static final String BASE_NAME = "UserDAOReal";
    private static UserDAO userDAO = (UserDAO) AppContext.getBeanByName("userDAO");


//    @AfterClass
//    public static void tearDown() throws Exception {
//        final String SQL = "" +
//                "DELETE FROM tweagle.users WHERE name LIKE 'UserDAOReal%';";
//
//        transactionManager.doInTransaction(new Command() {
//            @Override
//            public void doCommands() throws Exception {
//                transactionManager.getConnection().createStatement().executeUpdate(SQL);
//            }
//        });
//    }

    private User createUser() {
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

    @Test
    public void save() throws SQLException {
        User user = createUser();
        userDAO.save(user);
    }
}
