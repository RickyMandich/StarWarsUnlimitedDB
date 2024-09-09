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

    @GetMapping("/insertToDB")
	public ModelAndView insertToDB(){
		return new ModelAndView("insertToDB");
	}

    @GetMapping("/insertToDB/operation")
    public ModelAndView insertToDBOperation(){
        System.out.println("form ricevuto");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/starwarsunlimited", "root", "Minecraft35?")) {
            try (Statement stmt = conn.createStatement()) {
                System.out.println("ho applicato " + stmt.executeUpdate("insert into carte values ()") + " modifiche");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("insertToDB");
    }

    @GetMapping("/insertToDeck")
	public ModelAndView insertToDeck(){
		return new ModelAndView("insertToDeck");
	}

    @GetMapping("/insertToDeck/operation")
    public ModelAndView insertToDeckOperation(@RequestParam String mazzo,@RequestParam String espansione,@RequestParam String nr){
        System.out.println("form ricevuto");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/starwarsunlimited", "root", "Minecraft35?")) {
            try (Statement stmt = conn.createStatement()) {
                System.out.println("sono dentro il try");
                System.out.println("ho applicato " + stmt.executeUpdate("insert into mazzi values ('" + mazzo.toLowerCase() + "', '" + espansione.toUpperCase() + "', '" + nr + "')") + " modifiche");
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