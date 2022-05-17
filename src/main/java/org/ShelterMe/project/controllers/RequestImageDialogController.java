package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RequestImageDialogController {

    @FXML
    private ImageView requestImage;

    public void setRequestImage(Image image) {
        requestImage.setImage(image);

    }
}
