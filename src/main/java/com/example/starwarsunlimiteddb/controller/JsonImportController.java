package com.example.starwarsunlimiteddb.controller;

import com.example.starwarsunlimiteddb.Deck;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Controller
public class JsonImportController {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/uploadJsonDeck")
    public String showUploadForm() {
        return "importFile";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus(){
        return "upload-status";
    }

    @PostMapping("/upload-json")
    public String uploadJson(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Seleziona un file JSON da caricare");
            return "redirect:/uploadStatus";
        }

        try {
            // Leggi il contenuto del file JSON
            String jsonContent = new String(file.getBytes());
            
            // Converti il JSON in un oggetto Java (in questo caso, una Map)
            Map<String, Object> jsonData = objectMapper.readValue(jsonContent, Map.class);
            
            // Qui puoi elaborare i dati JSON come necessario
            // Per esempio, potresti salvare i dati nel database
            processJsonData(jsonData);

            redirectAttributes.addFlashAttribute("message", 
                "File JSON importato con successo: " + file.getOriginalFilename()
            );
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Errore nell'importazione del file JSON");
        }

        return "redirect:/uploadStatus";
    }

    private void processJsonData(Map<String, Object> jsonData) {
        System.out.println("Elaborazione dei dati JSON: " + jsonData);
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")){
            try(Statement stmt = conn.createStatement()){
                String json = jsonData.toString();
                Deck myDeck = new Deck(json);
                System.out.println(myDeck.getInsertSql());
                System.out.println("ho fatto " + stmt.executeUpdate(myDeck.getInsertSql()) + " modifiche");
            }catch (SQLException e){
                System.out.println("statement");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("connection");
            e.printStackTrace();
        }
    }
}
