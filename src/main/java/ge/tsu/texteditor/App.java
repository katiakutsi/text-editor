package ge.tsu.texteditor.texteditor;

import ge.tsu.texteditor.texteditor.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent mainRoot;

        try(InputStream inputStream = App.class.getResourceAsStream("/views/main-view.fxml")) {
            mainRoot = fxmlLoader.load(inputStream);
        }

        MainController controller = fxmlLoader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(mainRoot, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Text Editor");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}