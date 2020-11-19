package app;

import controller.LoginPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The main class of the photo project
 * @author Feiyu Zheng
 * @author Ying Yu
 */
public class Photos extends Application{

    /**
     * The start method of the whole project that loads the login page and show the primary window
     * @param primaryStage the primary window instance
     */
    @Override
    public void start(Stage primaryStage){
        try {
            String fxmlPath = "loginPage.fxml";

            primaryStage.setTitle("Login Page");
            primaryStage.setResizable(false);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/" + fxmlPath));
            AnchorPane loginPage = (AnchorPane) loader.load();

            LoginPageController loginPageController = loader.getController();
            loginPageController.start(primaryStage);

            Scene scene = new Scene(loginPage);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error When Loading The Login Page!");
            alert.setContentText("Cannot load the Login Page!");
            alert.show();
        }
    }

    /**
     * The main method of the photo project
     * @param args argument parameter
     */
    public static void main(String[] args){
        launch(args);
    }
}
