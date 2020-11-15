package app;

import controller.LoginPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Photos extends Application{

    @Override
    public void start(Stage primaryStage){
        try {
            String fxmlPath = "loginPage.fxml";

            primaryStage.setTitle(fxmlPath);
            primaryStage.setResizable(false);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/" + fxmlPath));
            AnchorPane loginPage = (AnchorPane) loader.load();

            LoginPageController loginPageController = loader.getController();
            loginPageController.start(primaryStage);

            Scene scene = new Scene(loginPage);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error When Loading The Login Page!");
            alert.setContentText("Cannot load the Login Page!");
            alert.show();
        }
    }

    public static void main(String[] args){

//        Photo photo1 = new Photo("data/photo1.jpg");
//        Photo photo2 = new Photo("data/photo2.jpg");
//        Photo photo3 = new Photo("data/photo3.jpg");
//        Photo photo4 = new Photo("data/photo4.jpg");
//
//        photo1.addUniqueValueTag("unique", "value");
//        photo2.addUniqueValueTag("unique", "value");
//        photo3.addUniqueValueTag("unique", "value");
//        photo4.addUniqueValueTag("unique", "value");
//
//        photo1.addMultipleValueTag("multiple", "value1");
//        photo1.addMultipleValueTag("multiple", "value2");
//
//        photo2.addMultipleValueTag("multiple", "value2");
//        photo2.addMultipleValueTag("multiple1", "value1");
//
//        photo4.addMultipleValueTag("multiple", "value2");
//        photo4.addMultipleValueTag("multiple1", "value1");
//        photo4.addMultipleValueTag("multiple", "value");
//
//        User user = new User("name");
//        Album album = new Album("name");
//        album.addPhoto(photo1);
//        album.addPhoto(photo2);
//        album.addPhoto(photo3);
//        album.addPhoto(photo4);
//        user.getAlbumList().add(album);
//
//        Tag targetTag1 = new UniqueValueTag("unique", "value");
//        Tag targetTag2 = new MultipleValueTag("multiple", "value1");
//        System.out.println(user.searchPhotoByTag(user.getAlbumList().get(0), targetTag2));
////        System.out.println(user.albumList.get(0).photoList.get(3).tagList);

        launch(args);
    }
}
