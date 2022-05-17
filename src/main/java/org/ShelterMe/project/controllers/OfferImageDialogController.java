package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OfferImageDialogController {

    @FXML
    private ImageView offerImage;

    public void setOfferImage(Image image) {
        offerImage.setImage(image);

    }
}