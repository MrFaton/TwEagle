package com.mr_faton.core.dao.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by Mr_Faton on 20.10.2015.
 */
public class AnyDataAdder {
    private static final String JDBC_URL =
            "jdbc:mysql://127.0.0.1:3306/tweagle?user=Mr_Faton&password=123";//DB at my home
    private static final String SQL = "" +
            "INSERT IGNORE INTO tweagle.donor_users (donor_name, is_male) VALUES (?, 0)";
    private static final String PATH_TO_FILE = "C:\\females.txt";

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        int counter = 0;
        try(Scanner scanner = new Scanner(new FileInputStream(PATH_TO_FILE), "cp1251")) {
            try(Connection connection = DriverManager.getConnection(JDBC_URL)) {
                connection.setAutoCommit(false);
                try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
                    while (scanner.hasNextLine()) {
                        preparedStatement.setString(1, scanner.nextLine());
                        preparedStatement.addBatch();
                        counter++;
                    }
                    preparedStatement.executeBatch();
                }
                connection.commit();
            }
        }
        System.out.println(counter + " - inserts");
    }
}
