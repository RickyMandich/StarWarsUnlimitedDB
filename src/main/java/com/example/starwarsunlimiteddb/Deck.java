package com.example.starwarsunlimiteddb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deck {
    public String nome;
    public Card leader;
    public Card base;
    public Card[] deck = new Card[0];
    public Card[] sideboard = new Card[0];
    public Card[] carte = new Card[0];

    public Deck(String json) {
        try {
            Matcher nome = Pattern.compile("\\{metadata=\\{name=([A-z ]*),").matcher(json);
            nome.find();
            this.nome = nome.group(1);
            Matcher leader = Pattern.compile("leader=\\{id=(SHD)?(SOR)?(TWI)?_([0-9][0-9][0-9])").matcher(json);
            leader.find();
            String setLeader;
            if(leader.group(1) != null) {
                setLeader = leader.group(1);
            } else if(leader.group(2) != null) {
                setLeader = leader.group(2);
            } else if(leader.group(3) != null) {
                setLeader = leader.group(3);
            }else{
                throw new IllegalStateException("Json non compatibile");
            }
            this.leader = new Card(setLeader, Integer.parseInt(leader.group(4)));
            Matcher base = Pattern.compile("base=\\{id=(SHD)?(SOR)?(TWI)?_([0-9][0-9][0-9])").matcher(json);
            base.find();
            String setBase;
            if(base.group(1) != null) {
                setBase = base.group(1);
            } else if(base.group(2) != null) {
                setBase = base.group(2);
            } else if(base.group(3) != null) {
                setBase = base.group(3);
            }else{
                throw new IllegalStateException("Json non compatibile");
            }
            this.base = new Card(setBase, Integer.parseInt(base.group(4)));
            Matcher deck = Pattern.compile("deck=\\[([^\\[\\]].*?)?\\]").matcher(json);
            deck.find();
            String deckString = deck.group(1);
            if(deckString == null) deckString = "";
            Matcher carteDeck = Pattern.compile("\\{id=(SHD)?(SOR)?(TWI)?_([0-9][0-9][0-9]), count=([0-9])\\}").matcher(deckString);
            while(carteDeck.find()){
                String setCarta;
                if(carteDeck.group(1) != null){
                    setCarta = carteDeck.group(1);
                } else if(carteDeck.group(2) != null){
                    setCarta = carteDeck.group(2);
                } else if(carteDeck.group(3) != null){
                    setCarta = carteDeck.group(3);
                } else{
                    throw new IllegalStateException("Json non compatibile");
                }
                for (int i = 0; i < Integer.parseInt(carteDeck.group(5)); i++) {
                    this.deck = add(this.deck, new Card(setCarta, Integer.parseInt(carteDeck.group(4))));
                }
            }
            Matcher sideboard = Pattern.compile("sideboard=\\[([^\\[\\]].*?)?\\]").matcher(json);
            sideboard.find();
            String sideboardString = sideboard.group(1);
            if(sideboardString == null) sideboardString = "";
            Matcher carteSideboard = Pattern.compile("\\{id=(SHD)?(SOR)?(TWI)?_([0-9][0-9][0-9]), count=([0-9])\\}").matcher(sideboardString);
            while(carteSideboard.find()){
                String setCarta;
                if(carteSideboard.group(1) != null){
                    setCarta = carteSideboard.group(1);
                } else if(carteSideboard.group(2) != null){
                    setCarta = carteSideboard.group(2);
                } else if(carteSideboard.group(3) != null){
                    setCarta = carteSideboard.group(3);
                } else{
                    throw new IllegalStateException("Json non compatibile");
                }
                for (int i = 0; i < Integer.parseInt(carteSideboard.group(5)); i++) {
                    this.sideboard = add(this.sideboard, new Card(setCarta, Integer.parseInt(carteSideboard.group(4))));
                }
            }
        } catch (IllegalStateException e) {
            System.out.println("json non compatibile");
            throw e;
        }
        carte = add(carte, leader);
        carte = add(carte, base);
        for(Card c:deck){
            carte = add(carte, c);
        }
        for(Card c:sideboard){
            carte = add(carte, c);
        }
    }

    public Card[] add(Card[] oldCards, Card card) {
        Card[] newCards = new Card[oldCards.length + 1];
        System.arraycopy(oldCards, 0, newCards, 0, oldCards.length);
        newCards[oldCards.length] = card;
        return newCards;
    }

    public static void main(String[] args){
        Deck deck = new Deck("{metadata={name=Cad Bane verde, author=Ssppoocckk}, leader={id=SHD_014, count=1}, secondleader=null, base={id=SHD_022, count=1}, deck=[{id=SHD_229, count=3}, {id=SHD_086, count=3}, {id=SHD_231, count=3}, {id=SHD_210, count=3}, {id=SHD_225, count=1}, {id=SHD_121, count=2}, {id=SHD_129, count=2}, {id=SOR_209, count=2}, {id=SHD_211, count=3}, {id=SHD_085, count=3}, {id=SOR_213, count=3}, {id=SHD_187, count=3}, {id=SHD_089, count=2}, {id=SHD_212, count=3}, {id=SOR_207, count=3}, {id=SHD_219, count=3}, {id=SHD_209, count=3}, {id=SHD_193, count=1}, {id=SHD_091, count=2}, {id=SHD_080, count=3}, {id=SHD_215, count=2}, {id=SHD_255, count=3}], sideboard=[]}");
        System.out.println(deck);
        System.out.println(deck.getInsertSql());
    }

    public static class Card {
        public String expansion;
        public int number;

        public Card(String set, int number) {
            this.expansion = set;
            this.number = number;
        }

        @Override
        public String toString(){
            return "Card=" +
                    expansion + ' ' +
                    number;
        }
    }

    public String toString(){
        String info = "";
        info = info.concat("nome:\t\t\t" + nome + "\n");
        info = info.concat("leader:\t\t\t" + leader + "\n");
        info = info.concat("base:\t\t\t" + base + "\n");
        info = info.concat("deck:\n");
        for (Card card : deck) {
            info = info.concat("\t\t\t\t" + card.toString() + "\n");
        }
        info = info.concat("sideboard:\n");
        for (Card card : sideboard) {
            info = info.concat("\t\t\t" + card.toString() + "\n");
        }
        return info;
    }

    public String getInsertSql(){
        String insert = "insert into mazzi\nvalues";
        for(Card c:carte){
            insert = insert.concat("('" + nome + "','" + c.expansion + "'," + c.number + "),\n");
        }
        char[] array = insert.toCharArray();
        array[array.length-2] = ';';
        return new String(array);
    }
}