package view;

import control.Control;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import model.Card;
import model.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.utils.Err;
import view.utils.Info;

import java.io.*;
import java.util.*;

public class HomeView extends View{

    Control ctrl;

    private Deck _deck;
    private Card _card;

    @FXML
    private AnchorPane mgmt, stats, odds,
            cardPane;

    //MGMT
    @FXML
    Label   mdLabel, sdLabel,
            ic1, ic2, ic3, ic4, ic5;
    @FXML
    ListView<Object[]> mdLV, sdLV;
    @FXML
    ListView<Card> scLV;
    @FXML
    ComboBox<String> cmcBox;
    @FXML
    TextField cname, ctype, ctext, ccolorID, ccolor, ccmc;
    @FXML
    ImageView cardImage;

    //STATISTIC
    @FXML
    StackedBarChart<?,?> manaSBC;
    @FXML
    CategoryAxis mclcX;
    @FXML
    NumberAxis mclcY;

    @FXML
    PieChart landsPC;

    HomeView(Control control){
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
        super.stage.setScene(new Scene(loader.load(), 800, 752 ) );
        super.stage.setResizable(true);
        super.stage.show();

        this.MGMTinit();
        this.setUpPreferences();
        this.setUpMenuItems();
        this.setUpStatsPane();
    }

    @Override
    public void hide() {
        super.stage.hide();
    }

    @Override
    public void start(Stage primaryStage) throws Exception { }



    public synchronized void setCurrentDeck(Deck d){

        if(d == null) this.clear();

        this._deck = d;
        super.stage.setTitle(this._deck.getName());

        this.deckFunction(true);
    }

    private synchronized void setCurrentCard(Card c){
        if(this._card != null)
            if(this._card.getId().equals(c.getId())) return;
        this._card = c;
        this.updateCardInfo();
    }

    int sortby = 0; /*
                             * 0 : name
                             * 1 : cmc
                             * 2 : type
                             */

    boolean asc;

    private void setUpPreferences(){
        this.sortby = 0;
        this.SBCSplitTypes = false;
        this.asc = true;
    }


    //MTGM

    private ObservableList<Object[]> mainL, sideL;
    private ObservableList<Card> searchL;
    private Thread loadImage;
    @FXML
    private ProgressIndicator piLoad;

    private void MGMTinit(){
            this.mainL = FXCollections.observableArrayList();
            mdLV.setItems(this.mainL);
            mdLV.setCellFactory(param -> new LVCGeneric());
            this.mdLV.set

            this.sideL = FXCollections.observableArrayList();
            sdLV.setItems(this.sideL);
            sdLV.setCellFactory(param -> new LVCGeneric());

            this.searchL = FXCollections.observableArrayList();
            scLV.setItems(this.searchL);
            scLV.setCellFactory(param -> new LVCAdding());

            this.cardImage.setPreserveRatio(true);

            this.cmcBox.setItems(cmcParams);
            this.cmcBox.setValue(cmcParams.get(0));

            this.loadImage = new Thread();
        }

    private void MGMTclear(){
        this.mainL.clear();
        this.sideL.clear();
        mdLabel.setText("Main deck ( M )");
        sdLabel.setText("Side deck ( S )");
        this.cardImage.setVisible(false);
        this.clearCardInfo();
    }

    public void updateDeckList(){

            mdLV.getItems().clear();
            sdLV.getItems().clear();

            int s = 0;
            int m = 0;

            ArrayList<Object[]> dc = _deck.getCardList();

            Comparator<Object[]> c;

            switch (sortby){
                case 0:
                    c = Comparator.comparing(o -> ((Card) o[0]).getName());
                    if(asc)
                        dc.sort(c);
                    else
                        dc.sort(c.reversed());
                    break;
                case 1:
                    c = Comparator.comparing(o -> ( (Card) o[0] ).getConvertedManaCost());
                    if(asc)
                        dc.sort(c);
                    else
                        dc.sort(c.reversed() );
                    break;
                case 2:
                    c = Comparator.comparing(o -> ((Card) o[0]).getTypes()[ ((Card) o[0]).getTypes().length-1 ]);
                    c.thenComparing(o -> ( (Card) o[0] ).getConvertedManaCost());
                    if(asc)
                        dc.sort(c);
                    else
                        dc.sort(c.reversed());
            }

            for(int i = 0; i < dc.size(); i++){
                if(dc.get(i)[2].toString().equals("S")) { sideL.add(dc.get(i)); s += Integer.parseInt(dc.get(i)[1].toString()); }
                else if(dc.get(i)[2].toString().equals("M")) { mainL.add(dc.get(i)); m += Integer.parseInt(dc.get(i)[1].toString()); }
            }

            mdLabel.setText("Main deck ( M ): "+m);
            sdLabel.setText("Side deck ( S ): "+s);
        }

    private void updateCardInfo(){
        this.cardPane.setVisible(true);
        this.cardImage.setVisible(false);

        ic1.setText(_card.getName());
        ic2.setText(_card.getManaCost());
        ic3.setText(_card.getFullType());
        ic4.setText(_card.getText());
        ic5.setText(_card.getPowerOrLoyalty());

        if(loadImage != null)
            if(loadImage.isAlive())
                if(loadImage.getName().equals(this._card.getId()))
                    return;
                else
                    loadImage.interrupt();

        Task<Boolean>  task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return ctrl.downloadImage(_card.getId()); }
        };
        task.setOnSucceeded(event -> piLoad.setVisible(false));
        task.setOnCancelled(event -> piLoad.setVisible(false));
        task.setOnFailed(event -> piLoad.setVisible(false));
        task.setOnRunning(event -> piLoad.setVisible(true));

        this.piLoad.progressProperty().bind(task.progressProperty());

        this.loadImage = new Thread(task);
        this.loadImage.setName(_card.getId());
        this.loadImage.start();
    }

    public synchronized void updateImage(Image i, String card_id){
        if(!this._card.getId().equals(card_id)) return;

        this.cardPane.setVisible(false);
        this.cardImage.setVisible(true);

        this.cardImage.setId(card_id);
        this.cardImage.setImage(i);
    }

    private void clearDeckPane(){
            _deck = null;
            _card = null;
        }

    private class LVCGeneric extends ListCell<Object[]> {

            @FXML
            AnchorPane generic;
            @FXML
            Label name;
            @FXML
            TextField number;
            @FXML
            Button remove, sswitch;

            private FXMLLoader loader;

            Object[] cCard;

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
                    this.cCard = card;
                    this.name.setText(((Card)card[0]).getName());

                    this.number.setPromptText(card[1].toString());
                    this.number.setOnAction(event -> this.changeNumber(event));

                    this.remove.setOnAction(event -> {
                        ctrl.removeCard(card, _deck);
                    });

                    this.sswitch.setOnAction(event -> {
                        ctrl.switchCard(card, _deck);
                    });

                    this.generic.setOnMouseClicked(event -> {
                        setCurrentCard((Card)card[0]);
                    });

                    setGraphic(this.generic);
                } else setGraphic(null);
            }

            public void changeNumber(ActionEvent event){
                if(Err.isNumber(this.number.getText())) {

                    ctrl.changeNumberOf(this.cCard, Integer.parseInt(this.number.getText()), _deck);
                    generic.requestFocus();
                    this.number.clear();
                    this.number.setPromptText(this.cCard[1].toString());


                } else { Err.show(Err.INPUT_MUST_BE_INTEGER); }
            }
        }

    public void find(ActionEvent event){

            if(this.ccmc.getText().length() > 0 && !Err.isNumber(this.ccmc.getText())) return;

            if (!this.ctrl.findCards(this.cname.getText(), this.ctype.getText(), this.ctext.getText(), this.ccolorID.getText(), this.ccolor.getText(), this.ccmc.getText(), this.cmcBox.getValue()))
                Err.show(Err.NOTHING_FOUND);
    }

    public void updateSearchList(ArrayList<Card> card_list){
            this.scLV.getItems().clear();
            //todo sort preferences
            this.searchL.addAll(card_list);
        }

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
                    this.name.setText(card.getName());
                    this.toMain.setOnAction(event -> {
                        if(_deck == null) { Err.show(Err.MUST_LOAD_DECK_FIRST); return; }
                        if(spin.getValue() != 0) {
                            ctrl.addCard(new Object[] { card, spin.getValue(), "M" }, _deck);
                        }
                    });
                    this.toSide.setOnAction(event -> {
                        if(_deck == null) { Err.show(Err.MUST_LOAD_DECK_FIRST); return; }
                        if(spin.getValue() != 0) {
                            ctrl.addCard(new Object[] { card, spin.getValue(), "S" }, _deck);
                        }
                    });
                    this.adding.setOnMouseClicked(event -> {
                        setCurrentCard(card);
                    });

                    setGraphic(this.adding);

                } else setGraphic(null);
            }
        }

    public void clearCardInfo(){
            ic1.setText("");
            ic2.setText("");
            ic3.setText("");
            ic4.setText("");
            ic5.setText("");
        }

    // STATISTIC

    private boolean SBCSplitTypes;

    private void setUpStatsPane(){
        this.setUpManaBarChart();
        this.setUpPieChart();
    }

    private void setUpManaBarChart(){
        this.mclcY.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                if(object.intValue()!=object.doubleValue())
                    return "";
                return ""+(object.intValue());
            }

            @Override
            public Number fromString(String string) {
                Number val = Double.parseDouble(string);
                return val.intValue();
            }
        });

        this.mclcY.setTickUnit(1);
        this.mclcY.setMinorTickVisible(false);
        this.mclcY.setAutoRanging(false);
    }

    private void updateManaBarChart(){
        this.manaSBC.getData().clear();

        ArrayList<Object[]> list = this._deck.getMainCardList();

        list.sort(Comparator.comparing(o -> ((Card)o[0]).getConvertedManaCost() ));

        this.mclcX.setCategories(getXRangedValues(list));
        this.mclcX.invalidateRange(this.mclcX.getCategories());
        this.mclcY.setUpperBound(getYRange(this._deck.getMainCardList())+1);

        if(!SBCSplitTypes) { //merge types
            setSpecificSeries(Card.Type.CREATURE,"Creatures", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.INSTANT,"Instants", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.SORCERY,"Sorceries", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.ENCHANTMENT,"Enchantments", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.ARTIFACT,"Artifacts", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.PLANESWALKER, "PlanesWalkers", this._deck, this.manaSBC);
        } else { //split types
            setSpecificSeries(Card.Type.ARTIFACT_CREATURE,"Arctifact Creatures", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.ARTIFACT_LAND,"Arctifact Lands", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.ENCHANTMENT_ARTIFACT,"Enchantment Arctifacts", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.ENCHANTMENT_CREATURE,"Enchantment Creatures", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.LAND_CREATURE,"Land Creatures", this._deck, this.manaSBC);
            setOnlyGenericSeries(Card.Type.CREATURE,"Creatures", this._deck, this.manaSBC);
            setOnlyGenericSeries(Card.Type.INSTANT,"Instants", this._deck, this.manaSBC);
            setOnlyGenericSeries(Card.Type.SORCERY,"Sorceries", this._deck, this.manaSBC);
            setOnlyGenericSeries(Card.Type.ENCHANTMENT,"Enchantments", this._deck, this.manaSBC);
            setOnlyGenericSeries(Card.Type.ARTIFACT,"Artifacts", this._deck, this.manaSBC);
            setSpecificSeries(Card.Type.PLANESWALKER, "PlanesWalkers", this._deck, this.manaSBC);
        }
    }

    private static void setSpecificSeries(String type, String name, Deck deck, StackedBarChart<?,?> SBC){
        ArrayList<Double> cmc = new ArrayList<>();
        ArrayList<Integer> n = new ArrayList<>();
        ArrayList<Object[]> list = deck.getCardListOfSpecifiedType(deck.getMainCardList(), type);

        if(list.size() == 0) return;

        list.sort(Comparator.comparing(o -> ((Card)o[0]).getConvertedManaCost() ));

        foo(list, cmc, n);

        SBC.getData().addAll(getSeries(cmc, n, name));

    }

    private static void setOnlyGenericSeries(String type, String name, Deck deck, StackedBarChart<?,?> SBC){
        ArrayList<Double> cmc = new ArrayList<>();
        ArrayList<Integer> n = new ArrayList<>();

        ArrayList<Object[]> list = deck.getCardListOfGenericType(deck.getMainCardList(), type);

        if(list.size() == 0) return;

        list.sort(Comparator.comparing(o -> ((Card)o[0]).getConvertedManaCost() ));

        foo(list, cmc, n);

        SBC.getData().addAll(getSeries(cmc, n, name));

    }

    private static void foo(ArrayList<Object[]> card_list, ArrayList<Double> cost, ArrayList<Integer> number ){
        for ( Object[] o : card_list){
            Card card = (Card) o[0];
            int n = Integer.parseInt(o[1].toString());

            if(card.getConvertedManaCost() == 0.0) continue;

            if( cost.contains(card.getConvertedManaCost()) ){
                number.set( cost.indexOf( card.getConvertedManaCost() ), number.get(cost.indexOf(card.getConvertedManaCost()))+ n);
            } else {
                cost.add(card.getConvertedManaCost());
                number.add(cost.indexOf(card.getConvertedManaCost()), n);
            }
        }
    }

    private static ObservableList<String> getXRangedValues(ArrayList<Object[]> card_list){

        ArrayList<Double> manaCost = new ArrayList<>();

        for ( Object[] o : card_list){
            Card card = (Card) o[0];
            if(card.getConvertedManaCost() == 0.0) continue;
            if( !manaCost.contains(card.getConvertedManaCost()) ) manaCost.add(card.getConvertedManaCost());
        }

        ObservableList<String> result = FXCollections.observableArrayList();
        double maxMana = 1;
        for( Double i : manaCost ) if( i > maxMana ) maxMana = i;
        for(double i = 1; i <= maxMana; i++) result.add(Double.toString(i));
        return result;
    }

    private static double getYRange(ArrayList<Object[]> list){
        ArrayList<Double> m = new ArrayList<>();
        ArrayList<Double> n = new ArrayList<>();

        for ( Object[] o : list){
            Card card = (Card) o[0];
            if(card.getConvertedManaCost() == 0.0) continue;

            if( !m.contains(card.getConvertedManaCost()) ) { m.add(card.getConvertedManaCost()); n.add( m.indexOf(card.getConvertedManaCost()), Double.parseDouble(o[1].toString()) ); }
            else n.set( m.indexOf(card.getConvertedManaCost()), n.get(m.indexOf(card.getConvertedManaCost()))+Double.parseDouble(o[1].toString()) );
        }

        double max = 0;

        for( double d : n ) if( d > max ) max = d;

        return max;
    }

    private static XYChart.Series getSeries(ArrayList<Double> manaCost, ArrayList<Integer> numberOf, String name){
        XYChart.Series series = new XYChart.Series();
        for( Double d : manaCost )
            series.getData().add(new Data(d.toString(), numberOf.get(manaCost.indexOf(d))));
        series.setName(name);
        return series;
    }

    private void setUpPieChart(){}

    private void updatePieChart(){

        this.landsPC.getData().clear();

        int[] land = {0, 0, 0, 0, 0, 0, 0};
        String[] name = {"White", "Blue", "Black", "Red", "Green", "Colorless", "Other"};

        int count = 0;

        for ( Object[] o : this._deck.getCardList()) {
            Card c = (Card)o[0];
            int n = Integer.parseInt(o[1].toString());

            if(!c.is(Card.Type.LAND)) continue;

            count += n;

            System.out.println(c.getName()+" "+n);

            for( String col : c.getColorID() )
                switch (col){
                    case "W": land[0] += n; break;
                    case "U": land[1] += n; break;
                    case "B": land[2] += n; break;
                    case "R": land[3] += n; break;
                    case "G": land[4] += n; break;
                    case "C": land[5] += n; break;
                    default: land[5] += n; break;
                }
        }

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        System.out.println("");
        for(int i = 0; i < land.length; i++)
            if(land[i] > 0){
                data.add(new PieChart.Data(name[i], (double)land[i]/(double)count*100));
                System.out.println(name[i]+" "+((double)land[i]/(double)count)*100);
        }

        this.landsPC.setData(data);
    }


    // ODDS

    @FXML
    ListView<Card> cardsOddsLV, desiredCardsLV;
    @FXML
    TextField outCardName, desiredCardName, desiredCardsRange;
    @FXML
    Label rhc1, rhc2, rhc3, rhc4, rhc5, rhc6, rhc7, rhc8, rhc9, rhc10, rhc11, rhc12, desiredCardsOdds;

    public void randomHand(ActionEvent event){
        Label[] rh = { rhc1, rhc2, rhc3, rhc4, rhc5, rhc6, rhc7, rhc8, rhc9, rhc10, rhc11, rhc12 };

        ArrayList<Card> list = new ArrayList<>();

        for ( Object[] o : this._deck.getMainCardList() )
            for(int i = 0; i < Integer.parseInt(o[1].toString()); i++)
                list.add((Card)o[0]);

        double x;
        for( int i = 0; i < rh.length && list.size() != 0; i++) {
            x = Math.random();
            x *= list.size();
            Card card = list.get((int) x);
            rh[i].setText(card.getName()+"\t"+card.getManaCost());
            list.remove(card);
        }

    }

    public void addOutCard(ActionEvent event){}

    public void resetOutCards(ActionEvent event){}

    public void addDesiredCard(ActionEvent event){}

    public void oddsClear(){
        Label[] rh = { rhc1, rhc2, rhc3, rhc4, rhc5, rhc6, rhc7, rhc8, rhc9, rhc10, rhc11, rhc12 };
        for ( Label l: rh) l.setText("");
    }



    // MENU

    @FXML
    private Menu openDeckMenu, editMenu, SBCPrefMenu, deckPrefMenu;
    @FXML
    private MenuItem infoSearch, aboutApp;

    private void setUpMenuItems(){
        this.updateOpenMenu();
        this.setUpSBCPrefMenu();
        this.setUpDeckPref();
        this.setUpHelpMenu();
    }

    private void setUpDeckPref(){
        Menu deckSort = new Menu("Sort by...");

        ToggleGroup t = new ToggleGroup();
        RadioMenuItem name = new RadioMenuItem("Card name");
        name.setId("0sb");
        name.setToggleGroup(t);
        RadioMenuItem cmc = new RadioMenuItem("Converted mana cost");
        cmc.setToggleGroup(t);
        cmc.setId("1sb");
        RadioMenuItem type = new RadioMenuItem("Type");
        type.setToggleGroup(t);
        type.setId("2sb");

        switch (this.sortby){
            case 0:
                name.setSelected(true);
                break;
            case 1:
                cmc.setSelected(true);
                break;
            case 2:
                type.setSelected(true);
                break;
        }
        EventHandler<ActionEvent> e = event -> {
            switch ( (((RadioMenuItem) event.getSource()).getId()) ){
                case "0sb":
                    this.sortby = 0;
                    break;
                case "1sb":
                    this.sortby = 1;
                    break;
                case "2sb":
                    this.sortby = 2;
                    break;
            }

            this.updateDeckList();
        };
        name.setOnAction(e);
        cmc.setOnAction(e);
        type.setOnAction(e);

        ToggleGroup r = new ToggleGroup();
        RadioMenuItem desc = new RadioMenuItem("Descending");
        desc.setToggleGroup(r);
        RadioMenuItem asc = new RadioMenuItem("Ascending");
        asc.setToggleGroup(r);
        desc.setSelected(!this.asc);
        asc.setSelected(this.asc);
        asc.setOnAction(event -> {
            this.asc = true;
            this.updateDeckList();
        });
        desc.setOnAction(event -> {
            this.asc = false;
            this.updateDeckList();
        });

        deckSort.getItems().add(name);
        deckSort.getItems().add(cmc);
        deckSort.getItems().add(type);
        deckSort.getItems().add(new SeparatorMenuItem());
        deckSort.getItems().add(asc);
        deckSort.getItems().add(desc);

        this.deckPrefMenu.getItems().add(deckSort);
    }

    private void setUpSBCPrefMenu(){
        ToggleGroup t = new ToggleGroup();
        RadioMenuItem split = new RadioMenuItem("Split types");
        RadioMenuItem nosplit = new RadioMenuItem("Merge types");
        split.setToggleGroup(t);
        split.setSelected(this.SBCSplitTypes);
        split.setOnAction(event -> {
            RadioMenuItem r = (RadioMenuItem) event.getSource();
            if(r.isSelected()) SBCSplitTypes = true;
            if( stats.isVisible() ) updateManaBarChart();
        });
        nosplit.setToggleGroup(t);
        nosplit.setSelected(!this.SBCSplitTypes);
        nosplit.setOnAction(event -> {
            RadioMenuItem r = (RadioMenuItem) event.getSource();
            if(r.isSelected()) SBCSplitTypes = false;
            if( stats.isVisible() ) updateManaBarChart();
        });


        this.SBCPrefMenu.getItems().addAll(split);
        this.SBCPrefMenu.getItems().addAll(nosplit);
    }

    private void setUpHelpMenu(){
        infoSearch.setOnAction(event -> Info.showSearchFunctionInfo());
        aboutApp.setOnAction(event -> Info.showAboutApp());
    }

    public void updateOpenMenu(){
        openDeckMenu.getItems().clear();

        String[] d = this.ctrl.getLocalDeckNames();

        if(d == null) return;

        for( int i = 0; i < d.length; i++ ){
            MenuItem m = new MenuItem(d[i]);
            m.setOnAction(event -> {
                if( !ctrl.openDeck(m.getText()) ) { Err.show(Err.FILE_NOT_FOUND); return; }

                showMgmtPane(new ActionEvent());
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

    public void massEntry(){
            Info.showMassEntry(this.ctrl, this._deck);
    }

    public void saveDeck(ActionEvent event){
        if(this._deck == null) return;

        if(!this.ctrl.saveDeck(this._deck)) Err.show(Err.UNKNOWN);
    }

    public void deleteDeck(ActionEvent event){
        if(this._deck == null) return;
        this.ctrl.deleteDeck(this._deck.getName());
    }

    public void closeApp(ActionEvent event){
        this.hide();
    }



    // HEADER BUTTONS

    @FXML
    Button statsButton, oddsButton;

    public void showMgmtPane(ActionEvent event){
        mgmt.setVisible(true);
        stats.setVisible(false);
        odds.setVisible(false);
    }


    public void showStatsPane(ActionEvent event){
        mgmt.setVisible(false);
        stats.setVisible(true);
        odds.setVisible(false);

        this.updateManaBarChart();
        this.updatePieChart();
    }


    public void showOddsPane(ActionEvent event){
        mgmt.setVisible(false);
        stats.setVisible(false);
        odds.setVisible(true);
    }



    // GLOBAL

    public void clear(){
        this.stage.setTitle("");
        this._deck = null;
        this._card = null;

        this.MGMTclear();
        this.oddsClear();

        this.deckFunction(false);
    }

    public void deckFunction(boolean enable){
        this.editMenu.setDisable(!enable);
        this.SBCPrefMenu.setDisable(!enable);
        this.deckPrefMenu.setDisable(!enable);

        this.statsButton.setDisable(!enable);
        this.oddsButton.setDisable(!enable);
    }

}
