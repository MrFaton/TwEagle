package com.mr_faton;

import com.mr_faton.core.context.AppContext;

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
            testDAO.save();
            testDAO.save();
            testDAO.save();
            testDAO.saveE();
        } catch (Exception e) {
            System.out.println("catch exception");
            e.printStackTrace();
        }
    }


}
