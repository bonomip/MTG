package model;

import java.util.ArrayList;

public class Deck {

    private String name;

    private String info;

    private ArrayList<Object[]> cardList; /* object[0] = Card : card
                                           * object[1] = int : number of
                                           * object[2] = String : "S" or "M" ( side or main )
                                           */

    public Deck(String name, String info){
        this.name = name;
        this.info = info;

        this.cardList = new ArrayList<>();
    }



    public String getName(){ return name; }

    public String getInfo() {
        return info;
    }

    public ArrayList<Object[]> getCardList() {
        return cardList;
    }



    public void addCard(Object[] card){
        if(card[0] == null) return;
        this.cardList.add(card);
    }

    public void removeCard(Object[] card){
        this.cardList.remove(card);
    }

    public void changeNumberOf(Object[] card, int newValue){
        if(newValue == 0) { this.cardList.remove(card); return; }
        card[1] = newValue;
    }



    public String toString(){

        String s = "";

        for(int i = 0; i<this.cardList.size(); i++){
            s += this.cardList.get(i)[0]+" n: "+this.cardList.get(i)[1]+"\n";
        }

        return this.name+"\n"+this.info+"\n\n"+s;
    }

}
