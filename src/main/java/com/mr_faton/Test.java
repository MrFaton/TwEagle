package com.mr_faton;

import com.mr_faton.core.context.AppContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.10.2015
 */
public class Test {
    static TestDAO testDAO;
    public static void main(String[] args) {
        testDAO = (TestDAO) AppContext.getBeanByName("testDao");
        try {
            testDAO.saveOrUpdate();
        } catch (Exception e) {
            System.out.println("catch exception");
            e.printStackTrace();
        }
    }


}
