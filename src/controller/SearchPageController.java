package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

/**
 * Control the SearchPage
 * @author Feiyu Zheng
 * @author Ying Yu
 *
 *
 */
public class SearchPageController {
    @FXML DatePicker earliestDatePicker;
    @FXML DatePicker latestDatePicker;
    @FXML Button searchByDateButton;
    @FXML ChoiceBox<Tag> tagName1ChoiceBox;
    @FXML TextField tagValue1TextField;
    @FXML Button andButton;
    @FXML Button orButton;
    @FXML ChoiceBox<Tag> tagName2ChoiceBox;
    @FXML TextField tagValue2TextField;
    @FXML Button deleteButton;
    @FXML Button searchByTag;
    @FXML Button createSearchedAlbum;
    @FXML TilePane tilePane;
    @FXML Button returnToAlbumListButton;
    @FXML Button logoutButton;
    @FXML TextField newAlbumTextField;
    @FXML Text tagName2Text;
    @FXML Text tagValue2Text;
    @FXML Button resetButton;


    private User user;

    ObservableList<Photo> thumbnailPhotos;
    ObservableList<Tag> existedTags;
    ArrayList<StackPane> imagePanes;
    private boolean isAnd;
    private boolean isOr;

    /**
     * The start method for search page
     * @param mainStage the primary window
     * @param loginUser the login user
     */
    public void start(Stage mainStage, User loginUser){
        user = loginUser;

        imagePanes = new ArrayList<>();
        thumbnailPhotos = FXCollections.observableArrayList();

        existedTags = FXCollections.observableArrayList();
        existedTags.setAll(user.getCreatedTags());

        tagName1ChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                return tag.getTagName();
            }

            @Override
            public Tag fromString(String s) {
                return null;
            }
        });
        tagName2ChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                return tag.getTagName();
            }

            @Override
            public Tag fromString(String s) {
                return null;
            }
        });
        tagName1ChoiceBox.setItems(existedTags);
        tagName1ChoiceBox.getSelectionModel().select(0);
        tagName2ChoiceBox.setItems(existedTags);
        tagName2ChoiceBox.getSelectionModel().select(0);
        mainStage.setOnCloseRequest(this::close);
    }

    /**
     * Search by date operation
     * @param actionEvent action event of hitting search by date Button
     */
    public void searchByDate(ActionEvent actionEvent){
        LocalDate earliestDatePickerValue = earliestDatePicker.getValue();
        LocalDate latestDatePickerValue = latestDatePicker.getValue();
        if(earliestDatePickerValue != null && latestDatePickerValue != null){
            ZoneId defaultZoneId = ZoneId.systemDefault();
            Date earliestDate = Date.from(earliestDatePickerValue.atStartOfDay(defaultZoneId).toInstant());
            Date latestDate = Date.from(latestDatePickerValue.atStartOfDay(defaultZoneId).toInstant());
            if(earliestDate.getTime() - latestDate.getTime() > 0){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Earliest date must be earlier than latest date", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Date error!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            else{
                ArrayList<Photo> result = user.searchPhotoByDate(earliestDate, latestDate);
                if(result.size() == 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                    alert.setTitle("Search By Date");
                    alert.setHeaderText("No photo found!");
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
                else{
                    thumbnailPhotos.setAll(result);
                    for(Photo photo : thumbnailPhotos){
                        ImageView iv;
                        try{
                            iv = new ImageView(photo.getImage());
                            iv.setFitWidth(80.0);
                            iv.setFitHeight(80.0);
                            iv.setPickOnBounds(true);
                            StackPane sp = new StackPane();
                            sp.setStyle("-fx-padding: 3");
                            sp.getChildren().add(iv);
                            imagePanes.add(sp);
                        }
                        catch(FileNotFoundException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Error When Loading the Image");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                        catch(Photo.ImageErrorException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Error When Loading the Image!");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                        tilePane.getChildren().setAll(imagePanes);
                    }
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "You must select both of the date", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Date Missing!");
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * UI effect
     * @param actionEvent action event of hitting and button
     */
    public void and(ActionEvent actionEvent){
        isAnd = true;
        orButton.setVisible(false);
        tagName2Text.setVisible(true);
        tagName2ChoiceBox.setVisible(true);
        tagName2Text.setVisible(true);
        tagValue2Text.setVisible(true);
        tagValue2TextField.setVisible(true);
        resetButton.setVisible(true);
        searchByTag.setLayoutY(470.0);
    }

    /**
     * UI effect
     * @param actionEvent action event of hitting or button
     */
    public void or(ActionEvent actionEvent){
        isOr = true;
        andButton.setVisible(false);
        tagName2Text.setVisible(true);
        tagName2ChoiceBox.setVisible(true);
        tagName2Text.setVisible(true);
        tagValue2Text.setVisible(true);
        tagValue2TextField.setVisible(true);
        resetButton.setVisible(true);
        searchByTag.setLayoutY(470.0);
    }

    /**
     * Reset the UI for search by tag
     * @param actionEvent action event of hitting the reset button
     */
    public void reset(ActionEvent actionEvent){
        isAnd = false;
        isOr = false;
        andButton.setVisible(true);
        orButton.setVisible(true);
        tagName2Text.setVisible(false);
        tagName2ChoiceBox.setVisible(false);
        tagName2Text.setVisible(false);
        tagValue2Text.setVisible(false);
        tagValue2TextField.setVisible(false);
        resetButton.setVisible(false);
        searchByTag.setLayoutY(390.0);
    }

    /**
     * Search photos by tag
     * @param actionEvent action event of hitting search by tag Button
     */
    public void searchByTag(ActionEvent actionEvent){
        if(isAnd){
            if(tagValue1TextField.getText().length() == 0 || tagValue2TextField.getText().length() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR, "If you selected and, you must enter two tag combinations.", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Missing value!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            else{
                Tag selectedTag1 = tagName1ChoiceBox.getSelectionModel().getSelectedItem();
                Tag selectedTag2 = tagName2ChoiceBox.getSelectionModel().getSelectedItem();
                Tag tag1 = selectedTag1 instanceof MultipleValueTag ? new MultipleValueTag(selectedTag1.tagName, tagValue1TextField.getText()) : new UniqueValueTag(selectedTag1.tagName, tagValue2TextField.getText());
                Tag tag2 = selectedTag2 instanceof MultipleValueTag ? new MultipleValueTag(selectedTag2.tagName, tagValue1TextField.getText()) : new UniqueValueTag(selectedTag2.tagName, tagValue2TextField.getText());
                ArrayList<Photo> result = user.searchPhotoByTags(tag1, tag2, false);
                if(result.size() == 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                    alert.setTitle("Search By Date");
                    alert.setHeaderText("No photo found!");
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
                else{
                    thumbnailPhotos.setAll(result);
                    for(Photo photo : thumbnailPhotos){
                        ImageView iv;
                        try{
                            iv = new ImageView(photo.getImage());
                            iv.setFitWidth(80.0);
                            iv.setFitHeight(80.0);
                            iv.setPickOnBounds(true);
                            StackPane sp = new StackPane();
                            sp.setStyle("-fx-padding: 3");
                            sp.getChildren().add(iv);
                            imagePanes.add(sp);
                        }
                        catch(FileNotFoundException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Error When Loading the Image");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                        catch(Photo.ImageErrorException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Error When Loading the Image!");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                        tilePane.getChildren().setAll(imagePanes);
                    }
                }
            }
        }
        else if(isOr){
            if(tagValue1TextField.getText().length() == 0 || tagValue2TextField.getText().length() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR, "If you selected and, you must enter two tag combinations.", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Missing value!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            else{
                Tag selectedTag1 = tagName1ChoiceBox.getSelectionModel().getSelectedItem();
                Tag selectedTag2 = tagName2ChoiceBox.getSelectionModel().getSelectedItem();
                Tag tag1 = selectedTag1 instanceof MultipleValueTag ? new MultipleValueTag(selectedTag1.tagName, tagValue1TextField.getText()) : new UniqueValueTag(selectedTag1.tagName, tagValue2TextField.getText());
                Tag tag2 = selectedTag2 instanceof MultipleValueTag ? new MultipleValueTag(selectedTag2.tagName, tagValue1TextField.getText()) : new UniqueValueTag(selectedTag2.tagName, tagValue2TextField.getText());
                ArrayList<Photo> result = user.searchPhotoByTags(tag1, tag2, true);
                if(result.size() == 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                    alert.setTitle("Search By Date");
                    alert.setHeaderText("No photo found!");
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
                else{
                    thumbnailPhotos.setAll(result);
                    for(Photo photo : thumbnailPhotos){
                        ImageView iv;
                        try{
                            iv = new ImageView(photo.getImage());
                            iv.setFitWidth(80.0);
                            iv.setFitHeight(80.0);
                            iv.setPickOnBounds(true);
                            StackPane sp = new StackPane();
                            sp.setStyle("-fx-padding: 3");
                            sp.getChildren().add(iv);
                            imagePanes.add(sp);
                        }
                        catch(FileNotFoundException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Error When Loading the Image");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                        catch(Photo.ImageErrorException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Error When Loading the Image!");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                        tilePane.getChildren().setAll(imagePanes);
                    }
                }
            }
        }
        else{
            if(tagValue1TextField.getText().length() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR, "To search, you need to enter the tag value.", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Empty tag value!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            else{
                Tag selectedTag1 = tagName1ChoiceBox.getSelectionModel().getSelectedItem();
                Tag tag1 = selectedTag1 instanceof MultipleValueTag ? new MultipleValueTag(selectedTag1.tagName, tagValue1TextField.getText()) : new UniqueValueTag(selectedTag1.tagName, tagValue1TextField.getText());
                ArrayList<Photo> result = user.searchPhotoByTag(tag1);
                if(result.size() == 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, null, ButtonType.OK);
                    alert.setTitle("Search By Date");
                    alert.setHeaderText("No photo found!");
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
                else{
                    thumbnailPhotos.setAll(result);
                    for(Photo photo : thumbnailPhotos){
                        ImageView iv;
                        try{
                            iv = new ImageView(photo.getImage());
                            iv.setFitWidth(80.0);
                            iv.setFitHeight(80.0);
                            iv.setPickOnBounds(true);
                            StackPane sp = new StackPane();
                            sp.setStyle("-fx-padding: 3");
                            sp.getChildren().add(iv);
                            imagePanes.add(sp);
                        }
                        catch(FileNotFoundException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Error When Loading the Image");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                        catch(Photo.ImageErrorException e){
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Error When Loading the Image!");
                            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                        }
                        tilePane.getChildren().setAll(imagePanes);
                    }
                }
            }
        }
    }

    /**
     * Created album containing search result
     * @param actionEvent action event of hitting the createSearchedAlbumButton
     */
    public void createSearchedAlbum(ActionEvent actionEvent){
        if(thumbnailPhotos.size() != 0){
            if(newAlbumTextField.getText().length() != 0){
                String albumDetails = "Album Name: \t" + newAlbumTextField.getText() + "\nNumber of photos in search result: \t" + thumbnailPhotos.size();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, albumDetails, ButtonType.YES, ButtonType.CANCEL);
                alert.setTitle("Go Back");
                alert.setHeaderText("Are you sure you want to create this album?");
                alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                Optional<ButtonType> option =  alert.showAndWait();
                if(option.get() == ButtonType.YES){
                    ArrayList<Photo> photoList = new ArrayList<>(thumbnailPhotos);
                    try{
                        user.createAlbum(newAlbumTextField.getText(), photoList);
                    }
                    catch(User.RepeatedAlbumException e){
                        alert = new Alert(Alert.AlertType.INFORMATION, "Cannot create an album with a duplicate name.", ButtonType.OK);
                        alert.setTitle("Error!");
                        alert.setHeaderText("Album Name Already exists!");
                        alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                        alert.showAndWait();
                    }
                    thumbnailPhotos.clear();
                    tilePane.getChildren().clear();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cannot create an album with empty album name.", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Empty Album Name!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your search result doesn't have any photos to create album with.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Empty Search Result!");
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Return to album list operation
     * @param actionEvent action event of hitting return to album list button
     */
    public void returnToAlbumList(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Go Back");
        alert.setHeaderText("Are you sure you want to go back to album List Page?");
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
                String fxmlPath = "albumListPage.fxml";
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/" + fxmlPath));
                AnchorPane albumPage = (AnchorPane) loader.load();
                AlbumListPageController albumListPageController = loader.getController();

                Scene scene = new Scene(albumPage);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Album Page");
                albumListPageController.start(primaryStage, user);
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

    /**
     * Logout Operation
     * @param actionEvent action event of hitting the logout button
     */
    public void logout(ActionEvent actionEvent){
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
        if(! (option.get() == ButtonType.YES)) {
            windowEvent.consume();
        }
    }
}
