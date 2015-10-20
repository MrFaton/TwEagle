package com.mr_faton.core.dao.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Mr_Faton on 20.10.2015.
 */
public class AnyDataAdder {
    private static final String JDBC_URL =
            "jdbc:mysql://127.0.0.1:3306/tweagle?user=Mr_Faton&password=123";//DB at my home
    private static final String SQL = "" +
            "INSERT INTO tweagle.donor_users (donor_name, is_male) VALUES (?, 0)";
    private static final String PATH_TO_FILE = "C:\\females.txt";

    public static void main(String[] args) throws SQLException, FileNotFoundException {
        int counter = 0;
        try(Scanner scanner = new Scanner(new FileInputStream(PATH_TO_FILE), "cp1251")) {
            Set<String> donorSet = new HashSet<>();
            try(Connection connection = DriverManager.getConnection(JDBC_URL)) {
                connection.setAutoCommit(false);
                try(PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
                    while (scanner.hasNextLine()) {
                        String name = scanner.nextLine().toLowerCase();
                        if (name.length() >= 30) System.out.println(name);
                        donorSet.add(name);
                    }
                    for (String donorName : donorSet) {
                        preparedStatement.setString(1, donorName);
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
