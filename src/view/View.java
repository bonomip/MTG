package view;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public abstract class View extends Application{

    public Stage stage;
    public int width, height;

    public View(){}

    abstract public void show() throws IOException;

    abstract public void hide();

    public class Url
    {
        public final static String start = "fxml/start.fxml";
        public final static String home = "fxml/home.fxml";
        public final static String newDeck = "fxml/new_deck.fxml";

        public class Cell {
            public final static String generic = "fxml/cell/listViewCell-name-nbr-rm.fxml";
            public final static String adding = "fxml/cell/listViewCell-name-nbr-s-m.fxml";
            public final static String odd = "fxml/cell/listViewCell-name-odd.fxml";
        }
    }

    public static Optional<ButtonType> showYESorNO(String title, String header, String text) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);

        return alert.showAndWait();
    }
}
