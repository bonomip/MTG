package view;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

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

        public class Cell {
            public final static String generic = "fxml/cell/listViewCell-name-nbr-rm.fxml";
            public final static String adding = "fxml/cell/listViewCell-name-nbr-s-m.fxml";
            public final static String odd = "fxml/cell/listViewCell-name-odd.fxml";
        }
    }
}
