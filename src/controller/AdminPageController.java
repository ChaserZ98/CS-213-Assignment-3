package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Admin;
import model.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
/**
 * Control the AdminPage
 * @author Feiyu Zheng
 * @author Ying Yu
 *
 *
 */

public class AdminPageController {

    @FXML TableView<User> tableView;
    @FXML TableColumn<User, String> usernameColumn;
    @FXML TableColumn<User, String> passwordColumn;
    @FXML TextField usernameTextField;
    @FXML TextField passwordTextField;
    @FXML Button createButton;
    @FXML Button deleteButton;
    @FXML Button logoutButton;

    private Admin admin;

    private ObservableList<User> obsList;

    public void start(Stage mainStage){
        try {
            admin = Admin.readData();
        }
        catch(FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "The admin file cannot be found. A new one will be initialized.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("File not found!");
            alert.initOwner(mainStage);
            alert.showAndWait();
            admin = new Admin();
            Admin.writeData(admin);
        }
        catch(Admin.DeserializationException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "The admin file is corrupted. A new one will be initialized.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Deserialization Error!");
            alert.initOwner(mainStage);
            alert.showAndWait();
            admin = new Admin();
            Admin.writeData(admin);
        }

        obsList = FXCollections.observableArrayList();
        obsList.setAll(admin.getUserList());

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        tableView.setItems(obsList);
        if(obsList.size()!=0){
            tableView.getSelectionModel().select(0);
        }
        mainStage.setOnCloseRequest(this::close);
    }

    public void createUser(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String userDetails = "Username: \t" + username + "\nPassword: \t" + password;

        if(username.length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, userDetails, ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("To add a new user, you should at least enter a non-empty username!");
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, userDetails, ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Create");
            alert.setHeaderText("Are you sure you want to create this user?");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            Optional<ButtonType> option =  alert.showAndWait();
            if(option.get() == ButtonType.YES){
                try{
                    admin.createNewUser(username, password);
                    obsList.setAll(admin.getUserList());
                }
                catch(Admin.RepeatedUserException e){
                    alert = new Alert(Alert.AlertType.ERROR, userDetails, ButtonType.OK);
                    alert.setTitle("Username existed!");
                    alert.setHeaderText(e.getMessage());
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
                catch(Admin.SerializationException e){
                    alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alert.setTitle("Error");
                    alert.setHeaderText(e.getMessage());
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }
        }
    }

    public void logout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        Optional<ButtonType> option =  alert.showAndWait();

        if(option.get() == ButtonType.YES){
            try{
                Admin.writeData(admin);
            }
            catch(Exception e){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error!");
                alert.setContentText(e.getMessage());
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            try{
                Stage primaryStage = (Stage)logoutButton.getScene().getWindow();
                String fxmlPath = "loginPage.fxml";
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/" + fxmlPath));
                AnchorPane loginPage = (AnchorPane) loader.load();
                LoginPageController loginPageController = loader.getController();

                Scene scene = new Scene(loginPage);
                primaryStage.setScene(scene);
                primaryStage.setTitle(fxmlPath);
                loginPageController.start(primaryStage);
            }
            catch(IOException e){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error When Logging out");
                alert.setContentText("Cannot load the login page!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
        }
    }

    public void deleteUser(ActionEvent actionEvent) {
        int index = tableView.getSelectionModel().getSelectedIndex();
        if(index >= 0){
            User user = obsList.get(index);
            String userDetails = "Username: \t" + user.getUsername() + "\nPassword: \t" + user.getPassword();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, userDetails, ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you sure you want to delete this user?");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            Optional<ButtonType> option =  alert.showAndWait();
            if(option.get() == ButtonType.YES){
                obsList.remove(index);
                admin.deleteUser(user.getUsername());
                //Select the latter one
                if(index < obsList.size()){
                    tableView.getSelectionModel().clearSelection(index);
                    tableView.getSelectionModel().select(index);
                }
                //Select the previous one
                else if(index - 1 >=0){
                    tableView.getSelectionModel().select(index - 1);
                }
                else{
                    tableView.getSelectionModel().clearSelection(index);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no user selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    public void close(WindowEvent windowEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit the program?");
        alert.initOwner((Stage)windowEvent.getSource());
        Optional<ButtonType> option =  alert.showAndWait();
        if(option.get() == ButtonType.YES) {
            Admin.writeData(admin);
        }
        else{
            windowEvent.consume();
        }
    }
}
