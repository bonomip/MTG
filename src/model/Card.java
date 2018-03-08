package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static model.Card.Color.*;

public class Card {

    private static int FIELDSIZE = 13;

    public class Color {
        public final static String PLAINS = "1";
        public final static String ISLAND = "2";
        public final static String SWAMP = "3";
        public final static String MOUNTAIN = "4";
        public final static String FOREST = "5";
    }

    public class Type {
        public final static String ARTIFACT = "Artifact";
        public final static String CREATURE = "Creature";
        public final static String ENCHANTMENT = "Enchantment";
        public final static String INSTANT = "Instant";
        public final static String LAND = "Land";
        public final static String PLANESWALKER = "Planeswalker";
        public final static String SORCERY = "Sorcery";

        public final static String ARTIFACT_LAND = "Artifact Land";
        public final static String ARTIFACT_CREATURE = "Artifact Creature";
        public final static String ENCHANTMENT_ARTIFACT = "Enchantment Artifact";
        public final static String ENCHANTMENT_CREATURE = "Enchantment Creature";
        public final static String LAND_CREATURE = "Land Creature";
    }

    private Object[] fields;    // 0 : name
                                // 1 : manaCost
                                // 2 : cmc
                                // 3 : colorIdendity
                                // 4 : supertypes
                                // 5 : types
                                // 6 : subtypes
                                // 7 : type
                                // 8 : text
                                // 9 : power
                                //10 : toughness
                                //11 : loyalty
                                //12 : id
    public boolean[] flag;



    public Card(JSONObject card){

        fields = new Object[FIELDSIZE];
        for (Object o : this.fields) o = ""; //prevent null pointer exception

        flag = new boolean[FIELDSIZE];

        Object name = card.get("name");
        if(name == null) { flag[0] = false; }
        else { fields[0] = name; flag[0] = true; }

        Object manaCost = card.get("manaCost");
        if(manaCost == null) { flag[1] = false; }
        else { fields[1] = manaCost; flag[1] = true; }

        Object cmc = card.get("cmc");
        if(cmc == null) { flag[2] = false; }
        else { fields[2] = cmc; flag[2] = true; }

        Object colorID = card.get("colorIdentity");
        if(colorID == null) { flag[3] = false; }
        else { fields[3] = colorID; flag[3] = true; }

        Object spTypes = card.get("supertypes");
        if(spTypes == null) { flag[4] = false; }
        else { fields[4] = spTypes; flag[4] = true; }

        Object types = card.get("types");
        if(types == null) { flag[5] = false; }
        else { fields[5] = types; flag[5] = true; }

        Object sbTypes = card.get("subtypes");
        if(sbTypes == null) { flag[6] = false; }
        else { fields[6] = sbTypes; flag[6] = true; }

        Object type = card.get("type");
        if(type == null) { flag[7] = false; }
        else { fields[7] = type; flag[7] = true; }

        Object text = card.get("text");
        if(text == null) { flag[8] = false; }
        else { fields[8] = text; flag[8] = true; }

        Object power = card.get("power");
        if(power == null) { flag[9] = false; }
        else { fields[9] = power; flag[9] = true; }

        Object tough = card.get("toughness");
        if(tough == null) { flag[10] = false; }
        else { fields[10] = tough; flag[10] = true; }

        Object loy = card.get("loyalty");
        if(loy == null) { flag[11] = false; }
        else { fields[11] = loy; flag[11] = true; }

        Object id = card.get("id");
        if(id == null) { flag[12] = false; }
        else { fields[12] = id; flag[12] = true; }
    }

    public Card(String basic_mana){

        this.fields = new Object[FIELDSIZE];
        for (Object o : this.fields) o = ""; //prevent null pointer exception

        this.flag = new boolean[FIELDSIZE];

        switch (basic_mana){
            case PLAINS:
                this.fields[0] = "Plains";
                this.flag[0] = true;

                this.fields[7] = "Basic Land — Plains";
                this.flag[7] = true;

                this.fields[6] = new String[] { "Plains" };
                this.flag[6] = true;

                this.fields[3] = new String[] { "W" };
                this.flag[3] = true;
            case ISLAND:
                this.fields[0] = "Island";
                this.flag[0] = true;

                this.fields[7] = "Basic Land — Island";
                this.flag[7] = true;

                this.fields[6] = new String[] { "Island" };
                this.flag[6] = true;

                this.fields[3] = new String[] { "U" };
                this.flag[3] = true;
            case SWAMP:
                this.fields[0] = "Swamp";
                this.flag[0] = true;

                this.fields[7] = "Basic Land — Swamp";
                this.flag[7] = true;

                this.fields[6] = new String[] { "Swamp" };
                this.flag[6] = true;

                this.fields[3] = new String[] { "B" };
                this.flag[3] = true;
            case MOUNTAIN:
                this.fields[0] = "Mountain";
                this.flag[0] = true;

                this.fields[7] = "Basic Land — Mountain";
                this.flag[7] = true;

                this.fields[6] = new String[] { "Mountain" };
                this.flag[6] = true;

                this.fields[3] = new String[] { "R" };
                this.flag[3] = true;
            case FOREST:
                this.fields[0] = "Forest";
                this.flag[0] = true;

                this.fields[7] = "Basic Land — Forest";
                this.flag[7] = true;

                this.fields[6] = new String[] { "Forest" };
                this.flag[6] = true;

                this.fields[3] = new String[] { "G" };
                this.flag[3] = true;
        }

        this.fields[2] = 0;
        this.flag[2] = true;

        this.fields[4] = new String[] { "Basic" };
        this.flag[4] = true;

        this.fields[5] = new String[] { "Land" };
        this.flag[5] = true;

    }



    public String getName(){
        return this.fields[0].toString();
    }

    public String getManaCost() {
        return this.flag[1] ? this.fields[1].toString() : ""; }

    public double getConvertedManaCost() { return Double.parseDouble(this.fields[2].toString()); }

    public String[] getColorID() { return toStringArray(this.fields[3]); }

    public String[] get4() { return toStringArray(this.fields[4]); }

    public String[] getTypes() { return toStringArray(this.fields[5]); }

    public String[] get6() { return toStringArray(this.fields[6]); }

    public String getFullType() { return this.fields[7].toString(); }

    public String getText() { return this.flag[8] ? this.fields[8].toString() : ""; }

    public String getPower() { return this.flag[9] ? this.fields[9].toString() : ""; }

    public String getToughness() { return this.flag[10] ? this.fields[10].toString() : ""; }

    public String getLoyalty() { return this.flag[11] ? this.fields[11].toString() : ""; }

    public String getPowerOrLoyalty(){
        return this.getPower().length() == 0 ? this.getLoyalty() : this.getPower()+"/"+this.getToughness();
    }

    public String getId(){
        return this.flag[12] ? this.fields[12].toString() : "";
    }

    private String[] toStringArray(Object o){
        JSONArray a = (JSONArray) o;
        if(a == null) return new String[]{"C"};
        String[] r = new String[a.size()];
        for (int i = 0; i < r.length; i++) r[i] = a.get(i).toString();
        return r;
    }

    /**
     *
     * returns true if the card has AT LEAST the given type
     *
     * USE ONLY BASIC TYPES
     *
     */
    public boolean is(String type){
        for( String s : this.getTypes() )
            if(s.equals(type)) return true;
        return false;
    }

    /**
     *
     * return true if the card is ONLY of the given type
     *
     * USE ONLY BASIC TYPES
     *
     */
    public boolean isOnly(String type) {
        return this.getTypes().length == 1 && this.getTypes()[0].equals(type);
    }



    @Override
    public String toString() {
            StringBuilder s = new StringBuilder();
            s.append(this.getName()).append("\t").append(this.getManaCost()).append("\n\n");
            s.append(this.getFullType()).append("\n\n");
            s.append("\t").append(this.getText()).append("\n\n");
            s.append("\t\t\t").append(this.getPower().length() == 0 ? "" : (this.getPower()+"/"+this.getToughness()) ).append(this.getLoyalty());
            return s.toString();
    }
}
