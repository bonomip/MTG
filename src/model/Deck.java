package model;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Deck {

    private String name;

    private String info;

    private ArrayList<Object[]> cardList;

    private final static String SEPARATOR = ";";
    private final static int CARD_RECORD_SIZE = 3;


    public Deck(String[] params){
        this.name = params[0];
        this.info = params[1];

        this.cardList = new ArrayList<>();

        Object[] o = new Object[CARD_RECORD_SIZE];

        for(int i = 2; i < params.length; i=i+CARD_RECORD_SIZE){
            for(int j = 0; j < CARD_RECORD_SIZE; j++) o[j] = params[i+j];

            this.cardList.add(o);
            o = new Object[CARD_RECORD_SIZE];
        }
    }

    public void remove(String card_name){

    }


    public String getName(){ return name; }

    public String getInfo() {
        return info;
    }

    public ArrayList<Object[]> getCardList() {
        return cardList;
    }

    public static Deck fromFile(String filePath) throws FileNotFoundException {
        File dk = new File(filePath);
        Scanner sc = new Scanner(dk);
        StringBuilder s = new StringBuilder();
        while (sc.hasNext()) s.append(sc.nextLine());
        String[] par = s.toString().split(SEPARATOR);
        return new Deck(par);
    }

    public static String toFile(Deck deck){
        String s = deck.getName()+SEPARATOR;
        s += deck.getInfo()+SEPARATOR;
        for(int i = 0; i< deck.getCardList().size(); i++)
            for(int j = 0; j < CARD_RECORD_SIZE; j++)
                s += deck.getCardList().get(i)[j]+SEPARATOR;
        return s;
    }

    public String toString(){

        String s = "";

        for(int i = 0; i<this.cardList.size(); i++){
            s += this.cardList.get(i)[0]+" n: "+this.cardList.get(i)[1]+"\n";
        }

        return this.name+"\n"+this.info+"\n\n"+s;
    }

}
