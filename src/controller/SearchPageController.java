package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import model.Album;
import model.Photo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    public DatePicker startDate;
    public DatePicker endDate;

    private User user;

    public void start(Stage mainStage, User loginUser){

        user = loginUser;
    }

    public void LogoutClicked(ActionEvent actionEvent) {
    }

    public void CreateSearchedAlbumClicked(ActionEvent actionEvent) {
    }

    public ArrayList<Photo> searchByDateClicked(ActionEvent actionEvent) {
        ArrayList<Photo> photos = new ArrayList<Photo>();
        ArrayList<Album> albums = new ArrayList<Album>();
        albums = user.getAlbumList();

        LocalDate lowDate = startDate.getValue();
        Date firstdate = Date.from(lowDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDate highDate = endDate.getValue();
        Date seconddate = Date.from(highDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        for(int i = 0; i < albums.size(); i++){
            //For each album belonging to user
            photos = albums.get(i).getPhotosByDateRange(firstdate, seconddate);

        }

        return photos;

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
