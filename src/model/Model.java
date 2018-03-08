package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import view.View;
import view.utils.Info;

import java.io.*;
import java.util.*;

public class Model {

    public final static String DECK_FOLDER = "decks";
    public final static String FILE_PREFIX = "420_";
    private final static int CARD_RECORD_SIZE = 3;
    private final static String SEPARATOR = "~";

    private static JSONObject CARD_DB;

    public class Key {
        public static final String NAME = "name";
        public static final String TEXT = "text";
        public static final String TYPE = "type";
        public static final String COLOR = "colors";
        public static final String COLORID = "colorIdentity";
        public static final String CMC = "cmc";
    }

    public Model() throws FileNotFoundException, ParseException, IOException {
        Object o = new JSONParser().parse(new FileReader("db.json"));
        CARD_DB = (JSONObject) o;
    }



    //CARD MANAGE

    /**
     *
     *
     * Questo metodo serve nel caricamento di un deck pre-esistente
     * Trova la carta nel DB col nome @param e restituisce un oggetto di tipo Card
     *
     * @param card_name
     * @return
     */
    private Card findCardByName(String card_name){

        Object o = CARD_DB.get(card_name);

        if(o == null) { System.out.println("No match for the card: "+card_name); return null; }

        return new Card((JSONObject)o);

    }

    /**
     *
     * Questo metodo restituisce una lista di carte il cui nome che metcha con il parametro dato
     *
     * @param card_name
     * @return
     */
    public ArrayList<Card> findCardsByName(String card_name){
        ArrayList<Card> cardL = new ArrayList<>();

        for(Object key : CARD_DB.keySet().toArray())
            if(key.toString().toUpperCase().contains(card_name.toUpperCase()))
                cardL.add(new Card((JSONObject) CARD_DB.get(key)));

        return cardL;
    }

    public ArrayList<Card> findCards(List<String[]> params){

        ArrayList<Card> r = new ArrayList<>();

        if(params.size() == 0) return r;

        for ( Object set : CARD_DB.keySet().toArray() ){

            boolean match = true;
            JSONObject card = (JSONObject) CARD_DB.get(set);

            for (String[] p : params ){
                if(!match) break;
                switch( p[0] ){
                    case Key.NAME:
                        match = card.get("name").toString().toUpperCase().contains(p[1].toUpperCase());
                        break;
                    case Key.TEXT:
                        match = card.get("text") != null && card.get("text").toString().toUpperCase().contains(p[1].toUpperCase());
                        break;
                    case Key.TYPE:
                        match = typeMatch(card, p[1]);
                        break;
                    case Key.COLORID:
                        match = colorIdentityMatch(card, p[1]);
                        break;
                    case Key.COLOR:
                        match = colorMatch(card, p[1]);
                        break;
                    case Key.CMC:
                        match = cmcMatch(card, p[1], p[2]);
                        break;
                }
            }
            if(match) r.add(new Card(card));
        }
        return r;
    }

    private static boolean typeMatch(JSONObject card, String toFind){
        String temp = (String) card.get("type");
        if(temp == null || temp.equals("")) return false;

        temp = " "+temp+" ";
        temp.replace("-", " ");

        String[] types = toFind.trim().split("\\s+");

        for(int i = 0; i < types.length; i++)
            if(!temp.toUpperCase().contains(" "+types[i].toUpperCase()+" "))
                return false;

        return true;
    }

    private static boolean colorIdentityMatch(JSONObject card, String toFind){
        JSONArray tempA = (JSONArray) card.get("colorIdentity");

        if(tempA == null && toFind.toUpperCase().equals("C"))
            return true;
        else if(tempA == null)
            return false;

        Object[] tempB = tempA.toArray();
        StringBuilder colors = new StringBuilder();

        for (int i = 0; i < tempB.length; i++)
            colors.append(tempB[i].toString());

        if(toFind.substring(0, 1).equals("!")){ //ricerca per carte contentni SOLO i colori indicati

            if( colors.length() != toFind.length()-1 )
                return false;

            for(int i = 1; i < toFind.length(); i++)
                if(!colors.toString().contains(toFind.substring(i, i+1).toUpperCase()))
                    return false;

        } else { //ricerca per carte contenenti i colori indicati

            for(int i = 0; i < toFind.length(); i++)
                if(!colors.toString().contains(toFind.substring(i, i+1).toUpperCase()))
                    return false;
        }
        return true;
    }

    private static boolean colorMatch(JSONObject card, String toFind){
        JSONArray tempA = (JSONArray) card.get("colors");

        if(tempA == null && toFind.toUpperCase().equals("C"))
            return true;
        else if(tempA == null)
            return false;

        Object[] tempB = tempA.toArray();
        StringBuilder colors = new StringBuilder();

        for (int i = 0; i < tempB.length; i++) {
            switch (tempB[i].toString()){
                case Colors.BLACK:
                    colors.append("B");
                    break;
                case Colors.BLUE:
                    colors.append("U");
                    break;
                case Colors.GREEN:
                    colors.append("G");
                    break;
                case Colors.RED:
                    colors.append("R");
                    break;
                case Colors.WHITE:
                    colors.append("W");
                    break;
                    default:
                        colors.append("C");
            }
        }

        if(toFind.substring(0, 1).equals("!")){ //ricerca per carte contentni SOLO i colori indicati

            if( colors.length() != toFind.length()-1 )
                return false;

            for(int i = 1; i < toFind.length(); i++)
                if(!colors.toString().contains(toFind.substring(i, i+1).toUpperCase()))
                    return false;

        } else { //ricerca per carte contenenti i colori indicati

            for(int i = 0; i < toFind.length(); i++)
                if(!colors.toString().contains(toFind.substring(i, i+1).toUpperCase()))
                    return false;
        }
        return true;
    }

    private static boolean cmcMatch(JSONObject card, String cmc, String par){
            String s = card.get("cmc").toString();
            double x = Double.parseDouble(s);
            int y = Integer.parseInt(cmc);

            switch (par){
                case "<":
                    return x < y;
                case "<=":
                    return x <= y;
                case "=":
                    return x == y;
                case ">=":
                    return x >= y;
                case ">":
                    return x > y;
            }

            return false;
    }


    //DECK MANAGE

    /**
     *
     * Se non è già presente la cartella "decks" questo metodo la crea.
     *
     */
    public void checkDecksDir() {
        final File dir = new File(DECK_FOLDER);

        if( !dir.exists() ) { //nota: exists() -> non distingue le lettere maiuscole dalle minuscole
            dir.mkdir();
            System.out.println("create deck folder");
        }
    }

    public String[] getLocalDeckNames(){
        File dir = new File("./"+DECK_FOLDER);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(FILE_PREFIX);
            }
        });
        if(files == null) return null;;
        String[] res = new String[files.length];
        for ( int i = 0; i < files.length; i++) if( files[i].isFile() ) res[i] = files[i].getName().substring(4);
        Arrays.sort(res);
        return res;
    }

    public Deck getDeck(String deckName) throws FileNotFoundException {

        String t = this.removeSeparator(deckName);

        String filePath = "./"+DECK_FOLDER+"/"+FILE_PREFIX+t;
        File dk = new File(filePath);
        Scanner sc = new Scanner(dk);
        StringBuilder s = new StringBuilder();
        while (sc.hasNext()) s.append(sc.nextLine());
        String[] par = s.toString().split(SEPARATOR);

        Deck deck = new Deck(par[0], par[1]);

        //2 is card name
        //3 card number
        //4 side or main ecc.

        for(int i = 2; i < par.length; i=i+CARD_RECORD_SIZE) deck.addCard(new Object[]{ this.findCardByName(par[i+0]), par[i+1], par[i+2] });

        return deck;

    }

    public int createDeck(String name, String info){

        if(name.equals("") || info.equals("")) return 0;

        String s = removeSeparator(name);

        String path = "./"+DECK_FOLDER+"/"+FILE_PREFIX+s;

        File deck = new File(path);

        try {

            if(!deck.createNewFile()) return 1; //file already exist

        } catch (IOException e) {

            e.printStackTrace();
            return 2; // io exception

        }

        String t = removeSeparator(info);

        String toWrite = s+SEPARATOR+t+SEPARATOR;

        try {

            FileWriter w = new FileWriter(deck);
            w.write(toWrite);
            w.close();

        } catch (IOException e) {

            e.printStackTrace();

            deck.deleteOnExit();

            return 3; // io exception after creating file
        }

        return 4;
    }

    private String removeSeparator(String s){
        StringBuilder r = new StringBuilder();
        r.append("");
        for(int i = 0; i < s.length(); i++)
            if (s.charAt(i) != "~".charAt(0)) r.append(s.charAt(i));
            else r.append("-");

        return r.toString();
    }

    public int saveDeck(Deck deck){

        String path = "./"+DECK_FOLDER+"/"+FILE_PREFIX+deck.getName();

        File f = new File(path);

        String s = this.removeSeparator(deck.getName());
        String t = this.removeSeparator(deck.getInfo());

        StringBuilder r = new StringBuilder(s+SEPARATOR+t+SEPARATOR);

        for ( Object[] o : deck.getCardList() )
            r.append(((Card) o[0]).getName()).append(SEPARATOR).append(o[1].toString()).append(SEPARATOR).append(o[2].toString()).append(SEPARATOR);

        try {

            FileWriter w = new FileWriter(f);
            w.write(r.toString());
            w.close();

        } catch (IOException e) {

            e.printStackTrace();
            return 0;
        }

        return 1;

    }

    public boolean deleteDeck(String name){
        String path = "./"+DECK_FOLDER+"/"+FILE_PREFIX+name;

        File deck = new File(path);

        return deck.delete();
    }

    public ArrayList<Object[]> processMassEntry(String mass){
        ArrayList<Object[]> result = new ArrayList<>();

        String[] temp = mass.split("\n|\r");

        for(int i = 0; i < temp.length; i++){
            String number = "";
            String name;

            int j = 0;

            while(Character.isDigit(temp[i].charAt(j))){ number += temp[i].substring(j, j+1); j++; }
            name = temp[i].substring(j+1, temp[i].length());

            Card card = findCardByName(name);
            result.add(new Object[]{ card, number, "M" });

            }

        return result;
    }
}
