package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class LoginPageController{

    @FXML TextField usernameTextField;
    @FXML TextField passwordTextField;
    @FXML Button loginButton;

    public void start(Stage mainStage){
        mainStage.setOnCloseRequest(this::close);
    }

    public void login(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if(username.length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Empty Username!");
            alert.setContentText("Username cannot be empty!");
            alert.showAndWait();
            return;
        }

        if(username.equals("admin")) {
            if(password.equals("admin")){
                try{
                    Stage primaryStage = (Stage)loginButton.getScene().getWindow();

                    String fxmlPath = "adminPage.fxml";
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/" + fxmlPath));
                    AnchorPane adminPage = (AnchorPane) loader.load();
                    AdminPageController adminPageController = loader.getController();
                    adminPageController.start(primaryStage);

                    Scene scene = new Scene(adminPage);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle(fxmlPath);
                }
                catch(IOException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error When Loading The Admin Page");
                    alert.setContentText("Cannot load the admin page!");
                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Password Incorrect!");
                alert.showAndWait();
            }
        }
        else{
            User user;
            try{
                user = User.readData(username);
            }
            catch(FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Username Does Not Exist!");
                alert.showAndWait();
                return;
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Deserialization Error!");
                alert.showAndWait();
                return;
            }
            if(password.equals(user.getPassword())){
                try{
                    Stage primaryStage = (Stage)loginButton.getScene().getWindow();
                    String fxmlPath = "albumListPage.fxml";
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/" + fxmlPath));
                    AnchorPane albumListPage = (AnchorPane) loader.load();
                    AlbumListPageController albumListPageController = loader.getController();
                    albumListPageController.start(primaryStage,user);

                    Scene scene = new Scene(albumListPage);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle(fxmlPath);
                }
                catch(Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error When Loading The Album List Page");
                    alert.setContentText("Cannot load the album list page!");
                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Password Incorrect!");
                alert.showAndWait();
            }
        }
    }
    public void close(WindowEvent windowEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit the program?");
        alert.initOwner((Stage)windowEvent.getSource());
        Optional<ButtonType> option = alert.showAndWait();
        if(! (option.get() == ButtonType.YES)) {
            windowEvent.consume();
        }
    }
}
