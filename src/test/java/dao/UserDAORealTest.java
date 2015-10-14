package dao;

import com.mr_faton.core.dao.UserDAO;
import com.mr_faton.core.dao.impl.UserDAOReal;
import com.mr_faton.core.pool.db_connection.TransactionManager;
import com.mr_faton.core.table.User;
import com.mr_faton.core.util.Command;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.Counter;

import javax.jws.soap.SOAPBinding;
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
    private static TransactionManager transactionManager;
    private static UserDAO userDAO;

    @BeforeClass
    public static void setUp() throws Exception {
        transactionManager = TransactionMenagerHolder.getTransactionManager();
        userDAO = new UserDAOReal(transactionManager);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        final String SQL = "" +
                "DELETE FROM tweagle.users WHERE name LIKE 'UserDAOReal%';";

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                transactionManager.getConnection().createStatement().executeUpdate(SQL);
            }
        });
    }

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
    public void getUserByName() throws Exception {
        final User user = createUser();

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.save(user);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.getUserByName(user.getName());
            }
        });
    }

    @Test
    public void getUserList() throws Exception {
        final List<User> userList = new ArrayList<>(3);
        userList.add(createUser());
        userList.add(createUser());

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.save(userList);
            }
        });

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                List<User> takenList = userDAO.getUserList();
                Assert.assertTrue(takenList.size() >= 2);
            }
        });
    }


    @Test
    public void saveAndUpdate() throws Exception {
        final User user = createUser();

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.save(user);
            }
        });

        user.setMessages(101);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.update(user);
            }
        });
    }

    @Test
    public void saveAndUpdateList() throws Exception {
        final User user1 = createUser();
        final User user2 = createUser();

        final List<User> userList = new ArrayList<>(3);
        userList.add(user1);
        userList.add(user2);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.save(userList);
            }
        });

        user1.setMessages(102);
        user2.setMessages(103);

        transactionManager.doInTransaction(new Command() {
            @Override
            public void doCommands() throws Exception {
                userDAO.update(userList);
            }
        });
    }
}
