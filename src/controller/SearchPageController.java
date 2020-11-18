package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
/**
 * Control the SearchPage
 * @author Feiyu Zheng
 * @author Ying Yu
 *
 *
 */
public class SearchPageController {
    public TextField tagValue01;
    public Button Logout;
    public TextField tagValue02;
    public Button CreateSearchedAlbum;
    public Button searchByDate;
    public Button andID;
    public Button orID;
    public Button deleteID;
    public Button searchByTag;
    public Button returnToAlbumList;

    private User user;

    public void start(Stage mainStage, User loginUser){
        user = loginUser;
    }

    public void LogoutClicked(ActionEvent actionEvent) {
    }

    public void CreateSearchedAlbumClicked(ActionEvent actionEvent) {
    }

    public void searchByDateClicked(ActionEvent actionEvent) {
    }

    public void andClicked(ActionEvent actionEvent) {
    }

    public void orClicked(ActionEvent actionEvent) {
    }

    public void deleteClicked(ActionEvent actionEvent) {
    }

    public void searchByTagClicked(ActionEvent actionEvent) {
    }

    public void returnToAlbumListClicked(ActionEvent actionEvent) {
    }
}
