package com.gdu.wacdo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
public class WacdoApplication implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;


    public static void main(String[] args) {
        SpringApplication.run(WacdoApplication.class, args);
        System.out.println("[SUCCESS] Spring Server currently running...");
    }

    @Override
    public void run(String... args) {
        try {
            if (this.checkConnection()) {
                System.out.println("[SUCCESS] Database connection established...");
            } else {
                System.out.println("[ERROR] Database connection is NOT valid");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("Failed init : " + e.getMessage());
            System.exit(0);
        }
    }


    private boolean checkConnection() throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(2);
        } catch (SQLException e) {
            System.out.println("JDBC Driver failed : " + e.getMessage());
            return false;
        }
    }

}
