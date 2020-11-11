package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class photoAlbum extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/LoginPage.fxml"));
        AnchorPane secondLayout = (AnchorPane) loader.load();
        Scene scene = new Scene(secondLayout);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();

    }




    public static void main(String[] args) {
        launch(args);
    }
}



