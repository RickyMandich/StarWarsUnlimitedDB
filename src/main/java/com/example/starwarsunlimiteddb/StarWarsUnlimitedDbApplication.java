package com.example.starwarsunlimiteddb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.*;

@SpringBootApplication
@Controller
public class StarWarsUnlimitedDbApplication {
    @GetMapping("/insertToDeck")
	public ModelAndView insertToDeck(){
		return new ModelAndView("insertToDeck");
	}

    @GetMapping("/insertToDeck/operation")
    public ModelAndView insertToDeckOperation(@RequestParam String mazzo,@RequestParam String espansione,@RequestParam String nr){
        System.out.println("form ricevuto");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.8.170:3306/world", "formazione", "129EL@doiej!")) {
            try (Statement stmt = conn.createStatement()) {
                System.out.println("sono dentro il try");
                System.out.println("ho applicato " + stmt.executeUpdate("insert into mazzi values ('" + mazzo + "', '" + espansione + "', '" + nr + "')") + " modifiche");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("insertToDeck");
    }

    public static void main(String[] args){
        SpringApplication.run(StarWarsUnlimitedDbApplication.class, args);
    }
}