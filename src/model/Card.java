package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Arrays;

public class Card {

    private static int FIELDSIZE = 12;

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

        Object color = card.get("colorIdentity");
        if(color == null) { flag[3] = false; }
        else { fields[3] = color; flag[3] = true; }

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
    }

    private String[] toStringArray(Object o){
        JSONArray a = (JSONArray) o;
        String[] r = new String[a.size()];
        for (int i = 0; i < r.length; i++) r[i] = a.get(i).toString();
        return r;
    }

    public String get0(){
        return this.fields[0].toString();
    }

    public String get1() { return this.fields[1].toString(); }

    public double get2() { return Double.parseDouble(this.fields[2].toString()); }

    public String[] get3() { return toStringArray(this.fields[3]); }

    public String[] get4() { return toStringArray(this.fields[4]); }

    public String[] get5() { return toStringArray(this.fields[5]); }

    public String[] get6() { return toStringArray(this.fields[6]); }

    public String get7() { return this.fields[7].toString(); }

    public String get8() { return this.fields[8].toString(); }

    public String get9() { return this.fields[9].toString(); }

    public String get10() { return this.fields[10].toString(); }

    public int get11() { return Integer.parseInt(this.fields[11].toString()); }

    @Override
    public String toString() {
        String a = "";
        for ( int i = 0; i < this.flag.length; i++ ) a += this.flag[i] ? this.fields[i].toString()+" " : "";
        return a;
    }
}
