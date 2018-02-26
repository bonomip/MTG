package view;

import control.Control;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import model.Card;
import model.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class HomeView extends View{

    @FXML
    private Menu openDeckMenu;

    private Deck _deck;
    private Object[] _card;

    public void setCurrentDeck(Deck d){
        this._deck = d;
    }

    private Control ctrl;

    public HomeView(Control control){
        this.ctrl = control;
        this.ctrl.linkHome(this);
    }

    @Override
    public void show() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Url.home));
        loader.setController(this);

        super.stage = new Stage();
        super.stage.setOnCloseRequest(event -> System.exit(0));
        super.stage.setScene(new Scene(loader.load(), 800, 600 ) );
        super.stage.setResizable(false);
        super.stage.show();

        String[] d = this.ctrl.getLocalDeckNames();

        for( int i = 0; i < d.length; i++ ){
            MenuItem m = new MenuItem(d[i]);
            m.setOnAction(event -> {
                if( !ctrl.openDeck(m.getText()) ) { Err.show(Err.FILE_NOT_FOUND); return; }
                showReviewPane(new ActionEvent());
            });
            openDeckMenu.getItems().add(m);
        }

        this.setUpDeckManagePane();
    }

    @Override
    public void hide() {
        super.stage.hide();
    }

    @Override
    public void start(Stage primaryStage) throws Exception { }


    //deckManagePane

    @FXML
    AnchorPane deckManagePane, searchPane, reviewPane;

    @FXML
    ListView<Object[]> mainDeckLV, sideDeckLV;

    @FXML
    ListView<Card> seachCardLV;

    @FXML
    ComboBox<String> searchCB;

    @FXML
    TextField searchParam, numberOfCards;

    @FXML
    TextArea cardInfo;

    ObservableList<Object[]> mainL, sideL;

    private void setUpDeckManagePane(){
        mainL = FXCollections.observableArrayList();
        mainDeckLV.setItems(mainL);
        mainDeckLV.setCellFactory(param -> new LVCGeneric());

        sideL = FXCollections.observableArrayList();
        sideDeckLV.setItems(sideL);
        sideDeckLV.setCellFactory(param -> new LVCGeneric());

    }

    private class LVCGeneric extends ListCell<Object[]>{

        @FXML
        AnchorPane generic;
        @FXML
        Label name, number;
        @FXML
        Button remove;

        private FXMLLoader loader;

        @Override
        protected void updateItem(Object[] card, boolean empty) {
            super.updateItem(card, empty);
            if ( card != null) {
                if (this.loader == null) {
                    this.loader = new FXMLLoader();
                    this.loader.setLocation(getClass().getResource(Url.Cell.generic));
                    this.loader.setController(this);
                    try { this.loader.load(); } catch (IOException e) { e.printStackTrace(); }
                }
                this.name.setText(((Card)card[0]).get0());
                this.number.setText(card[1].toString());
                this.remove.setOnAction(event -> {
                    _deck.remove(card);
                    updateDeckCardList();
                });
                this.generic.setOnMouseClicked(event -> {
                    updateDeckCardInfo(card);
                });
                setGraphic(this.generic);
            } else setGraphic(null);
        }
    }

    public void updateDeckCardList(){

        this.mainDeckLV.getItems().clear();
        this.sideDeckLV.getItems().clear();

        ArrayList<Object[]> dc = this._deck.getCardList();
        for(int i = 0; i < dc.size(); i++){
            if(dc.get(i)[2].toString().equals("S")) sideL.add(dc.get(i));
            else if(dc.get(i)[2].toString().equals("M")) mainL.add(dc.get(i));
        }
    }

    public void updateDeckCardInfo(Object[] card){
        this._card = card;
        this.cardInfo.setText(card[0].toString());
    }

        //seachPane

    public void findCard(ActionEvent event){

    }

    public void addPlain(ActionEvent event){

    }
    public void addIsland(ActionEvent event){

    }
    public void addSwamp(ActionEvent event){

    }
    public void addMountain(ActionEvent event){

    }
    public void addForest(ActionEvent event){

    }


        //reviewPane

    public void changeNumberOfCards(ActionEvent event){
        if(Err.isNumber(this.numberOfCards.getText())) {
            if(this._card != null && this._deck != null) {
                this._deck.changeNumberOf(this._card, Integer.parseInt(this.numberOfCards.getText()));
                this.updateDeckCardList();
            }
        }
    }



    //statsPane

    @FXML
    AnchorPane statsPane;



    //oddsPane

    @FXML
    AnchorPane oddsPane;

    @FXML
    ListView<Card> cardsOddsLV, desiredCardsLV;

    @FXML
    TextField outCardName, desiredCardName, desiredCardsRange;

    @FXML
    Label rhc1, rhc2, rhc3, rhc4, rhc5, rhc6, rhc7, desiredCardsOdds;

    public void randomHand(ActionEvent event){}

    public void addOutCard(ActionEvent event){}

    public void resetOutCards(ActionEvent event){}

    public void addDesiredCard(ActionEvent event){}



//main pane action listener


    public void newDeck(ActionEvent event){

    }


    public void closeApp(ActionEvent event){
        this.hide();
    }


    public void showSearchPane(ActionEvent event){
        deckManagePane.setVisible(true);
        searchPane.setVisible(true);
        reviewPane.setVisible(false);
        statsPane.setVisible(false);
        oddsPane.setVisible(false);

    }


    public void showReviewPane(ActionEvent event){
        deckManagePane.setVisible(true);
        searchPane.setVisible(false);
        reviewPane.setVisible(true);
        statsPane.setVisible(false);
        oddsPane.setVisible(false);
    }


    public void deleteDeck(ActionEvent event){

    }


    public void showStatsPane(ActionEvent event){
        deckManagePane.setVisible(false);
        searchPane.setVisible(false);
        reviewPane.setVisible(false);
        statsPane.setVisible(true);
        oddsPane.setVisible(false);
    }


    public void showOddsPane(ActionEvent event){
        deckManagePane.setVisible(false);
        searchPane.setVisible(false);
        reviewPane.setVisible(false);
        statsPane.setVisible(false);
        oddsPane.setVisible(true);
    }




}
