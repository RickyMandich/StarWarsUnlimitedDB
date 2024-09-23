package com.example.starwarsunlimiteddb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.*;

@SpringBootApplication
@Controller
public class StarWarsUnlimitedDbApplication {

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
    public String carte(){
        String body;
        /*body = new ModelAndView("carte");
        String[] carte;
        String[] link;*/
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("select * from carte ORDER BY ordineEspansione, numero")) {
                    /*carte = aggiungiCella(new String[0],"");
                    link = aggiungiCella(new String[0],"");
                    for(int i=1;i<= rs.getMetaData().getColumnCount();i++){
                        carte[0] = carte[0].concat("<td>" + rs.getMetaData().getColumnName(i) + "</td>");
                    }
                    while(rs.next()) {
                        String line = "";
                        for(int i = 1; i<= rs.getMetaData().getColumnCount(); i++){
                            line = line.concat("<td>" + rs.getString(i) + "</td>");
                        }
                        carte = aggiungiCella(carte, line);
                        link = aggiungiCella(link,"https://swudb.com/card/" + rs.getString("espansione") + "/" + rs.getString("numero"));
                    }
                     */
                    body = selectToString(rs, "");
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }/*
        body.addObject("carte", carte);
        body.addObject("link", link);*/
        return body;
    }

    @GetMapping("/insertToDB")
	public ModelAndView insertToDB(){
        String[] tratti = getFile("tratti.txt");
        ModelAndView insertToDB = new ModelAndView("insertToDB");
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
		return new ModelAndView("insertToDeck");
	}

    @GetMapping("/insertToDeck/operation")
    public ModelAndView insertToDeckOperation(@RequestParam String mazzo,@RequestParam String espansione,@RequestParam String nr){
        System.out.println("form ricevuto");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/starwarsunlimited", "root", "Minecraft35?")) {
            try (Statement stmt = conn.createStatement()) {
                System.out.println("sono dentro il try");
                System.out.println("ho applicato " + stmt.executeUpdate("insert into mazzi values ('" + mazzo.toLowerCase() + "', '" + espansione.toUpperCase() + "', '" + nr + "')") + " modifiche");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("insertToDeck");
    }

    @GetMapping("/")
    public ModelAndView home(){
        return new ModelAndView("home");
    }

    public static void main(String[] args){SpringApplication.run(StarWarsUnlimitedDbApplication.class, args);}
}