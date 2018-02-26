package control;

import model.*;
import org.json.simple.parser.ParseException;
import view.Err;
import view.HomeView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Control {

    Model model;
    HomeView home;

    public Control(){
        super();
        try {
            this.model = new Model();
        } catch (ParseException e) {
            e.printStackTrace();
            view.Err.show(Err.PARSE);
        } catch (IOException e) {
            e.printStackTrace();
            view.Err.show(Err.IO);
        }
    }


    public void linkHome(HomeView homeView){
        this.home = homeView;
    }


    /**
     * Esecuzione di tutte le procedure che permettono un corretto funzionamento
     * dell'applicativo
     */
    public void setUp(){
        this.model.checkDecks();
    }


    public String[] getLocalDeckNames(){
        return this.model.getLocalDeckNames();
    }


    public boolean openDeck(String deckName){
        Deck deck = null;
        try {
            deck = this.model.getDeck(deckName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if( deck == null ) return false;

        this.home.setCurrentDeck(deck);
        this.home.updateDeckCardList();

        return true;
    }

}
