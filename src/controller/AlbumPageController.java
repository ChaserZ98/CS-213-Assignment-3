package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import model.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Controller of Album Page
 * @author Feiyu Zheng
 * @author Ying Yu
 */
public class AlbumPageController {

    /**
     * Area to display album name
     */
    @FXML Text albumNameText;
    /**
     * Tile pane which contains all the images
     */
    @FXML TilePane tilePane;
    /**
     * Add photo Button
     */
    @FXML Button addPhotoButton;
    /**
     * Delete photo
     */
    @FXML Button deletePhotoButton;
    /**
     * Slideshow Button
     */
    @FXML Button slideshowButton;
    /**
     * Drop-down Box for existed album
     */
    @FXML ChoiceBox<Album> albumChoiceBox;
    /**
     * Copy photo button
     */
    @FXML Button copyButton;
    /**
     * Move photo button
     */
    @FXML Button moveButton;
    /**
     * Component to show the image
     */
    @FXML ImageView imageView;
    /**
     * TextField for image caption
     */
    @FXML TextField captionTextField;
    /**
     * Caption button
     */
    @FXML Button captionButton;
    /**
     * Area to display the date of selected image
     */
    @FXML Text dateText;
    /**
     * TableView to show tag of photo
     */
    @FXML TableView<Tag> tableView;
    /**
     * First column of TableView which contains tagName
     */
    @FXML TableColumn<Tag, String> tagNameColumn;
    /**
     * Second column of TableView which contains tagValue
     */
    @FXML TableColumn<Tag, String> tagValueColumn;
    /**
     * Drop-down box for existed tagName
     */
    @FXML ChoiceBox<Tag> tagNameChoiceBox;
    /**
     * Text that indicates where to enter the new tag name
     */
    @FXML Text newTagNameText;
    /**
     * TextField for user to enter the new tag name
     */
    @FXML TextField newTagNameTextField;
    /**
     * Text that indicates where to select the type of new tag
     */
    @FXML Text newTagTypeText;
    /**
     * Drop-down box for user to select between two tag types
     */
    @FXML ChoiceBox<String> newTagTypeChoiceBox;
    /**
     * Text that indicates where to enter the tag value
     */
    @FXML Text tagValueText;
    /**
     * TextField for user to enter tag value
     */
    @FXML TextField tagValueTextField;
    /**
     * Add tag button
     */
    @FXML Button addTagButton;
    /**
     * Delete tag Button
     */
    @FXML Button deleteTagButton;
    /**
     * Go back Button
     */
    @FXML Button goBackButton;
    /**
     * Logout Button
     */
    @FXML Button logoutButton;

    /**
     * Login user instance
     */
    private User user;
    /**
     * Opened Album instance
     */
    private Album album;

    /**
     * Observable list for thumbnail images
     */
    ObservableList<Photo> thumbnailPhotos;
    /**
     * Observable list for existed tags
     */
    ObservableList<Tag> existedTags;
    /**
     * Observable list for two tag types
     */
    ObservableList<String> tagTypes;
    /**
     * Observable list for tags
     */
    ObservableList<Tag> tagObservableList;
    /**
     * Observable list for existed album
     */
    ObservableList<Album> albumObservableList;
    /**
     * StackPane arraylist. Each of them contains an ImageView component
     */
    ArrayList<StackPane> imagePanes;
    /**
     * The StackPane of selected image
     */
    StackPane selectedStackPane;

    /**
     * The start method of album page
     * @param mainStage the primary window instance
     * @param loginUser the login user instance
     * @param openedAlbum the opened album instance
     */
    public void start(Stage mainStage, User loginUser, Album openedAlbum){
        user = loginUser;
        album = openedAlbum;
        albumNameText.setText(album.getName());

        tagObservableList = FXCollections.observableArrayList();
        tagNameColumn.setCellValueFactory(new PropertyValueFactory<>("tagName"));
        tagValueColumn.setCellValueFactory(new PropertyValueFactory<>("tagValue"));

        imagePanes = new ArrayList<>();
        thumbnailPhotos = FXCollections.observableArrayList();
        thumbnailPhotos.setAll(album.getPhotoList());
        for(Photo photo : thumbnailPhotos){
            ImageView iv;
            try{
                iv = new ImageView(photo.getImage());
                iv.setFitWidth(80.0);
                iv.setFitHeight(80.0);
                iv.setPickOnBounds(true);
                iv.setOnMouseClicked(this::showPhotoInfo);
                StackPane sp = new StackPane();
                sp.setStyle("-fx-padding: 3");
                sp.getChildren().add(iv);
                imagePanes.add(sp);
            }
            catch(FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Error When Loading the Image");
                alert.initOwner(mainStage);
                alert.showAndWait();
            }
            catch(Photo.ImageErrorException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Error When Loading the Image!");
                alert.initOwner(mainStage);
                alert.showAndWait();
            }
        }
        tilePane.getChildren().setAll(imagePanes);

        existedTags = FXCollections.observableArrayList();
        existedTags.setAll(user.getCreatedTags());
        existedTags.add(new UniqueValueTag("other", ""));
        tagNameChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Tag tag) {
                return tag.getTagName();
            }

            @Override
            public Tag fromString(String s) {
                return null;
            }
        });
        tagNameChoiceBox.setItems(existedTags);
        tagNameChoiceBox.getSelectionModel().select(0);
        tagNameChoiceBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal)-> showInvisible(mainStage));

        tagTypes = FXCollections.observableArrayList();
        tagTypes.add("UniqueValueTag");
        tagTypes.add("MultipleValueTag");
        newTagTypeChoiceBox.setItems(tagTypes);
        newTagTypeChoiceBox.getSelectionModel().select(0);

        albumObservableList = FXCollections.observableArrayList();
        albumObservableList.setAll(user.getAlbumList());
        albumChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Album album) {
                return album.getName();
            }

            @Override
            public Album fromString(String s) {
                return null;
            }
        });
        albumChoiceBox.setItems(albumObservableList);
        albumChoiceBox.getSelectionModel().select(0);

        mainStage.setOnCloseRequest(this::close);

        if(thumbnailPhotos.size() != 0){
            MouseEvent mouseEvent = new MouseEvent(imagePanes.get(0).getChildren().get(0), imagePanes.get(0).getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                    true, true, true, true, true, true, null);
            Event.fireEvent(imagePanes.get(0).getChildren().get(0), mouseEvent);
        }
    }

    /**
     * Show hidden UI field operation
     * @param mainStage the main window
     */
    public void showInvisible(Stage mainStage){
        int index = tagNameChoiceBox.getSelectionModel().getSelectedIndex();
        Tag selectedTag = existedTags.get(index);
        if(selectedTag != null) {
            if(selectedTag.getTagName().equals("other")){
                newTagNameText.setVisible(true);
                newTagNameTextField.setVisible(true);
                newTagTypeText.setVisible(true);
                newTagTypeChoiceBox.setVisible(true);

                tagValueText.setLayoutY(720.0);
                tagValueTextField.setLayoutY(705.0);
                addTagButton.setLayoutY(750.0);
                deleteTagButton.setLayoutY(750.0);
            }
            else{
                newTagNameText.setVisible(false);
                newTagNameTextField.setVisible(false);
                newTagTypeText.setVisible(false);
                newTagTypeChoiceBox.setVisible(false);

                tagValueText.setLayoutY(660.0);
                tagValueTextField.setLayoutY(645.0);
                addTagButton.setLayoutY(690.0);
                deleteTagButton.setLayoutY(690.0);
            }
        }
        else{
            newTagNameText.setVisible(false);
            newTagNameTextField.setVisible(false);
            newTagTypeText.setVisible(false);
            newTagTypeChoiceBox.setVisible(false);

            tagValueText.setLayoutY(660.0);
            tagValueTextField.setLayoutY(645.0);
            addTagButton.setLayoutY(690.0);
            deleteTagButton.setLayoutY(690.0);
        }
    }

    /**
     * Show photo info operation
     * @param mouseEvent mouse event of clicking on any photo
     */
    private void showPhotoInfo(MouseEvent mouseEvent) {
        if(selectedStackPane != null){
            selectedStackPane.setStyle("-fx-border-width: 0;");
        }
        selectedStackPane = (StackPane) ((ImageView)mouseEvent.getSource()).getParent();
        selectedStackPane.setStyle("-fx-border-width: 2; -fx-border-color: black");
        int index = imagePanes.indexOf(selectedStackPane);
        Photo photo = thumbnailPhotos.get(index);
        captionTextField.setText(photo.getCaption());
        dateText.setText(photo.getDate());
        try {
            imageView.setImage(photo.getImage());
        }
        catch (FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Error When Loading the Image");
            alert.initOwner(((Node)mouseEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }

        tagObservableList = FXCollections.observableArrayList();
        for(Tag tag : photo.getTagList()){
            if(tag instanceof MultipleValueTag){
                tagObservableList.add(((MultipleValueTag)tag));
            }
            else if(tag instanceof UniqueValueTag){
                tagObservableList.add(((UniqueValueTag)tag));
            }
        }
        tableView.setItems(tagObservableList);
        if(tagObservableList.size() != 0){
            tableView.getSelectionModel().select(0);
        }
    }

    /**
     * Add photo operation
     * @param actionEvent action event of hitting add photo button
     */
    public void addPhoto(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("data"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.JPG")
                ,new FileChooser.ExtensionFilter("PNG Files", "*.PNG")
                ,new FileChooser.ExtensionFilter("PNG Files", "*.BMP")
                ,new FileChooser.ExtensionFilter("PNG Files", "*.GIF")
        );
        File selectedFile = fileChooser.showOpenDialog(((Node)actionEvent.getSource()).getScene().getWindow());

        if(selectedFile != null){
            Photo photo = null;
            try{
                photo = new Photo(selectedFile.getAbsolutePath());
            }
            catch(FileNotFoundException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot find file " + selectedFile.getAbsolutePath(), ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("File Not Found!");
                alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            if(photo != null){
                try{
                    int index = user.getPhotos().indexOf(photo);
                    if(index >= 0){
                        photo = user.getPhotos().get(index);
                    }
                    user.addPhoto(album, photo);
                    thumbnailPhotos.add(photo);
                    ImageView iv;
                    try{
                        iv = new ImageView(photo.getImage());
                        iv.setFitWidth(80.0);
                        iv.setFitHeight(80.0);
                        iv.setPickOnBounds(true);
                        iv.setOnMouseClicked(this::showPhotoInfo);
                        StackPane sp = new StackPane();
                        sp.setStyle("-fx-padding: 3");
                        sp.getChildren().add(iv);
                        imagePanes.add(sp);
                        tilePane.getChildren().setAll(imagePanes);

                        MouseEvent mouseEvent = new MouseEvent(sp.getChildren().get(0), sp.getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null);
                        Event.fireEvent(sp.getChildren().get(0), mouseEvent);
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
                        alert.setHeaderText("Error When Loading the Image");
                        alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                        alert.showAndWait();
                    }
                }
                catch(Album.PhotoExistedException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Photo Already Existed!");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Delete photo operation
     * @param actionEvent action event of hitting delete photo button
     */
    public void deletePhoto(ActionEvent actionEvent){
        if(selectedStackPane != null){
            int index = imagePanes.indexOf(selectedStackPane);
            Photo photo = thumbnailPhotos.get(index);
            String photoDetails = "Address: \t" + photo.getAddress() + "\nCaption: \t" + photo.getCaption() + "\nDate: \t" + photo.getDate();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, photoDetails, ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you sure you want to delete this photo?");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            Optional<ButtonType> option =  alert.showAndWait();
            if(option.get() == ButtonType.YES){
                user.deletePhoto(album, album.getPhotoList().get(index));
                imagePanes.remove(index);
                thumbnailPhotos.setAll(album.getPhotoList());
                tilePane.getChildren().setAll(imagePanes);
                if(index < album.getPhotoList().size()){
                    StackPane sp = imagePanes.get(index);
                    MouseEvent mouseEvent = new MouseEvent(sp.getChildren().get(0), sp.getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                            0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                            true, true, true, true, true, true, null);
                    Event.fireEvent(sp.getChildren().get(0), mouseEvent);
                }
                else if(index - 1 >=0){
                    StackPane sp = imagePanes.get(index - 1);
                    MouseEvent mouseEvent = new MouseEvent(sp.getChildren().get(0), sp.getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                            0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                            true, true, true, true, true, true, null);
                    Event.fireEvent(sp.getChildren().get(0), mouseEvent);
                }
                else{
                    selectedStackPane = null;
                    imageView.setImage(null);
                    captionTextField.setText("");
                    dateText.setText("");
                    tagObservableList = FXCollections.observableArrayList();
                    tableView.setItems(tagObservableList);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no photo selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Slideshow operation
     * @param actionEvent action event of hitting slideshow button
     */
    public void slideshow(ActionEvent actionEvent) {
        try{
            Stage primaryStage = (Stage)slideshowButton.getScene().getWindow();
            String fxmlPath = "slideshowPage.fxml";
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/" + fxmlPath));
            AnchorPane albumListPage = (AnchorPane) loader.load();
            SlideshowPageController slideshowPageController = loader.getController();
            //slideshowPageController.start(primaryStage, user, album);
            slideshowPageController.start(primaryStage, user, album);

            Scene scene = new Scene(albumListPage);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Slideshow");
        }
        catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error When Loading The Album List Page");
            alert.setContentText("Cannot load the album list page!");
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Copy photo operation
     * @param actionEvent action event of hitting copy button
     */
    public void copyPhoto(ActionEvent actionEvent){
        if(selectedStackPane != null){
            Album selectedAlbum = albumChoiceBox.getSelectionModel().getSelectedItem();
            int index = imagePanes.indexOf(selectedStackPane);
            Photo photo = thumbnailPhotos.get(index);
            String newCaption = captionTextField.getText();
            String photoDetails = "Address: \t" + photo.getAddress() + "\nCaption: \t" + photo.getCaption() + "\nDate: \t" + photo.getDate();

            if(selectedAlbum.getName().equals(album.getName())){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot copy to current album!", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Illegal Manipulation!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            else{
                if(!selectedAlbum.getPhotoList().contains(photo)){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, photoDetails, ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Copy Photo");
                    alert.setHeaderText("Are you sure you want to copy this photo to album " + selectedAlbum.getName() + "?");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    Optional<ButtonType> option =  alert.showAndWait();
                    if(option.get() == ButtonType.YES){
                        user.copyPhoto(photo, selectedAlbum);
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "The target album already has this photo.", ButtonType.OK);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Illegal Manipulation!");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no photo selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Move photo operation
     * @param actionEvent action event of hitting move button
     */
    public void movePhoto(ActionEvent actionEvent){
        if(selectedStackPane != null){
            Album selectedAlbum = albumChoiceBox.getSelectionModel().getSelectedItem();
            int index = imagePanes.indexOf(selectedStackPane);
            Photo photo = thumbnailPhotos.get(index);
            String newCaption = captionTextField.getText();
            String photoDetails = "Address: \t" + photo.getAddress() + "\nCaption: \t" + photo.getCaption() + "\nDate: \t" + photo.getDate();

            if(selectedAlbum.getName().equals(album.getName())){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot move to current album!", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Illegal Manipulation!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            else{
                if(!selectedAlbum.getPhotoList().contains(photo)){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, photoDetails, ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Move Photo");
                    alert.setHeaderText("Are you sure you want to move this photo to album " + selectedAlbum.getName() + "?");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    Optional<ButtonType> option =  alert.showAndWait();
                    if(option.get() == ButtonType.YES){
                        user.movePhoto(photo, album, selectedAlbum);
                        imagePanes.remove(index);
                        thumbnailPhotos.setAll(album.getPhotoList());
                        tilePane.getChildren().setAll(imagePanes);
                        if(index < album.getPhotoList().size()){
                            StackPane sp = imagePanes.get(index);
                            MouseEvent mouseEvent = new MouseEvent(sp.getChildren().get(0), sp.getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null);
                            Event.fireEvent(sp.getChildren().get(0), mouseEvent);
                        }
                        else if(index - 1 >=0){
                            StackPane sp = imagePanes.get(index - 1);
                            MouseEvent mouseEvent = new MouseEvent(sp.getChildren().get(0), sp.getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null);
                            Event.fireEvent(sp.getChildren().get(0), mouseEvent);
                        }
                        else{
                            selectedStackPane = null;
                            imageView.setImage(null);
                            captionTextField.setText("");
                            dateText.setText("");
                            tagObservableList = FXCollections.observableArrayList();
                            tableView.setItems(tagObservableList);
                        }
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "The target album already has this photo.", ButtonType.OK);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Illegal Manipulation!");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no photo selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Caption operation
     * @param actionEvent action event of hitting caption button
     */
    public void caption(ActionEvent actionEvent){
        if(selectedStackPane != null){
            int index = imagePanes.indexOf(selectedStackPane);
            Photo photo = thumbnailPhotos.get(index);
            String newCaption = captionTextField.getText();
            String photoDetails = "Original Caption: \t" + photo.getCaption() + "\nNew Caption: \t" + newCaption;

            if(newCaption.length() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR, "New caption cannot be empty", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Empty caption!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
            else{
                if(!photo.getCaption().equals(newCaption)){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, photoDetails, ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Caption");
                    alert.setHeaderText("Are you sure you want to caption this photo?");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    Optional<ButtonType> option =  alert.showAndWait();
                    if(option.get() == ButtonType.YES){
                        user.captionPhoto(photo, newCaption);
                        StackPane sp = imagePanes.get(index);
                        MouseEvent mouseEvent = new MouseEvent(sp.getChildren().get(0), sp.getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null);
                        Event.fireEvent(sp.getChildren().get(0), mouseEvent);
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "You don't change anything.", ButtonType.OK);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Illegal Manipulation!");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no photo selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Add tag operation
     * @param actionEvent action event of hitting add tag button
     */
    public void addTag(ActionEvent actionEvent){
        if(selectedStackPane != null){
            int index = imagePanes.indexOf(selectedStackPane);
            Photo photo = thumbnailPhotos.get(index);
            Tag selectedTag = tagNameChoiceBox.getSelectionModel().getSelectedItem();
            String newTagName = newTagNameTextField.getText();
            String newTagType = newTagTypeChoiceBox.getSelectionModel().getSelectedItem();
            String tagValue = tagValueTextField.getText();

            if(selectedTag.getTagName().equals("other")){
                String tagDetails = "Tag Name: \t" + newTagName + "\nTag Type: \t" + newTagType + "\nTag Value: \t" + tagValue;
                if(newTagName.length() == 0 || tagValue.length() == 0){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "You cannot add an tag with empty tag name or empty tag value", ButtonType.OK);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Empty Tag Name or Tag Value!");
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
                else{
                    for(Tag existedTag : user.getCreatedTags()){
                        if(newTagName.equals(existedTag.getTagName())){
                            Alert alert = new Alert(Alert.AlertType.ERROR, "The tag name " + newTagName + " already exists. You need to select it to add it instead of creating it.", ButtonType.OK);
                            alert.setTitle("Error!");
                            alert.setHeaderText("Tag Existed!");
                            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                            alert.showAndWait();
                            return;
                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, tagDetails, ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Add Tag");
                    alert.setHeaderText("Are you sure you want to add this tag?");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get() == ButtonType.YES){
                        if(newTagType.equals("UniqueValueTag")){
                            UniqueValueTag uniqueValueTag = new UniqueValueTag(newTagName, tagValue);
                            user.getCreatedTags().add(uniqueValueTag);
                            user.addTagToPhoto(uniqueValueTag, photo);

                            int indexBeforeOther = existedTags.size() - 1;
                            existedTags.add(indexBeforeOther, uniqueValueTag);
                            tagNameChoiceBox.setItems(existedTags);

                            tagObservableList.add(uniqueValueTag);
                            tableView.setItems(tagObservableList);
                            if(tagObservableList.size() != 0){
                                int tagIndex = tableView.getItems().indexOf(uniqueValueTag);
                                tableView.getSelectionModel().select(tagIndex);
                            }
                        }
                        else if(newTagType.equals("MultipleValueTag")){
                            MultipleValueTag multipleValueTag = new MultipleValueTag(newTagName, tagValue);
                            user.getCreatedTags().add(multipleValueTag);
                            user.addTagToPhoto(multipleValueTag, photo);

                            int indexBeforeOther = existedTags.size() - 1;
                            existedTags.add(indexBeforeOther, multipleValueTag);
                            tagNameChoiceBox.setItems(existedTags);

                            tagObservableList.add(multipleValueTag);
                            tableView.setItems(tagObservableList);
                            if(tagObservableList.size() != 0){
                                int tagIndex = tableView.getItems().indexOf(multipleValueTag);
                                tableView.getSelectionModel().select(tagIndex);
                            }
                        }

                        StackPane sp = imagePanes.get(index);
                        MouseEvent mouseEvent = new MouseEvent(sp.getChildren().get(0), sp.getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null);
                        Event.fireEvent(sp.getChildren().get(0), mouseEvent);
                    }
                }
            }
            else{
                String tagDetails = "Tag Name: \t" + selectedTag.getTagName() + "\nTag Type: \t" + (selectedTag instanceof MultipleValueTag ? "MultipleValueTag" : "UniqueValueTag") + "\nTag Value: \t" + tagValue;
                if(tagValue.length() == 0){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "You cannot add an tag with empty tag value", ButtonType.OK);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Empty Tag Value!");
                    alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, tagDetails, ButtonType.YES, ButtonType.CANCEL);
                    alert.setTitle("Add Tag");
                    alert.setHeaderText("Are you sure you want to add this tag?");
                    alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                    Optional<ButtonType> option = alert.showAndWait();
                    if(option.get() == ButtonType.YES){
                        if(selectedTag instanceof UniqueValueTag){
                            UniqueValueTag uniqueValueTag = new UniqueValueTag(selectedTag.getTagName(), tagValue);
                            try{
                                user.addTagToPhoto(uniqueValueTag, photo);
                                tagObservableList.add(uniqueValueTag);
                                tableView.setItems(tagObservableList);
                                if(tagObservableList.size() != 0){
                                    int tagIndex = tableView.getItems().indexOf(uniqueValueTag);
                                    tableView.getSelectionModel().select(tagIndex);
                                }
                            }
                            catch(Photo.RepeatedTagException e) {
                                alert = new Alert(Alert.AlertType.ERROR, "You cannot add an tagName-tagValue combination that is already existed!", ButtonType.OK);
                                alert.setTitle("Error!");
                                alert.setHeaderText("Tag Already Exists!");
                                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                                alert.showAndWait();
                            }
                            catch(Photo.TagTypeUnmatchedException e) {
                                alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                alert.setTitle("Error!");
                                alert.setHeaderText("Tag Type Mismatches");
                                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                                alert.showAndWait();
                            }
                        }
                        else if(selectedTag instanceof MultipleValueTag){
                            MultipleValueTag multipleValueTag = new MultipleValueTag(selectedTag.getTagName(), tagValue);
                            try{
                                user.addTagToPhoto(multipleValueTag, photo);
                                tagObservableList.add(multipleValueTag);
                                tableView.setItems(tagObservableList);
                                if(tagObservableList.size() != 0) {
                                    int tagIndex = tableView.getItems().indexOf(multipleValueTag);
                                    tableView.getSelectionModel().select(tagIndex);
                                }
                            }
                            catch(Photo.RepeatedTagException e) {
                                alert = new Alert(Alert.AlertType.ERROR, "You cannot add an tagName-tagValue combination that is already existed!", ButtonType.OK);
                                alert.setTitle("Error!");
                                alert.setHeaderText("Tag Already Exists!");
                                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                                alert.showAndWait();
                            }
                            catch(Photo.TagTypeUnmatchedException e) {
                                alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                                alert.setTitle("Error!");
                                alert.setHeaderText("Tag Type Mismatches");
                                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                                alert.showAndWait();
                            }
                        }
                        StackPane sp = imagePanes.get(index);
                        MouseEvent mouseEvent = new MouseEvent(sp.getChildren().get(0), sp.getChildren().get(0), MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null);
                        Event.fireEvent(sp.getChildren().get(0), mouseEvent);
                    }
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no photo selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Delete tag operation
     * @param actionEvent action event of hitting delete tag button
     */
    public void deleteTag(ActionEvent actionEvent){
        if(selectedStackPane != null){
            int tableViewIndex = tableView.getSelectionModel().getSelectedIndex();
            if(tableViewIndex >= 0){
                Tag tag = tableView.getItems().get(tableViewIndex);
                String tagName = tag.getTagName();
                String tagValue;
                String tagType;
                if(tag instanceof MultipleValueTag){
                    MultipleValueTag multipleValueTag = (MultipleValueTag) tag;
                    tagValue = multipleValueTag.getTagValues().get(0);
                    tagType = "MultipleValueTag";
                }
                else{
                    UniqueValueTag uniqueValueTag = (UniqueValueTag) tag;
                    tagValue = uniqueValueTag.getTagValue();
                    tagType = "UniqueValueTag";
                }
                String tagDetails = "Tag Name: \t" + tagName + "\nTag Type: \t" + tagType + "\nTag Value: \t" + tagValue;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, tagDetails, ButtonType.YES, ButtonType.CANCEL);
                alert.setTitle("Add Tag");
                alert.setHeaderText("Are you sure you want to add this tag?");
                alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get() == ButtonType.YES) {
                    int photoIndex = imagePanes.indexOf(selectedStackPane);
                    Photo photo = thumbnailPhotos.get(photoIndex);
                    user.deletePhotoTag(tag, photo);

                    tagObservableList.remove(tag);
                    tableView.setItems(tagObservableList);
                    if(tableViewIndex < tagObservableList.size()){
                        tableView.getSelectionModel().clearSelection(tableViewIndex);
                        tableView.getSelectionModel().select(tableViewIndex);
                    }
                    else if(tableViewIndex - 1 >= 0){
                        tableView.getSelectionModel().select(tableViewIndex - 1);
                    }
                    else{
                        tableView.getSelectionModel().clearSelection(tableViewIndex);
                    }
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "There is no tag selected.", ButtonType.OK);
                alert.setTitle("Error!");
                alert.setHeaderText("Illegal Manipulation!");
                alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "There is no photo selected.", ButtonType.OK);
            alert.setTitle("Error!");
            alert.setHeaderText("Illegal Manipulation!");
            alert.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            alert.showAndWait();
        }
    }

    /**
     * Operation to go back to album list page
     * @param actionEvent action event of hitting go back button
     */
    public void goBack(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, null, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Go Back");
        alert.setHeaderText("Are you sure you want to go back to album list page?");
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
                AnchorPane loginPage = (AnchorPane) loader.load();
                AlbumListPageController albumListPageController = loader.getController();

                Scene scene = new Scene(loginPage);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Album List Page");
                albumListPageController.start(primaryStage, user);
            }
            catch(IOException e){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error When Going Back To Album List Page");
                alert.setContentText("Cannot load the album list page!");
                alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
                alert.showAndWait();
            }
        }
    }

    /**
     * Logout operation
     * @param actionEvent action event of hitting logout button
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
     * @param windowEvent window event of hitting exit button on window
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
