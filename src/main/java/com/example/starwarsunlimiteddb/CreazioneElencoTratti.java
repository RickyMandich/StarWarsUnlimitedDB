package com.example.starwarsunlimiteddb;

public class CreazioneElencoTratti{
    public static void main(String[] args) {
        /*
        String[][] tratti = getFileMatrice("tratti.txt");
        System.out.println("fuori print prima");
        print(tratti);
        System.out.println("fuori print dopo");
        /**/
        String[] tratti = getFile("tratti.txt");
        try{
            tratti = input(tratti);
        }catch (StackOverflowError e){
            System.out.println("memoria piena, salvo su file, poi riesegui il programma per proseguire con il salvataggio");
        }finally{
            tratti = orderAndCompact(tratti);
            output(tratti);
        }/**/
    }

    private static void print(String[][] tratti) {
        System.out.println("dentro print");
        for(String[] row : tratti) {
            for(String line : row) {
                System.out.print(line + "\t\t");
            }
            System.out.println();
        }
    }

    private static String[] orderAndCompact(String[] tratti){
        boolean scambio = true;
        while (scambio){
            scambio = false;
            for(int i=0;i<tratti.length-1;i++){
                if(tratti[i].compareTo(tratti[i+1])>0){
                    scambio = true;
                    String temp = tratti[i];
                    tratti[i] = tratti[i+1];
                    tratti[i+1] = temp;
                }
            }
        }
        for(int i=1;i<tratti.length;i++){
            if(tratti[i].compareTo(tratti[i-1]) == 0){
                tratti = delete(i--, tratti);
            }
        }
        return tratti;
    }

    public static String[] delete(int h, String[] tratti){
        String[] newTabel = new String[tratti.length-1];
        for(int i=0, j=0;j<newTabel.length;j++, i++){
            if(i==h) i++;
            newTabel[j] = tratti[i];
        }
        return newTabel;
    }

    private static void output(String[] tratti){
        java.io.FileWriter writer = null;
         try {
            writer = new java.io.FileWriter("tratti.txt");
            for(String tratto:tratti){
                writer.append(tratto.concat("\n"));
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }finally {
             try {
                writer.close();
            } catch (java.io.IOException | NullPointerException ignore) {}
         }
    }

    private static String[] input(String[] tratti){
        String line;
        if(!(line = getString()).isEmpty()){
            return input(aggiungiTratto(tratti, line));
        }else{
            return tratti;
        }
    }

    public static String getString(){
        try{
            return new java.util.Scanner(System.in).nextLine();
        }catch (java.util.InputMismatchException e){
            return getString();
        }
    }

    public String[][] getFileMatrice(String fileName){
        java.io.BufferedReader reader = null;
        try{
            String line;
            reader = new java.io.BufferedReader(new java.io.FileReader(fileName));
            String[] trattiLine = new String[0];
            while((line = reader.readLine()) != null){
                trattiLine = aggiungiTratto(trattiLine, line);
            }
            String[][] tratti = new String[0][10];
            for(String tratto:trattiLine){
                tratti = add(tratti, tratto);
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

    public static String[] getFile(String fileName){
        java.io.BufferedReader reader = null;
        try{
            String line;
            reader = new java.io.BufferedReader(new java.io.FileReader(fileName));
            String[] tratti = new String[0];
            while((line = reader.readLine()) != null){
                tratti = aggiungiTratto(tratti, line);
                System.out.println(line);
            }
            return tratti;
        }catch (java.io.IOException e){
            return new String[0];
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
                for(int j=0;j<tratti[i].length;j++){
                    newTratti[i][j] = tratti[i][j];
                }
            }
            newTratti[tratti.length][0] = tratto;
            tratti = newTratti;
        }
        return tratti;
    }

    private static String[] aggiungiTratto(String[] oldTratti, String line){
        String[] newTratti = new String[oldTratti.length+1];
        System.arraycopy(oldTratti, 0, newTratti, 0, oldTratti.length);
        newTratti[oldTratti.length] = line;
        return newTratti;
    }
}