package view.utils;

import control.Control;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import model.Deck;

import java.util.Optional;

public class Info {

    public static void showSearchFunctionInfo(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to...");
        alert.setHeaderText("Search function");
        alert.setContentText("name: of the card\n\n" +
                "type: of the card included subtypes and supertypes\n\n" +
                "text: where card's abilities appear\n\n" +
                "colorID: specified by all mana symbols that appear in the card's casting cost, color indicator, and rules text\n" +
                "\t\"!B\" = only black cards\n"+
                "\t\"B\" = cards that are at least black\n" +
                "\t\"!BU\" = only black and blue cards\n" +
                "\t\"C\" = only colorless cards\n"+
                "\t\"WUB\" = card that are at least white, blue and black\n\n" +
                "color: of the card ( see colorID for usage )\n\n"+
                "cmc: converted mana cost");
        alert.showAndWait();
    }

    public static void showAboutApp(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About DBMTG...");
        alert.setHeaderText("THIS IS NOT A TRAP!");
        alert.setContentText("Programma in versione beta\n\nPer chiarimenti, segnalazioni o nudes:\n\t\tpaolo.bonomi@outlook.com");
        alert.showAndWait();
    }

    public static void showMassEntry(Control ctrl, Deck deck){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mass entry");
        alert.setHeaderText("");
        alert.setContentText("Usage:\n\n4 vault skirge\n18 Forest\n10 pulfpluf");

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        Optional<ButtonType> r = alert.showAndWait();

        if(r.get() == ButtonType.OK) ctrl.processMassEntry(textArea.getText(), deck);
    }

}
