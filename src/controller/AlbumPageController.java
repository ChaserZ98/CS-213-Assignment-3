package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Album;
import model.User;
/**
 * Control the AlbumPage
 * @author Feiyu Zheng
 * @author Ying Yu
 *
 *
 */


public class AlbumPageController {

    public Button renameCaption;
    public Button addPhoto;
    public Button deletePhoto;
    public Button addCaption;
    public Button Logout;
    public TextField addCaptionID;
    public TextField renameCaptionID;
    public Button backToAlbum;
    public Button slideShow;
    public TextField tagValue;
    public Button addTag;
    public TextField newTagName;
    public Button deleteTag;
    public Button displayPhoto;
    public Button movePhoto;
    public Button copyPhoto;
    public Button search;

    private User user;
    private Album album;

    public void start(Stage mainStage, User loginUser, Album openedAlbum){
        user = loginUser;
        album = openedAlbum;
    }

    public void renameCaptionClicked(ActionEvent actionEvent) {
    }

    public void addPhotoClicked(ActionEvent actionEvent) {
    }

    public void deletePhotoClicked(ActionEvent actionEvent) {
    }

    public void addCaptionClicked(ActionEvent actionEvent) {
    }

    public void LogoutClicked(ActionEvent actionEvent) {
    }

    public void backToAlbumClicked(ActionEvent actionEvent) {
    }

    public void slideShowClicked(ActionEvent actionEvent) {
    }

    public void addTagClicked(ActionEvent actionEvent) {
    }

    public void deleteTagClicked(ActionEvent actionEvent) {
    }

    public void displayPhotoClicked(ActionEvent actionEvent) {
    }

    public void movePhotoClicked(ActionEvent actionEvent) {
    }

    public void copyPhotoClicked(ActionEvent actionEvent) {
    }

    public void searchClicked(ActionEvent actionEvent) {
    }
}
