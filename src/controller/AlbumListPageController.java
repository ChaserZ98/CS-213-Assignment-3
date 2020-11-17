package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.User;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class AlbumListPageController {

    @FXML ListView<Album> listView;
    @FXML Button addAlbumButton;
    @FXML Button renameAlbumButton;
    @FXML Button deleteAlbumButton;
    @FXML Button openAlbumButton;
    @FXML Button logoutButton;
    @FXML Button goToSearchPageButton;
    @FXML Text albumName;
    @FXML Text numberOfPhotos;
    @FXML Text rangeOfDates;
    @FXML TextField newName;
    @FXML TextField newAlbumName;

    private ObservableList<Album> obsList;
    private User user;

    public void start(Stage mainStage, User loginUser){
        user = loginUser;
        obsList = FXCollections.observableArrayList();
        obsList.setAll(user.getAlbumList());
        listView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Album> call(ListView<Album> p) {
                return new ListCell<>() {
                    protected void updateItem(Album s, boolean bln) {
                        super.updateItem(s, bln);
                        if (s != null) {
                            setText(s.getName());
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });
        Collections.sort(obsList);
        listView.setItems(obsList);
        listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal)-> showDetails(mainStage));
        if(obsList.size()!=0) {
            listView.getSelectionModel().select(0);
        }
    }

    public void showDetails(Stage mainStage){
        int index = listView.getSelectionModel().getSelectedIndex();
        if(index >= 0) {
            Album selectedAlbum = obsList.get(index);
            albumName.setText(selectedAlbum.getName());
            numberOfPhotos.setText(String.valueOf(selectedAlbum.getNumberOfPhotos()));
            String[] dateRange = selectedAlbum.getDateRange();
            rangeOfDates.setText(dateRange[0] + "-" + dateRange[1]);
        }
        else{
            albumName.setText("");
            numberOfPhotos.setText("");
            rangeOfDates.setText("");
        }
    }

    public void renameAlbum(ActionEvent actionEvent) {

    }

    public void addAlbum(ActionEvent actionEvent) {

    }

    public void openAlbum(ActionEvent actionEvent) {

    }

    public void deleteAlbum(ActionEvent actionEvent) {

    }

    public void goToSearchPage(ActionEvent actionEvent) {

    }

    public void logout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        Optional<ButtonType> option =  alert.showAndWait();

        if(option.get() == ButtonType.YES){
            try{
                User.writeData(user);
//                for(User user : admin.getUserList()){
//                    User.writeData(user);
//                }
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


}
