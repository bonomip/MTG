package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

public class Model {

    public final static String DECK_FOLDER = "decks";
    private final static int CARD_RECORD_SIZE = 3;
    private final static String SEPARATOR = ";";

    private static JSONObject CARD_DB;

    public Model() throws FileNotFoundException, ParseException, IOException {
        Object o = new JSONParser().parse(new FileReader("db.json"));
        CARD_DB = (JSONObject) o;
    }


    /**
     *
     * Se non è già presente la cartella "decks" questo metodo la crea.
     *
     */
    public void checkDecks() {
        final File dir = new File(DECK_FOLDER);

        if( !dir.exists() ) { //nota: exists() -> non distingue le lettere maiuscole dalle minuscole
            dir.mkdir();
            System.out.println("create deck folder");
        }
    }


    public String[] getLocalDeckNames(){
        File dir = new File("./"+DECK_FOLDER);
        File[] files = dir.listFiles();
        String[] res = new String[files.length];
        for ( int i = 0; i < files.length; i++) if( files[i].isFile() ) res[i] = files[i].getName();
        return res;
    }


    public Deck getDeck(String deckName) throws FileNotFoundException {

        String filePath = "./"+DECK_FOLDER+"/"+deckName;
        File dk = new File(filePath);
        Scanner sc = new Scanner(dk);
        StringBuilder s = new StringBuilder();
        while (sc.hasNext()) s.append(sc.nextLine());
        String[] par = s.toString().split(SEPARATOR);

        Deck deck = new Deck(par[0], par[1]);

        //3 is card name
        //4 card number
        //5 side or main ecc.

        for(int i = 2; i < par.length; i=i+CARD_RECORD_SIZE) deck.addCard(new Object[]{ this.findCardsByName(par[i+0]), par[i+1], par[i+2] });

        return deck;

    }

    private Card findCardsByName(String card_name){

        Set e = CARD_DB.keySet();
        Object[] keys = e.toArray();

        for(int i = 0; i < keys.length; i++) if(keys[i].toString().matches(card_name)) return new Card((JSONObject) CARD_DB.get(keys[i]));

        System.out.println("No match for the card: "+card_name);

        return null;

    }

            /* //esempio di estrazione di stringhe s

            Object o = new JSONParser().parse(new FileReader("db.json"));
            JSONObject oj = (JSONObject) o;
            //JSONArray cards = new JSONArray();

            //oj.keySet(); print all cards name

            Set e = oj.keySet();
            Object[] keys = e.toArray();


            String heType = "Vaul";
            ArrayList<String> possResult = new ArrayList<>();

            for( int i = 0; i < keys.length; i++ )
            {
                if( keys[i].toString().regionMatches(true, 0, heType, 0, 3) ) possResult.add(keys[i].toString()) ;
            }

            System.out.println(possResult.toString());


    */

}
