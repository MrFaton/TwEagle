package com.mr_faton;

import com.mr_faton.core.context.AppContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description
 *
 * @author Mr_Faton
 * @since 29.10.2015
 */
public class TestDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveOrUpdate() throws Exception{
        String SQL1 = "" +
                "INSERT INTO tweagle.not_exists_synonym (word) VALUE ('happy') ON DUPLICATE KEY UPDATE used = used + 1;";
//        JdbcTemplate template = (JdbcTemplate) AppContext.getBeanByName("jdbcTemplate");
//        template.update(SQL1);
        System.out.println(jdbcTemplate);
        jdbcTemplate.update(SQL1);
//        throw new RuntimeException();
    }
}
