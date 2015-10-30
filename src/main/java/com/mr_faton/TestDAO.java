package com.mr_faton;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.10.2015
 */
public class TestDAO extends JdbcDaoSupport {
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save() throws Exception{
        String SQL1 = "" +
                "INSERT INTO tweagle.not_exists_synonym (word) VALUE ('happy') ON DUPLICATE KEY UPDATE used = used + 1;";
        getJdbcTemplate().update(SQL1);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveE() throws Exception{
        String SQL1 = "" +
                "INSERT INTO tweagle.not_exists_synonym (word) VALUE ('happy') ON DUPLICATE KEY UPDATE used = used + 1;";
        getJdbcTemplate().update(SQL1);
        throw new RuntimeException();
    }
}
