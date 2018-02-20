package view;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import control.*;

public class StartView extends View {

    @FXML
    private ProgressBar progressBar;

    private Control ctrl;

    @Override
    public void show() throws IOException {

    }

    @Override
    public void hide() {
        super.stage.hide();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(Url.start));
        loader.setController(this);

        super.stage = primaryStage;
        super.stage.setOnCloseRequest(event -> System.exit(0));
        super.stage.initStyle(StageStyle.UTILITY);
        super.stage.setScene(new Scene(loader.load(), 220, 50 ) );
        super.stage.setResizable(false);
        super.stage.show();

        this.ctrl = new Control();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ctrl.setUp();
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            try{

                this.hide();

                new HomeView(ctrl).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        progressBar.setProgress(-1);
        progressBar.progressProperty().bind(task.progressProperty());

        new Thread(task).start();
    }

    public static void main(String[] args){
        launch(args);
    }
}
