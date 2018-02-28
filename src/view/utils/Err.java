package view.utils;

import javafx.scene.control.Alert;

public class Err {

    public static final int FILE_NOT_FOUND = 0;
    public static final int PARSE = 1;
    public static final int IO = 2;
    public static final int NOTHING_FOUND = 3;
    public static final int MUST_LOAD_DECK_FIRST = 4;
    public static final int MUST_LOAD_CARD_FIRST = 5;
    public static final int INPUT_MUST_BE_INTEGER = 6;
    public static final int DECK_ALREADY_EXIST = 7;
    public static final int UNKNOWN = 99;

    public static void show(int kind){

        String text;

        switch(kind){
            case FILE_NOT_FOUND:
                text = "File non trovato!";
                break;
            case PARSE:
                text = "Il database potrebbe essere danneggiato...";
                break;
            case IO:
                text = "Ecchenneso...!";
                break;
            case NOTHING_FOUND:
                text = "La ricerca non ha prodotto risultati!";
                break;
            case MUST_LOAD_DECK_FIRST:
                text = "Nessun deck a cui fare riferimento!";
                break;
            case MUST_LOAD_CARD_FIRST:
                text = "Nessuna carta a cui fare riferimento";
                break;
            case INPUT_MUST_BE_INTEGER:
                text = "È necessario un numero intero";
                break;
            case DECK_ALREADY_EXIST:
                text = "Mazzo già esistente!";
                break;
            default:
                text = "?!";

        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore!");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();

    }

    public static boolean isNumber(String s){
        if(s.length() == 0) return false;
        for(int i = 0; i < s.length(); i++) if( !Character.isDigit(s.charAt(i)) ) return false;
        return true;
    }


}
