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
        String[][] tratti = getFile("tratti.txt");
        ModelAndView insertToDB = new ModelAndView("insertToDB");
        insertToDB.addObject("tratti", tratti);
        return insertToDB;
	}

    public String[][] getFile(String fileName){
        java.io.BufferedReader reader = null;
        try{
            String line;
            reader = new java.io.BufferedReader(new java.io.FileReader(fileName));
            String[] trattiLine = new String[0];
            while((line = reader.readLine()) != null){
                trattiLine = aggiungiTratto(trattiLine, line);
                System.out.println(line);
            }
            String[][] tratti = new String[0][10];
            for(String[] row: tratti){
                for(String tratto:row){
                    tratti = add(tratti, tratto);
                }
            }
            return tratti;
        }catch (java.io.IOException e){
            return new String[0][0];
        }finally {
            try {
                reader.close();
            } catch (java.io.IOException | NullPointerException ignore) {}
        }
    }

    public String[][] add(String[][] tratti, String tratto){
        if(tratti.length == 0){
            tratti = new String[1][10];
            tratti[0][0] = tratto;
        }else if(tratti[tratti.length-1][9] == null){
            for(int i=0;i<tratti[tratti.length-1].length;i++){
                if(tratti[tratti.length-1][i] == null){
                    tratti[tratti.length-1][i] = tratto;
                }
            }
        }else{
            String[][] newTratti = new String[tratti.length+1][10];
            for(int i=0;i<tratti.length;i++){
                for(int j=0;j<tratti[i].length;j++){
                    newTratti[i][j] = tratti[i][j];
                }
            }
            newTratti[tratti.length][0] = tratto;
            tratti = newTratti;
        }
        return tratti;
    }

    private String[] aggiungiTratto(String[] oldTratti, String line){
        String[] newTratti = new String[oldTratti.length+1];
        System.arraycopy(oldTratti, 0, newTratti, 0, oldTratti.length);
        newTratti[oldTratti.length] = line;
        return newTratti;
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