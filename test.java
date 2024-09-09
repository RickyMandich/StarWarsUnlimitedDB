public class test {
    public static void main(String[] args) {
        String[] tratti = getFile("tratti.txt");
        try{
            tratti = input(tratti);
        }catch (StackOverflowError e){
            System.out.println("memoria piena, salvo su file, poi riesegui il programma per proseguire con il salvataggio");
        }finally{
            tratti = orderAndCompact(tratti);
            output(tratti);
        }
    }

    private static String[] orderAndCompact(String[] tratti){

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
            return aggiungiTratto(tratti, line);
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

    public static String[] getFile(String fileName){
        java.io.BufferedReader reader = null;
        try{
            String line;
            reader = new java.io.BufferedReader(new java.io.FileReader(fileName));
            String[] tratti = new String[0];
            while((line = reader.readLine()) != null){
                tratti = aggiungiTratto(tratti, line);
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

    private static String[] aggiungiTratto(String[] oldTratti, String line){
        String[] newTratti = new String[oldTratti.length+1];
        System.arraycopy(oldTratti, 0, newTratti, 0, oldTratti.length);
        newTratti[oldTratti.length] = line;
        return newTratti;
    }
}