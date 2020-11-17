package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.User;

import java.io.FileNotFoundException;
import java.io.IOException;

public class LoginPageController{

    @FXML TextField usernameTextField;
    @FXML TextField passwordTextField;
    @FXML Button loginButton;

    public void start(Stage mainStage){

    }

    public void login(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if(username.length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Empty Username!");
            alert.setContentText("Username cannot be empty!");
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
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
//                    primaryStage.show();
                }
                catch(IOException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error When Loading The Admin Page");
                    alert.setContentText("Cannot load the admin page!");
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Password Incorrect!");

                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
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
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
                return;
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Deserialization Error!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
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
                    albumListPageController.start(primaryStage);

                    Scene scene = new Scene(albumListPage);
                    primaryStage.setScene(scene);
                    primaryStage.setTitle(fxmlPath);
                }
                catch(IOException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error When Loading The Album List Page");
                    alert.setContentText("Cannot load the album list page!");
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Password Incorrect!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
        }
    }
}
