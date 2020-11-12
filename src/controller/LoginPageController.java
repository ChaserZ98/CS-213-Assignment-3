package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginPageController {

    public TextField username;
    public TextField password;
    public Button Login;

    public void LoginClicked(ActionEvent actionEvent) {


        String user = username.getText();
        String psw = password.getText();

        if(user.equals("") || user == null || psw.equals("") || psw == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No username and password");
            alert.setContentText("Must input user name and password");
            alert.show();
            return;
        }

        if(user.equalsIgnoreCase("admin") && psw.equalsIgnoreCase("admin")) {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/adminPage.fxml"));
                AnchorPane rootLayout = (AnchorPane) loader.load();
                adminPageController AdminController = loader.getController();

                Scene scene = new Scene(rootLayout);
                stage.setScene(scene);
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                // stage.setTitle("admin page");
                stage.show();
            } catch (IOException m) {
                m.printStackTrace();
            }
        }
    }
}
