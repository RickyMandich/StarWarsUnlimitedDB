package com.example.starwarsunlimiteddb;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Utente{
    private String nome;
    private int id;
    private String email;
    private String password;

    public Utente(String nome, int id, String email, String password) {
        this.nome = nome;
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Utente(ResultSet rs, int i) throws SQLException{
        if(rs.absolute(i)){
            nome = rs.getString("nome");
            id = rs.getInt("idUtente");
            email = rs.getString("email");
            password = rs.getString("password");
        }
    }

    public String getNome(){
        return nome;
    }

    public int getID(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password.replaceAll(".", "*");
    }
}
