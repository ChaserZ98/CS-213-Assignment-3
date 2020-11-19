package controller;

import javafx.scene.layout.BorderPane;
import model.Album;
import model.Photo;
import model.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Album;
import model.User;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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
    public Button nextSlide;

    /**
     * Create Slideshow button to go previousSlide
     */
    public Button previousSlide;
    /**
     * Create logout button
     */
    public Button Logout;

    /**
     * Create a button to go AlbumPage
     */
    public Button returnToAlbumPage;
    /**
     * Create a BorderPane for images
     */
    public BorderPane borderPandID;

    int currentIndex = 0;
    ArrayList<Photo> photos;


    /**
     * Start method
     * @param album the album to store photos
     */
    public void start(Album album) throws FileNotFoundException {

        photos = album.getPhotos();
        Image firstImage = photos.get(0).getImage();

        currentIndex = 0;
        ImageView image = new ImageView(firstImage);
        image.setFitWidth(200);
        image.setFitHeight(200);


        borderPandID.setCenter(image);
    }

    /**
     *
     * @param actionEvent
     */
    public void nextSlideClicked(ActionEvent actionEvent) {
        currentIndex++;
        if(currentIndex >= photos.size()){
            currentIndex = 0;
        }
        Image nextImage = null;
        try {
            nextImage = photos.get(currentIndex).getImage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        Image nextImage = null;
        try {
            nextImage = photos.get(currentIndex).getImage();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
