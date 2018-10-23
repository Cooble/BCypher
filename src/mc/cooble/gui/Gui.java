package mc.cooble.gui;/**
 * Created by Matej on 20.7.2018.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Gui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Gui.class.getClassLoader().getResource("bcypher.fxml"));

        Scene scene = new Scene(root, 1300, 800);


        stage.setTitle("BCypher");
        stage.setScene(scene);
        stage.show();

    }
}
