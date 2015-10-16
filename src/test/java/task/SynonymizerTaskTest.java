package task;

import com.mr_faton.core.dao.MessageDAO;
import com.mr_faton.core.dao.SynonymizerDAO;
import com.mr_faton.core.exception.NoSuchEntityException;
import com.mr_faton.core.table.Message;
import com.mr_faton.core.task.impl.SynonymizerTask_Old;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Description
 *
 * @author Mr_Faton
 * @version 1.0
 * @since 16.10.2015
 */
public class SynonymizerTaskTest {

    @Test
    public void execute() throws SQLException, NoSuchEntityException {
        SynonymizerDAO synonymizerDAO = mock(SynonymizerDAO.class);
        MessageDAO messageDAO = mock(MessageDAO.class);

        List<String> synonymList = new ArrayList<>(4);
        synonymList.add("synonym");
        synonymList.add("synonym");
        synonymList.add("synonym");

        Message message = new Message();
        message.setOwner("Test");
        message.setMessage("@oone oone, ttwo)) tthree one");
        List<Message> messageList = new ArrayList<>(2);
        messageList.add(message);

        when(synonymizerDAO.getSynonyms(anyString())).thenReturn(synonymList);
        when(messageDAO.getUnSynonymizedMessages(anyInt())).thenReturn(messageList);

        SynonymizerTask_Old synonymizerTask = new SynonymizerTask_Old(messageDAO, synonymizerDAO);

        synonymizerTask.update();
        synonymizerTask.execute();

        Assert.assertEquals("@oone synonym, synonym)) synonym one", message.getMessage());
    }


//    @Test
//    public void execute2() throws SQLException, NoSuchEntityException {
//        SynonymizerDAO synonymizerDAO = Mockito.mock(SynonymizerDAO.class);
//        MessageDAO messageDAO = Mockito.mock(MessageDAO.class);
//
//        List<String> oneList = new ArrayList<>(4);
//        oneList.add("oone+");
//        oneList.add("oone++");
//        oneList.add("oone+++");
//        List<String> twoList = new ArrayList<>(4);
//        twoList.add("ttwo+");
//        twoList.add("ttwo++");
//        twoList.add("ttwo+++");
//        List<String> threeList = new ArrayList<>(4);
//        threeList.add("tthree+");
//        threeList.add("tthree++");
//        threeList.add("tthree+++");
//
//        Message message = new Message();
//        message.setOwner("Test");
//        message.setMessage("oone, ttwo)) tthree one");
//        List<Message> messageList = new ArrayList<>(3);
//        messageList.add(message);
//
//        System.out.println(messageList);
//
//        Mockito.when(synonymizerDAO.getSynonyms("oone")).thenReturn(oneList);
//        Mockito.when(synonymizerDAO.getSynonyms("ttwo")).thenReturn(twoList);
//        Mockito.when(synonymizerDAO.getSynonyms("tthree")).thenReturn(threeList);
//
//        Mockito.when(messageDAO.getUnSynonymizedMessages(10)).thenReturn(messageList);
//
//        SynonymizerTask synonymizerTask = new SynonymizerTask(messageDAO, synonymizerDAO);
//
//        synonymizerTask.update();
//        synonymizerTask.execute();
//
//        System.out.println(messageList);
//    }
}
