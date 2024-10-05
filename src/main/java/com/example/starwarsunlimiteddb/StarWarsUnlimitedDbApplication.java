package com.example.starwarsunlimiteddb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.*;
import java.util.Map;

@SpringBootApplication
@Controller
public class StarWarsUnlimitedDbApplication {

    @Autowired
    private ObjectMapper objectMapper;

    Utente user = null;

    public String[][] add(String[][] tratti, String tratto){
        if(tratti.length == 0){
            tratti = new String[1][10];
            tratti[0][0] = tratto;
        }else if(tratti[tratti.length-1][9] == null){
            boolean daInserire = true;
            for(int i=0;daInserire & i<tratti[tratti.length-1].length;i++){
                if(tratti[tratti.length-1][i] == null){
                    tratti[tratti.length-1][i] = tratto;
                    daInserire = false;
                }
            }
        }else{
            String[][] newTratti = new String[tratti.length+1][10];
            for(int i=0;i<tratti.length;i++){
                System.arraycopy(tratti[i], 0, newTratti[i], 0, tratti[i].length);
            }
            newTratti[tratti.length][0] = tratto;
            tratti = newTratti;
        }
        return tratti;
    }

    public String[] getFile(String fileName){
        java.io.BufferedReader reader = null;
        try{
            String line;
            reader = new java.io.BufferedReader(new java.io.FileReader(fileName));
            String[] trattiLine = new String[0];
            while((line = reader.readLine()) != null){
                trattiLine = aggiungiCella(trattiLine, line);
            }
            return trattiLine;
        }catch (java.io.IOException e){
            return new String[0];
        }finally {
            try {
                reader.close();
            } catch (java.io.IOException | NullPointerException ignore) {}
        }
    }

    private String[] aggiungiCella(String[] oldArray, String line){
        String[] newArray = new String[oldArray.length+1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = line;
        return newArray;
    }

    public String join(String[] array, String concat){
        String ret = "";
        for (int i = 0; i < array.length; i++) {
            ret = ret.concat(array[i] + (i+1 != array.length ? concat : ""));
        }
        return ret;
    }

	private String selectToString(ResultSet rs, String body) throws SQLException{
		String[] tabel = new String[0];
		String label = "";
		for(int i = 1; i<= rs.getMetaData().getColumnCount(); i++) {
			label = label.concat("<td>" + rs.getMetaData().getColumnName(i) + "</a></td>");
		}
		tabel = aggiungiCella(tabel, "<tr>" + label + "</tr>");
		while(rs.next()) {
			String row = "";
			for(int i = 1; i<= rs.getMetaData().getColumnCount(); i++) {
				row = row.concat("<td> <a target=\"_blank\" href=\"https://swudb.com/card/" + rs.getString("espansione") + "/" + String.format("%03d", rs.getInt("numero")) + "\">" + rs.getString(i) + "<a></td>");
			}
			tabel = aggiungiCella(tabel, "<tr>" + row + "</tr>");
		}
		body = body.concat("<table border=\"\">");
		for(String line:tabel) {
			body = body.concat(line);
		}
		body = body.concat("</table>");
		return body;
	}

    @RequestMapping("/carte")
    @ResponseBody
    public String carte(@RequestParam (required = false, defaultValue = "") String carta){
        String body;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")) {
            try (Statement stmt = conn.createStatement()) {
                String select = "select * from carte where nome like \"%" + carta + "%\"ORDER BY ordineEspansione, numero";
                try (ResultSet rs = stmt.executeQuery(select)) {
                    body = "<form action=\"\" method=\"GET\">" +
                            "<input value=\"" + carta + "\" type=\"text\" name=\"carta\" " +
                            "placeholder=\"Inserisci il nome della carta\">" +
                            "</form>" + select;
                    body = selectToString(rs, body);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return body;
    }

    @RequestMapping("/mazzi")
    public String mazzi(@RequestParam (required = false, defaultValue = "") String mazzo){
        if(user == null) return "redirect:/login";
        String body;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")) {
            try (Statement stmt = conn.createStatement()) {
                String select = "select m.mazzo, c.* from carte c, mazzi m where c.espansione  = m.espansione and c.numero = m.numero and m.mazzo like \"%" + mazzo + "%\" and codUtente = " + user.getID() + " order by c.ordineEspansione, c.numero;";
                try (ResultSet rs = stmt.executeQuery(select)) {
                    body = "<form action=\"\" method=\"GET\">" +
                            "<input value=\"" + mazzo + "\" type=\"text\" name=\"mazzo\" " +
                            "placeholder=\"Inserisci il nome del mazzo\">" +
                            "</form>" + select;
                    body = selectToString(rs, body);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return body;
    }

    @GetMapping("/insertToDB")
	public ModelAndView insertToDB(){
        String[] tratti = getFile("tratti.txt");
        ModelAndView insertToDB = new ModelAndView("html/insertToDB");
        insertToDB.addObject("tratti", tratti);
        return insertToDB;
	}

    @GetMapping("/insertToDB/operation")
public ModelAndView insertToDBOperation(
    @RequestParam(required = false) boolean unica,
    @RequestParam String nome,
    @RequestParam(required = false) String titolo,
    @RequestParam String espansione,
    @RequestParam Integer numero,
    @RequestParam(required = false) String aspettoPrimario,
    @RequestParam(required = false) String aspettoSecondario,
    @RequestParam String tipo,
    @RequestParam(required = false) String[] tratti,
    @RequestParam(required = false) boolean imboscata,
    @RequestParam(required = false) boolean tenacia,
    @RequestParam(required = false) boolean sopraffazione,
    @RequestParam(required = false) boolean sabotatore,
    @RequestParam(required = false) boolean sentinella,
    @RequestParam(required = false) boolean schermata,
    @RequestParam(required = false) boolean incursione,
    @RequestParam(required = false) Integer valoreIncursione,
    @RequestParam(required = false) boolean recupero,
    @RequestParam(required = false) Integer valoreRecupero,
    @RequestParam(required = false) boolean contrabbando,
    @RequestParam(required = false) String valoreContrabbando,
    @RequestParam(required = false) boolean quandoGiocata,
    @RequestParam(required = false) String valoreQuandoGiocata,
    @RequestParam(required = false) boolean taglia,
    @RequestParam(required = false) String valoreTaglia,
    @RequestParam(required = false) boolean quandoSconfitta,
    @RequestParam(required = false) String valoreQuandoSconfitta,
    @RequestParam(required = false) boolean quandoAttacca,
    @RequestParam(required = false) String valoreQuandoAttacca,
    @RequestParam(required = false) boolean descrizioneEvento,
    @RequestParam(required = false) String valoreDescrizioneEvento,
    @RequestParam(required = false) boolean azione,
    @RequestParam(required = false) String valoreAzione,
    @RequestParam(required = false, defaultValue = "") String arena,
    @RequestParam(required = false) Integer costo,
    @RequestParam(required = false) Integer vita,
    @RequestParam(required = false) Integer potenza,
    @RequestParam String rarita,
    @RequestParam double prezzo,
    @RequestParam String artista
) {
    System.out.println("form ricevuto");
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/starwarsunlimited", "root", "Minecraft35?")) {
        try (Statement stmt = conn.createStatement()) {
            String insert = "insert into carte values(\"" +
                    espansione.split(";")[0] +
                    "\"," + numero +
                    ",\"" + nome +
                    "\"," + espansione.split(";")[1] +
                    "," + unica +
                    ",\"" + titolo +
                    "\",\"" + aspettoPrimario +
                    "\",\"" + aspettoSecondario +
                    "\",\"" + tipo +
                    "\",\"" + join(tratti, " * ") +
                    "\"," + (imboscata ? 1 : 0) +
                    "," + (tenacia ? 1 : 0) +
                    "," + (sopraffazione ? 1 : 0)
                    + "," + (sabotatore ? 1 : 0) +
                    "," + (sentinella ? 1 : 0) +
                    "," + (schermata ? 1 : 0) +
                    "," + (incursione ? 1 : 0) +
                    "," + (recupero ? 1 : 0) +
                    "," + (contrabbando ? 1 : 0) +
                    "," + (quandoGiocata ? 1 : 0) +
                    "," + (taglia ? 1 : 0) +
                    "," + (quandoSconfitta ? 1 : 0) +
                    "," + (quandoAttacca ? 1 : 0) +
                    "," + (descrizioneEvento ? 1 : 0) +
                    ",\"" + rarita +
                    "\"," + costo +
                    "," + vita +
                    "," + potenza +
                    "," + prezzo +
                    ",\"" + artista +
                    "\"," + valoreIncursione +
                    "," + valoreRecupero +
                    ",\"" + valoreContrabbando +
                    "\",\"" + valoreQuandoGiocata +
                    "\",\"" + valoreTaglia +
                    "\",\"" + valoreQuandoSconfitta +
                    "\",\"" + valoreQuandoAttacca +
                    "\",\"" + valoreDescrizioneEvento +
                    "\",\"" + arena +
                    "\"," + (azione ? 1 : 0) +
                    ",\"" + valoreAzione +
                    "\");";
            System.out.println(insert);
            stmt.executeUpdate(insert);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }finally {
        return insertToDB();
    }
}

    @GetMapping("/insertToDeck")
	public ModelAndView insertToDeck(){
        if(user!=null) return new ModelAndView("html/insertToDeck");
        else return new ModelAndView("redirect:html/login");
	}

    @GetMapping("/insertToDeck/operation")
    public ModelAndView insertToDeckOperation(@RequestParam String mazzo,@RequestParam String espansione,@RequestParam int nr){
        if(user == null) return new ModelAndView("redirect:html/login");
        System.out.println("form ricevuto");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")) {
            try (Statement stmt = conn.createStatement()) {
                System.out.println("sono dentro il try");
                System.out.println("ho applicato " + stmt.executeUpdate("insert into mazzi values ('" + mazzo.toLowerCase() + "', '" + espansione.toUpperCase() + "', " + nr + "," + user.getID() + ")") + " modifiche");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("redirect:html/insertToDeck");
    }

    @GetMapping("/")
    public ModelAndView home(){
        return new ModelAndView("html/home");
    }

    @GetMapping("/uploadJsonDeck")
    public String showUploadForm() {
        if(user != null) return "html/importFile";
        else return "redirect:html/login";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus(){
        return "html/upload-status";
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

    @GetMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("html/login");
    }

    @GetMapping("/login/operation")
    public ModelAndView login(@RequestParam String userID, @RequestParam String password){
        ModelAndView login = new ModelAndView("html/login");
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")){
            try(Statement stmt = conn.createStatement()){
                try(ResultSet rs = stmt.executeQuery("select * from utenti where nome = '" + userID + "' or email = '" + userID + "'")){
                    boolean notFound = true;
                    boolean findUser = false;
                    while(rs.next() && notFound){
                        findUser = true;
                        if(rs.getString("password").equals(password)){
                            notFound = false;
                            user = new Utente(rs.getString("nome"), rs.getInt("id"), rs.getString("email"), rs.getString("password"));
                            login.addObject("result", "accesso eseguito con successo");
                        }else{
                            login.addObject("result", "password errata");
                        }
                    }
                    if(!findUser){
                        login.addObject("result", "nome utente o email errati");
                    }
                }catch (SQLException e){
                    System.out.println("select");
                    e.printStackTrace();
                }
            }catch (SQLException e){
                System.out.println("statement");
            }
        }catch (SQLException e){
            System.out.println("connection");
        }
        return login;
    }

    @GetMapping("/signIn")
    public ModelAndView signIn(){
        return new ModelAndView("html/signIn");
    }

    @GetMapping("/signIn/operation")
    public ModelAndView signIn(@RequestParam String nome, @RequestParam String email, @RequestParam String password){
        ModelAndView signIn = new ModelAndView("html/signIn");
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")){
            try(Statement stmt = conn.createStatement()){
                boolean notFound = true;
                try(ResultSet rs = stmt.executeQuery("select * from utenti where nome = '" + nome + "'")){
                    while(rs.next() && notFound){
                        notFound = false;
                        signIn.addObject("result", "nome utente già in uso");
                        System.out.println("nome utente già in uso");
                    }
                }catch (SQLException e){
                    System.out.println("select nome");
                }
                if(notFound){
                    try(ResultSet rs = stmt.executeQuery("select * from utenti where email = '" + email + "'")){
                        while(rs.next() && notFound){
                            notFound = false;
                            signIn.addObject("result", "è già stato registrato un account con questa mail");
                            System.out.println("è già stato registrato un account con questa mail");
                        }
                    }catch (SQLException e){
                        System.out.println("select email");
                    }
                }
                if(notFound){
                    stmt.executeUpdate("insert into utenti (nome, email, password) value('" + nome + "', '" + email + "', '" + password + "')");
                    signIn.addObject("result", "account registrato con successo");
                    System.out.println("account registrato con successo");
                }
            }catch (SQLException e){
                System.out.println("statement");
                e.printStackTrace();
            }
        }catch (SQLException e){
            System.out.println("connection");
        }
        return signIn;
    }
/**/
    @GetMapping("profilo")
    public ModelAndView profilo(){
        if(user != null){
            ModelAndView mav = new ModelAndView("html/profilo");
            mav.addObject("nome", user.getNome());
            mav.addObject("email", user.getEmail());
            mav.addObject("password", user.getPassword());
            try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")){
            try(Statement stmt = conn.createStatement()){
                try(ResultSet rs = stmt.executeQuery("select * from mazzi where codUtente = " + user.getID() + ";")){
                    String[] mazzi = new String[0];
                    while(rs.next()){

                    }
                }catch (SQLException e){
                    System.out.println("select");
                }
            }catch (SQLException e){
                System.out.println("statement");
            }
        }catch (SQLException e){
            System.out.println("connection");
        }
            return mav;
        }else{
            return new ModelAndView("redirect:/login");
        }
    }

    public static void main(String[] args){SpringApplication.run(StarWarsUnlimitedDbApplication.class, args);}
}