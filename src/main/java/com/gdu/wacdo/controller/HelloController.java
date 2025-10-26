package com.gdu.wacdo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class HelloController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/error")
    public ResponseEntity<String> handleError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong");
    }


    @GetMapping("/db")
    public ResponseEntity<String> checkDbConnection() {
        try (Connection conn = dataSource.getConnection()) {
            boolean valid = conn.isValid(2); // timeout in seconds
            return ResponseEntity.ok(valid ? "Database connection is OK" : "Database connection is NOT valid");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Database connection failed: " + e.getMessage());
        }
    }



}