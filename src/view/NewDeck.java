package view;

import control.Control;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import view.utils.Err;

import java.io.IOException;

public class NewDeck extends View {

    private Control ctrl;

    @FXML
    TextField name;
    @FXML
    TextArea info;

    public NewDeck(Control control){
        this.ctrl = control;
    }

    @Override
    public void show() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Url.newDeck));
        loader.setController(this);

        super.stage = new Stage();
        super.stage.setOnCloseRequest(event -> this.hide());
        super.stage.setScene(new Scene(loader.load(), 240, 360 ) );
        super.stage.setResizable(false);
        super.stage.setTitle("Create new deck");
        super.stage.show();

        this.ctrl.setNewDeckPopUpVisible(true);
    }

    @Override
    public void hide() {
        this.ctrl.setNewDeckPopUpVisible(false);
        super.stage.hide();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    public void create(ActionEvent event){
            if(!this.ctrl.createDeck(this.name.getText(), this.info.getText(), this))
                Err.show(Err.UNKNOWN);
    }
}
