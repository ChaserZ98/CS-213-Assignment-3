package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import model.Album;
import model.Photo;
import model.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Control the Page for Slideshow
 * @author Feiyu Zheng
 * @author Ying Yu
 *
 *
 */
public class SlideshowPageController {
    /**
     * Create Slideshow button to go nextSlide
     */
    @FXML Button nextSlideButton;

    /**
     * Create Slideshow button to go previousSlide
     */
    @FXML Button previousSlideButton;
    /**
     * Create logout button
     */
    @FXML Button logoutButton;

    /**
     * Create a button to go AlbumPage
     */
    @FXML Button returnToAlbumPageButton;
    /**
     * Create a BorderPane for images
     */
    @FXML BorderPane borderPane;

    int currentIndex = 0;
    ArrayList<Photo> photos;

    private User user;
    private Album album;

    /**
     * The start method of slide show
     * @param mainStage the primary window instance
     * @param loginUser current login user instance
     * @param openedAlbum current opened album instance
     */
    public void start(Stage mainStage, User loginUser, Album openedAlbum){
        user = loginUser;
        album = openedAlbum;

        photos = album.getPhotoList();
        Image firstImage = null;
        try {
            firstImage = photos.get(0).getImage();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Error When Loading the Image");
            alert.initOwner(mainStage);
            alert.showAndWait();
        }

        currentIndex = 0;
        ImageView image = new ImageView(firstImage);
        image.setFitWidth(350);
        image.setFitHeight(350);

        borderPane.setCenter(image);
    }

    /**
     * Shift to next slide
     * @param actionEvent action event of hitting next slide button
     */
    public void nextSlide(ActionEvent actionEvent) {
        currentIndex++;
        if(currentIndex >= photos.size()){
            currentIndex = 0;
        }
        Image nextImage = null;
        try {
            nextImage = photos.get(currentIndex).getImage();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Error When Loading the Image");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
        ImageView image = new ImageView(nextImage);
        image.setFitWidth(350);
        image.setFitHeight(350);
        borderPane.setCenter(image);
    }

    /**
     * Shift to previous slide
     * @param actionEvent action event of hitting previous slide button
     */
    public void previousSlide(ActionEvent actionEvent) {
        currentIndex--;
        if(currentIndex < 0){
            currentIndex = photos.size()-1;
            if (currentIndex < 0) currentIndex = 0;
        }
        Image nextImage = null;
        try {
            nextImage = photos.get(currentIndex).getImage();
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Error When Loading the Image");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
        ImageView image = new ImageView(nextImage);
        image.setFitWidth(350);
        image.setFitHeight(350);
        borderPane.setCenter(image);
    }

    /**
     * logout operation
     * @param actionEvent action event of hitting logout button
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
     * Operation of returning to album page
     * @param actionEvent action event of hitting return to album page button
     */
    public void returnToAlbumPage(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Go Back");
        alert.setHeaderText("Are you sure you want to go back to album page?");
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
                String fxmlPath = "albumPage.fxml";
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/" + fxmlPath));
                AnchorPane albumPage = (AnchorPane) loader.load();
                AlbumPageController albumPageController = loader.getController();

                Scene scene = new Scene(albumPage);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Album Page");
                albumPageController.start(primaryStage, user, album);
            }
            catch(IOException e){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error When Going Back To Album Page");
                alert.setContentText("Cannot load the album page!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
        }
    }
}
