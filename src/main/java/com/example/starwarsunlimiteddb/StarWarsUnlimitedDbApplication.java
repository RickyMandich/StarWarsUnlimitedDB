package com.example.starwarsunlimiteddb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.*;

@SpringBootApplication
@RestController
public class StarWarsUnlimitedDbApplication {
/*
    @GetMapping("")
    public ModelAndView InsertToDeck(){
        return new ModelAndView("InsertToDeck");
    }/**/

    @GetMapping("/insertToDeck")
	public ModelAndView insertToDeck(){
		return new ModelAndView("insertToDeck");
	}

    @GetMapping("/insertToDeck/operation")
    public ModelAndView insertToDeck(String mazzo, String espansione, String nr){
        System.out.println("form ricevuto");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306", "root", "Minecraft35?")) {
            try (Statement stmt = conn.createStatement()) {
                System.out.println("sono dentro il try");
                System.out.println("ho applicato " + stmt.executeUpdate("insert into mazzi values ('" + mazzo + "', '" + espansione + "', '" + nr + "')") + " modifiche");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return insertToDeck();
    }

    public static void main(String[] args){
        SpringApplication.run(StarWarsUnlimitedDbApplication.class, args);
    }
}