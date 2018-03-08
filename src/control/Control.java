package control;

import io.magicthegathering.javasdk.api.CardAPI;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import model.*;
import org.json.simple.parser.ParseException;
import view.NewDeck;
import view.View;
import view.utils.Err;
import view.HomeView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Control {

    private boolean isNewDeckPopUpVisible = false;

    public void setNewDeckPopUpVisible(boolean visible){
        this.isNewDeckPopUpVisible = visible;
    }

    public boolean isNewDeckPopUpVisible(){
        return this.isNewDeckPopUpVisible;
    }



    private Model model;
    private HomeView home;

    public Control(){
        super();
        try {
            this.model = new Model();
        } catch (ParseException e) {
            e.printStackTrace();
            Err.show(Err.PARSE);
        } catch (IOException e) {
            e.printStackTrace();
            Err.show(Err.IO);
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
        this.model.checkDecksDir();
    }


    //CARD SEARCH

    public boolean downloadImage(String card_id){
        Image i = new Image(CardAPI.getCard(card_id).getImageUrl());

        System.out.println(i.getHeight()+" "+i.getWidth()+" "+i.isError()+"\n"+CardAPI.getCard(card_id).getImageUrl());

        if(i.isError()) return false;

        this.home.updateImage(i, Thread.currentThread().getName());

        return true;
    }

    public boolean findCards(String card_name, String type, String text, String colorID, String color, String cmc, String comboBox){

        ArrayList<String[]> l = new ArrayList<>();

        if(card_name.length() > 0) l.add(new String[]{ Model.Key.NAME, card_name });
        if(type.length() > 0) l.add(new String[]{ Model.Key.TYPE, type });
        if(text.length() > 0) l.add(new String[]{ Model.Key.TEXT, text });
        if(colorID.length() > 0) l.add(new String[]{ Model.Key.COLORID, colorID });
        if(color.length() > 0) l.add(new String[]{ Model.Key.COLOR, color });
        if(cmc.length() > 0) l.add(new String[]{ Model.Key.CMC, cmc, comboBox });

        ArrayList<Card> cardL = this.model.findCards(l);

        if(cardL == null) return false;
        if(cardL.size() == 0) return false;

        cardL.sort(Comparator.comparing(Card::getName));

        this.home.updateSearchList(cardL);

        return true;
    }


    //DECK MANAGE

    public String[] getLocalDeckNames(){
        return this.model.getLocalDeckNames();
    }

    public boolean openDeck(String deckName){
        Deck deck = null;
        try {
            deck = this.model.getDeck(deckName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Err.show(Err.FILE_NOT_FOUND);
            return true;
        }

        if( deck == null ) return false;

        this.home.clear();
        this.home.setCurrentDeck(deck);
        this.home.updateDeckList();

        return true;
    }

    public boolean createDeck(String name, String info, NewDeck popUp) {
        switch (this.model.createDeck(name, info)) {
            case 0:
                Err.show(Err.DECK_MUST_HAVE_NAME);
                return true;
            case 1:
                Err.show(Err.DECK_ALREADY_EXIST);
                return true;
            case 2:
                Err.show(Err.IO);
                return true;
            case 3:
                Err.show(Err.IO);
                return true;
            case 4:
                popUp.hide();
                this.home.updateOpenMenu();
                this.openDeck(name);
                return true;
            default:
                return false;
        }
    }

    public boolean saveDeck(Deck deck){
        switch(this.model.saveDeck(deck)){
            case 0:
                Err.show(Err.IO);
                return true;
            case 1:
                return true;
            default:
                return false;
        }
    }

    public void deleteDeck(String name) {

        Optional<ButtonType> r = View.showYESorNO(
                "",
                "Do you wanna really delete " + "\""+name+"\"" + " ?",
                "That can't be undone!");

        if (r.get() == ButtonType.OK)
            if (this.model.deleteDeck(name)) {
                this.home.updateOpenMenu();
                this.home.clear();
            }
            else Err.show(Err.UNKNOWN);
    }

    public void processMassEntry(String mass, Deck deck){
        ArrayList<Object[]> cardL = this.model.processMassEntry(mass);
        for(Object[] o : cardL)
            addCard(o, deck);
    }

    public void removeCard(Object[] card, Deck deck){
        if(deck == null) return;
        deck.removeCard(card);
        this.home.clearCardInfo();
        this.home.updateDeckList();
    }

    public void switchCard(Object[] card, Deck deck){
        deck.switchCard(card);
        this.home.updateDeckList();
    }

    public void addCard(Object[] card, Deck deck){
        if(deck == null) return;
        deck.addCard(card);
        this.home.updateDeckList();
    }

    public void changeNumberOf(Object[] card, int value, Deck deck){
        deck.changeNumberOf(card, value);
        this.home.updateDeckList();
    }
}
