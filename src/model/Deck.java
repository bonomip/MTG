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



    public String getName(){ return this.name; }

    public void setInfo(String info) { this.info = info; }

    public String getInfo() {
        return this.info;
    }


    public ArrayList<Object[]> getCardList() {
        return this.cardList;
    }

    public ArrayList<Object[]> getMainCardList(){
        ArrayList<Object[]> list = new ArrayList<>();

        for( Object[] o : this.cardList ) if( o[2].toString().equals("M") ) list.add(o);

        return list;
    }

    public ArrayList<Object[]> getSideCardList(){
        ArrayList<Object[]> list = new ArrayList<>();

        for( Object[] o : this.cardList ) if( o[2].toString().equals("S") ) list.add(o);

        return list;
    }

    public ArrayList<Object[]> getCardListOfSpecifiedType(ArrayList<Object[]> list, String type){
        ArrayList<Object[]> result = new ArrayList<>();

        switch (type){
            case Card.Type.ARTIFACT:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(type) && !((Card)o[0]).is(Card.Type.LAND) && !((Card)o[0]).is(Card.Type.CREATURE) )
                        result.add(o);
                break;
            case Card.Type.ARTIFACT_CREATURE:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(Card.Type.ARTIFACT) && ((Card)o[0]).is(Card.Type.CREATURE) )
                        result.add(o);
                break;
            case Card.Type.ARTIFACT_LAND:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(Card.Type.ARTIFACT) && ((Card)o[0]).is(Card.Type.LAND) )
                        result.add(o);
                break;

            case Card.Type.CREATURE:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(type) )
                        result.add(o);
                break;

            case Card.Type.ENCHANTMENT:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(type) && !((Card)o[0]).is(Card.Type.CREATURE) && !((Card)o[0]).is(Card.Type.ARTIFACT)  )
                        result.add(o);
                break;
            case Card.Type.ENCHANTMENT_ARTIFACT:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(Card.Type.ENCHANTMENT) && ((Card)o[0]).is(Card.Type.ARTIFACT)  )
                        result.add(o);
                break;
            case Card.Type.ENCHANTMENT_CREATURE:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(Card.Type.ENCHANTMENT) && ((Card)o[0]).is(Card.Type.CREATURE)  )
                        result.add(o);
                break;

            case Card.Type.INSTANT:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(type) )
                        result.add(o);
                break;

            case Card.Type.LAND:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(type) && !((Card)o[0]).is(Card.Type.CREATURE) )
                        result.add(o);
                break;
            case Card.Type.LAND_CREATURE:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(Card.Type.LAND) && ((Card)o[0]).is(Card.Type.CREATURE) )
                        result.add(o);
                break;

            case Card.Type.PLANESWALKER:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(type) )
                        result.add(o);
                break;
            case Card.Type.SORCERY:
                for( Object[] o : list )
                    if( ((Card)o[0]).is(type) )
                        result.add(o);
                break;
        }

        return result;
    }

    public ArrayList<Object[]> getCardListOfGenericType(ArrayList<Object[]> list, String type){
        ArrayList<Object[]> result = new ArrayList<>();

        switch (type){
            case Card.Type.ARTIFACT:
                for( Object[] o : list )
                    if( ((Card)o[0]).isOnly(type))
                        result.add(o);
                break;
            case Card.Type.CREATURE:
                for( Object[] o : list )
                    if( ((Card)o[0]).isOnly(type) )
                        result.add(o);
                break;
            case Card.Type.ENCHANTMENT:
                for( Object[] o : list )
                    if( ((Card)o[0]).isOnly(type) )
                        result.add(o);
                break;
            case Card.Type.INSTANT:
                for( Object[] o : list )
                    if( ((Card)o[0]).isOnly(type) )
                        result.add(o);
                break;
            case Card.Type.LAND:
                for( Object[] o : list )
                    if( ((Card)o[0]).isOnly(type) )
                        result.add(o);
                break;
            case Card.Type.PLANESWALKER:
                for( Object[] o : list )
                    if( ((Card)o[0]).isOnly(type) )
                        result.add(o);
                break;
            case Card.Type.SORCERY:
                for( Object[] o : list )
                    if( ((Card)o[0]).isOnly(type) )
                        result.add(o);
                break;
        }

        return result;
    }



    public void addCard(Object[] card){
        if(card[0] == null) return;

        for(Object[] o : this.cardList)
            if( ((Card)o[0]).getName().equals(((Card)card[0]).getName()) && o[2].toString().equals(card[2].toString()) ) {
                o[1] = Integer.parseInt(o[1].toString())+Integer.parseInt(card[1].toString());
                return;
            }

        this.cardList.add(card);
    }

    public void removeCard(Object[] card){
        this.cardList.remove(card);
    }

    public void changeNumberOf(Object[] card, int newValue){
        if(newValue == 0) { this.cardList.remove(card); return; }
        card[1] = newValue;
    }

    public void switchCard(Object[] card){
        this.removeCard(card);
        card[2] = card[2].toString().equals("S") ? "M" : "S";
        this.addCard(card);
    }


    public String toString(){

        String s = "";

        for(int i = 0; i<this.cardList.size(); i++){
            s += this.cardList.get(i)[0]+" n: "+this.cardList.get(i)[1]+"\n";
        }

        return this.name+"\n"+this.info+"\n\n"+s;
    }

}
