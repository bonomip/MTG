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
import view.utils.Err;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class HomeView extends View{

    @FXML
    private Menu openDeckMenu;

    private static final ObservableList<String> SRC_OPT = FXCollections.observableArrayList( "Name" );

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

        this.setUpMenuItems();
        this.setUpDeckManagePane();
        this.setUpSearchPane();
    }

    @Override
    public void hide() {
        super.stage.hide();
    }

    @Override
    public void start(Stage primaryStage) throws Exception { }



    // DECK MANAGE

    @FXML
    private AnchorPane deckManagePane, searchPane, reviewPane;
    @FXML
    private Label mainDeckLabel, sideDeckLabel;
    @FXML
    private ListView<Object[]> mainDeckLV, sideDeckLV;

    private ObservableList<Object[]> mainL, sideL;

    private Deck _deck;
    private Object[] _card;

    public void setCurrentDeck(Deck d){
        this._deck = d;
        this.deckInfo.setText(this._deck.getInfo());
        super.stage.setTitle(this._deck.getName());
    }

    private void setUpDeckManagePane(){
        this.mainL = FXCollections.observableArrayList();
        this.mainDeckLV.setItems(this.mainL);
        this.mainDeckLV.setCellFactory(param -> new LVCGeneric());

        this.sideL = FXCollections.observableArrayList();
        this.sideDeckLV.setItems(this.sideL);
        this.sideDeckLV.setCellFactory(param -> new LVCGeneric());
    }

    public void updateDeckCardList(){

        this.mainDeckLV.getItems().clear();
        this.sideDeckLV.getItems().clear();

        int s = 0;
        int m = 0;

        ArrayList<Object[]> dc = this._deck.getCardList();
        dc.sort(Comparator.comparing(o -> ((Card) o[0]).get0()));

        for(int i = 0; i < dc.size(); i++){
            if(dc.get(i)[2].toString().equals("S")) { sideL.add(dc.get(i)); s += Integer.parseInt(dc.get(i)[1].toString()); }
            else if(dc.get(i)[2].toString().equals("M")) { mainL.add(dc.get(i)); m += Integer.parseInt(dc.get(i)[1].toString()); }
        }

        this.mainDeckLabel.setText("Main deck ( M ): "+m);
        this.sideDeckLabel.setText("Side deck ( S ): "+s);
    }

    private void updateDeckCardInfo(Object[] card){
        this._card = card;
        this.cardInfo.setText(card[0].toString());
    }

    private void clearDeckPane(){
        this.mainL.clear();
        this.sideL.clear();
        this._deck = null;
        this._card = null;
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
                    _deck.removeCard(card);
                    numberOfCards.setText("");
                    cardInfo.setText("");
                    updateDeckCardList();
                });
                this.generic.setOnMouseClicked(event -> {
                    showReviewPane(new ActionEvent());
                    updateDeckCardInfo(card);
                    numberOfCards.setText(card[1].toString());
                });
                setGraphic(this.generic);
            } else setGraphic(null);
        }
    }



        // INFO

    @FXML
    private TextArea cardInfo, deckInfo;
    @FXML
    TextField numberOfCards;

    public void changeNumberOfCards(ActionEvent event){
        if(Err.isNumber(this.numberOfCards.getText())) {

            if(this._deck == null) { Err.show(Err.MUST_LOAD_DECK_FIRST); return; }
            if(this._card == null) { Err.show(Err.MUST_LOAD_CARD_FIRST); return; }

            this._deck.changeNumberOf(this._card, Integer.parseInt(this.numberOfCards.getText()));
            this.updateDeckCardList();

        } else { Err.show(Err.INPUT_MUST_BE_INTEGER); }
    }

    private void clearInfoPane(){
        this.cardInfo.setText("");
        this.deckInfo.setText("");
        this.numberOfCards.setText("");
    }

        // SEARCH

    @FXML
    ListView<Card> searchCardLV;
    @FXML
    ComboBox<String> searchCB;
    @FXML
    TextField searchParam;
    @FXML
    TextArea searchCardInfo;

    ObservableList<Card> searchL;

    private void setUpSearchPane(){
        this.searchCB.setItems(SRC_OPT);
        this.searchCB.setValue(SRC_OPT.get(0));

        this.searchL = FXCollections.observableArrayList();
        this.searchCardLV.setItems(this.searchL);
        this.searchCardLV.setCellFactory(param -> new LVCAdding());
    }

    public void findCard(ActionEvent event){

        String s = this.searchParam.getText();

        switch (this.searchCB.getValue()){
            case "Name":
                if( !this.ctrl.findCardsByName(s) ) Err.show(Err.NOTHING_FOUND);
                break;
                //todo altri tipi di ricerca
        }

    }

    public void updateSearchLV(ArrayList<Card> card_list){
        this.searchCardLV.getItems().clear();

        this.searchL.addAll(card_list);
    }

    private void updateSearchCardInfo(Card card){
        this.searchCardInfo.setText(card.toString());
    }

    /*
    public void addLand(ActionEvent event){
        if(this._deck == null) return;

        switch (((Button)event.getSource()).getId()){
            case Card.PLAINS:
                break;
            case Card.ISLAND:
                break;
            case Card.SWAMP:
                break;
            case Card.MOUNTAIN:
                break;
            case Card.FOREST:
                break;

        }
    }*/

    private class LVCAdding extends ListCell<Card> {

        @FXML
        private AnchorPane adding;

        @FXML
        private Label name;

        @FXML
        private Button toMain, toSide;

        @FXML
        private Spinner<Integer> spin;

        private FXMLLoader loader;

        @Override
        protected void updateItem(Card card, boolean empty){
            super.updateItem(card, empty);
            if ( card != null) {

                if (this.loader == null) {
                    this.loader = new FXMLLoader();
                    this.loader.setLocation(getClass().getResource(Url.Cell.adding));
                    this.loader.setController(this);
                    try { this.loader.load(); } catch (IOException e) { e.printStackTrace(); }
                }

                this.spin.getValueFactory().setValue(0);
                this.name.setText(card.get0());
                this.toMain.setOnAction(event -> {
                    if(_deck == null) { Err.show(Err.MUST_LOAD_DECK_FIRST); return; }
                    if(spin.getValue() != 0) {
                        _deck.addCard(new Object[] { card, spin.getValue(), "M" });
                        updateDeckCardList();
                    }
                });
                this.toSide.setOnAction(event -> {
                    if(_deck == null) { Err.show(Err.MUST_LOAD_DECK_FIRST); return; }
                    if(spin.getValue() != 0) {
                        _deck.addCard(new Object[] { card, spin.getValue(), "S" });
                        updateDeckCardList();
                    }
                });
                this.adding.setOnMouseClicked(event -> {
                    updateSearchCardInfo(card);
                });

                setGraphic(this.adding);

            } else setGraphic(null);
        }

    }



    // STATISTIC

    @FXML
    AnchorPane statsPane;



    // ODDS

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



    // MENU

    private void setUpMenuItems(){
        this.updateOpenMenu();
    }

    public void updateOpenMenu(){
        openDeckMenu.getItems().clear();

        String[] d = this.ctrl.getLocalDeckNames();

        if(d == null) return;

        for( int i = 0; i < d.length; i++ ){
            MenuItem m = new MenuItem(d[i]);
            m.setOnAction(event -> {
                if( !ctrl.openDeck(m.getText()) ) { Err.show(Err.FILE_NOT_FOUND); return; }
                showReviewPane(new ActionEvent());
            });
            openDeckMenu.getItems().add(m);
        }
    }

    private NewDeck ndPop;

    public void newDeck(ActionEvent event){
        if(!this.ctrl.isNewDeckPopUpVisible()) {
            this.ndPop = new NewDeck(this.ctrl);
            try { this.ndPop.show(); } catch (IOException e) { e.printStackTrace(); }

        } else { this.ndPop.stage.requestFocus(); }
    }

    public void saveDeck(ActionEvent event){
        if(this._deck == null) return;

        if(!this.ctrl.saveDeck(this._deck)) Err.show(Err.UNKNOWN);
    }

    public void deleteDeck(ActionEvent event){
        this.ctrl.deleteDeck(this._deck.getName());
    }

    public void closeApp(ActionEvent event){
        this.hide();
    }



    // HEADER BUTTONS

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



    // GLOBAL

    public void clear(){
        this.stage.setTitle("");
        this.clearDeckPane();
        this.clearInfoPane();
    }

}
