package view;

import javafx.scene.control.Alert;

public class Err {

    public static final int FILE_NOT_FOUND = 0;
    public static final int PARSE = 1;
    public static final int IO = 2;

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
