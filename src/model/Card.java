package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Arrays;

public class Card {

    public String name;

    public String text; //JSON key: "text"

    public String manaCost; //JSON key: "manaCost"

    public double cmc; //JSON key: "cmc"

    public String[] colors; //JSON key: "colorIdentity"

    public String[] superTypes; //JSON key: "supertypes"

    public String[] types; //JSON key: "types"

    public String[] subTypes; //JSON key: "subtypes"

    public String fullType; //JSON key: "type"

    public String power; //JSON key: "power"

    public String toughness; //JSON key: "toughness"

    public int loyalty; //JSON key: "loyalty"


    public Card(JSONObject card){

        this.name = card.get("name").toString();

        this.manaCost = card.get("manaCost").toString();

        String s = card.get("cmc").toString();
        this.cmc = (s==null?-1:Double.parseDouble(s));

        this.colors = toStringArray(card, "colorIdentity");

        this.superTypes = toStringArray(card, "supertypes");

        this.types = toStringArray(card, "types");

        this.subTypes = toStringArray(card, "subtypes");

        this.fullType = card.get("type").toString();

        this.text = card.get("text").toString();

        this.power = card.get("power").toString();

        this.toughness = card.get("toughness").toString();

        s = card.get("loyalty").toString();
        this.loyalty = (s==null?-1:Integer.parseInt(s));
    }

    private String[] toStringArray(JSONObject from, String what){
        JSONArray a = (JSONArray) from.get(what);
        String[] r = new String[a.size()];
        for (int i = 0; i < r.length; i++) r[i] = a.get(i).toString();
        return r;
    }

    @Override
    public String toString() {
        return "Card name: "+this.name+
                (this.manaCost==null?"":"\nMana cost: "+this.manaCost)+
                "\nColors: "+ Arrays.toString(this.colors) +
                "\nType: "+this.fullType+
                (this.power == null?"":"\nPower: "+this.power)+
                (this.toughness == null?"":"\nToughness: "+this.toughness)+
                (this.loyalty == -1?"":"\nLoyalty: "+this.loyalty);
    }
}
