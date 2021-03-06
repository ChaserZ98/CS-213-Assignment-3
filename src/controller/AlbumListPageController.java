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
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.Admin;
import model.Album;
import model.User;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * Control the AlbumListPage
 * @author Feiyu Zheng
 * @author Ying Yu
 *
 *
 */

public class AlbumListPageController {

    /**
     * ListView that contains albums
     */
    @FXML ListView<Album> listView;
    /**
     * Create album button
     */
    @FXML Button createAlbumButton;
    /**
     * Rename album button
     */
    @FXML Button renameAlbumButton;
    /**
     * Delete album button
     */
    @FXML Button deleteAlbumButton;
    /**
     * Open album button
     */
    @FXML Button openAlbumButton;
    /**
     * Logout button
     */
    @FXML Button logoutButton;
    /**
     * Search button
     */
    @FXML Button goToSearchPageButton;
    /**
     * Text area to show the name of selected album
     */
    @FXML Text albumName;
    /**
     * Text area to show the total number of photos of selected album
     */
    @FXML Text numberOfPhotos;
    /**
     * Text area to show the range of date of selected album
     */
    @FXML Text rangeOfDates;
    /**
     * TextField for user to enter the new name of an album
     */
    @FXML TextField renameTextField;
    /**
     * TextField for user to enter the name when creating an album
     */
    @FXML TextField creatAlbumTextField;
    /**
     * Observalbe list of albums
     */
    private ObservableList<Album> obsList;
    /**
     * The login user instance
     */
    private User user;

    /**
     * The start method of album list page
     * @param mainStage the primary window instance
     * @param loginUser the login user instance
     */
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
        mainStage.setOnCloseRequest(this::close);
    }

    /**
     * Display the album details after selected an album
     * @param mainStage the primary window instance
     */
    public void showDetails(Stage mainStage){
        int index = listView.getSelectionModel().getSelectedIndex();
        if(index >= 0) {
            Album selectedAlbum = obsList.get(index);
            albumName.setText(selectedAlbum.getName());
            numberOfPhotos.setText(String.valueOf(selectedAlbum.getNumberOfPhotos()));
            String[] dateRange = selectedAlbum.getDateRange();
            if (dateRange != null){
                rangeOfDates.setText(dateRange[0] + " - " + dateRange[1]);
            }
            else{
                rangeOfDates.setText("");
            }
        }
        else{
            albumName.setText("");
            numberOfPhotos.setText("");
            rangeOfDates.setText("");
        }
    }

    /**
     * Rename album operation
     * @param actionEvent action event of hitting the rename album button
     */
    public void renameAlbum(ActionEvent actionEvent) {
        int index = listView.getSelectionModel().getSelectedIndex();
        //If there is an album being selected
        if(index >= 0){
            Album album = obsList.get(index);
            String newName = renameTextField.getText();
            String renameDetail = "Original Name: " + album.getName() + "\nNew Name: " + newName;
            //If entered album name is empty
            if(newName.length() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR, "The album name cannot be empty", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Empty album name!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            else{
                //If the user makes any change
                if(!album.getName().equals(newName)){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, renameDetail, ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Rename Album");
                    alert.setHeaderText("Are you sure you want to do such a rename?");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    Optional<ButtonType> option =  alert.showAndWait();
                    if(option.get() == ButtonType.YES){
                        try{
                            user.renameAlbum(album, newName);
                        }
                        catch (Admin.RepeatedUserException e){
                            alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Illegal Manipulation!");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                    }
                }
                //If the user doesn't change anything
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "You don't change anything.", ButtonType.OK);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Illegal Manipulation!");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }
        }
        //If no album is selected
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no album selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Create album operation
     * @param actionEvent action event of hitting the create album button
     */
    public void createAlbum(ActionEvent actionEvent) {
        String newAlbumName = creatAlbumTextField.getText();
        if(newAlbumName.length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "The album name cannot be empty", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Empty album name!");
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Album name: " + newAlbumName, ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Create Album");
            alert.setHeaderText("Are you sure you want to create this album?");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            Optional<ButtonType> option =  alert.showAndWait();
            if(option.get() == ButtonType.YES){
                try{
                    Album newAlbum = new Album(newAlbumName);
                    user.createAlbum(newAlbum, null);
                    obsList.setAll(user.getAlbumList());
                    int newIndex = obsList.indexOf(newAlbum);
                    listView.getSelectionModel().select(newIndex);
                }
                catch (User.RepeatedAlbumException e){
                    alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Illegal Manipulation!");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Open album operation
     * @param actionEvent action event of hitting the open album button
     */
    public void openAlbum(ActionEvent actionEvent) {
        int index = listView.getSelectionModel().getSelectedIndex();
        if(index >= 0){
            User.writeData(user);
            try{
                Stage primaryStage = (Stage)openAlbumButton.getScene().getWindow();

                String fxmlPath = "albumPage.fxml";
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/" + fxmlPath));
                AnchorPane adminPage = (AnchorPane) loader.load();
                AlbumPageController albumPageController = loader.getController();
                albumPageController.start(primaryStage, user, obsList.get(index));

                Scene scene = new Scene(adminPage);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Album Page");
            }
            catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error When Loading The Album Page");
                alert.setContentText("Cannot load the album page!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
                e.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no album selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Delete album operation
     * @param actionEvent action event of hitting the delete album button
     */
    public void deleteAlbum(ActionEvent actionEvent) {
        int index = listView.getSelectionModel().getSelectedIndex();
        //If there is an album being selected
        if(index >= 0){
            Album album = obsList.get(index);
            String newName = renameTextField.getText();
            String deleteDetail = "Name: " + album.getName() + "\nNumber of Photos: " + album.getNumberOfPhotos() + "\nDate Range: " + (album.getDateRange()==null ? "" : album.getDateRange()[0] + "-" + album.getDateRange()[1]);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, deleteDetail, ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Delete Album");
            alert.setHeaderText("Are you sure you want to delete this album?");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            Optional<ButtonType> option =  alert.showAndWait();
            if(option.get() == ButtonType.YES){
                user.deleteAlbum(album);
                obsList.setAll(user.getAlbumList());
                if(index < obsList.size()){
                    listView.getSelectionModel().clearSelection(index);
                    listView.getSelectionModel().select(index);
                }
                //Select the latter one if there is no album before the removed one
                else if(index - 1 >=0){
                    listView.getSelectionModel().select(index - 1);
                }
                else{
                    listView.getSelectionModel().clearSelection(index);
                }
            }
        }
        //If no album is selected
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no album selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Go to search page operation
     * @param actionEvent action event of hitting the search button
     */
    public void goToSearchPage(ActionEvent actionEvent) {
        User.writeData(user);
        try{
            Stage primaryStage = (Stage)goToSearchPageButton.getScene().getWindow();

            String fxmlPath = "searchPage.fxml";
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/" + fxmlPath));
            AnchorPane adminPage = (AnchorPane) loader.load();
            SearchPageController searchPageController = loader.getController();
            searchPageController.start(primaryStage, user);

            Scene scene = new Scene(adminPage);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Search Page");
        }
        catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error When Loading The Search Page");
            alert.setContentText("Cannot load the search page!");
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Logout operation
     * @param actionEvent action event of hitting the logout button
     */
    public void logout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
        Optional<ButtonType> option =  alert.showAndWait();

        if(option.get() == ButtonType.YES){
            try{
                User.writeData(user);
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
                primaryStage.setTitle("Login Page");
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

    /**
     * Close window operation
     * @param windowEvent window event of hitting the exit button
     */
    public void close(WindowEvent windowEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit the program?");
        alert.initOwner((Stage)windowEvent.getSource());
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == ButtonType.YES) {
            User.writeData(user);
        }
        else{
            windowEvent.consume();
        }
    }

}
