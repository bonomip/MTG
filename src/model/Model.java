package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Model {

    public final static String DECK_FOLDER = "decks";

    public Model(){}


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
        return Deck.fromFile("./"+DECK_FOLDER+"/"+deckName);
    }



    private void findCardsByName(String name){
        Object object;

        try{
            object = new JSONParser().parse(new FileReader("db.json"));
        } catch (java.io.FileNotFoundException e) {
            System.out.print("file not found");
            e.printStackTrace();
            return;
        } catch (org.json.simple.parser.ParseException e){
            System.out.print("error while parsing db.json");
            e.printStackTrace();
            return;
        } catch (java.io.IOException e){
            System.out.print("input/output exception while load db.json");
            return;
        }

        JSONObject jsonObject = (JSONObject) object;
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
