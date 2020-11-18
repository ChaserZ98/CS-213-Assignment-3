package controller;

import javafx.scene.layout.BorderPane;
import model.Album;
import model.Photo;
import model.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;

public class SlideshowPageController {
    public Button nextSlide;
    public Button previousSlide;
    public Button Logout;
    public Button returnToAlbumPage;
    public BorderPane borderPandID;

    int currentIndex = 0;
    ArrayList<Photo> photos;

    public void start(Album album){

        photos = album.getPhotos();
        Image firstImage = photos.get(0).getImage();
        currentIndex = 0;
        ImageView image = new ImageView(firstImage);
        image.setFitWidth(200);
        image.setFitHeight(200);


        borderPandID.setCenter(image);
    }
    public void nextSlideClicked(ActionEvent actionEvent) {
        currentIndex++;
        if(currentIndex >= photos.size()){
            currentIndex = 0;
        }
        Image nextImage = photos.get(currentIndex).getImage();
        ImageView image = new ImageView(nextImage);
        image.setFitWidth(200);
        image.setFitHeight(200);
        borderPandID.setCenter(image);
    }

    public void previousSlideClicked(ActionEvent actionEvent) {
        currentIndex--;
        if(currentIndex < 0){
            currentIndex = photos.size()-1;
            if (currentIndex < 0) currentIndex = 0;
        }
        Image nextImage = photos.get(currentIndex).getImage();
        ImageView image = new ImageView(nextImage);
        image.setFitWidth(200);
        image.setFitHeight(200);
        borderPandID.setCenter(image);
    }

    public void LogoutClicked(ActionEvent actionEvent) {
    }

    public void returnToAlbumPageClicked(ActionEvent actionEvent) {
    }
}
