package control;

import model.*;
import view.HomeView;

import java.io.File;
import java.io.FileNotFoundException;

public class Control {

    Model model;
    HomeView home;

    public Control(){
        super();
        this.model = new Model();
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


    public void openDeck(String deckName){
        Deck deck = null;
        try {
            deck = this.model.getDeck(deckName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if( deck == null ) return;

        this.home.setCurrentDeck(deck);
        this.home.updateDeck();
    }

}
