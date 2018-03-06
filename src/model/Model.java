package model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Model {

    public final static String DECK_FOLDER = "decks";
    public final static String FILE_PREFIX = "420_";
    private final static int CARD_RECORD_SIZE = 3;
    private final static String SEPARATOR = "~";

    private static JSONObject CARD_DB;

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

        Set e = CARD_DB.keySet();
        Object[] keys = e.toArray();

        for(int i = 0; i < keys.length; i++)
            if(keys[i].toString().matches(card_name))
                return new Card((JSONObject) CARD_DB.get(keys[i]));

        System.out.println("No match for the card: "+card_name);

        return null;
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

        Set e = CARD_DB.keySet();
        Object[] keys = e.toArray();

        for(int i = 0; i < keys.length; i++)
            if(keys[i].toString().toUpperCase().contains(card_name.toUpperCase()))
                cardL.add(new Card((JSONObject) CARD_DB.get(keys[i])));

        return cardL;
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
}
