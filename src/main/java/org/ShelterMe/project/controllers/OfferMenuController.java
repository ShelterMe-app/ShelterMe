package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.ShelterMe.project.exceptions.EmptyFieldException;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.VolunteerService;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import java.util.Base64;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;

public class OfferMenuController {

    private Volunteer loggedInVolunteer;
    @FXML
    private TextField offerName;
    @FXML
    private ComboBox offerCategory;
    @FXML
    private TextArea offerSupplies;
    @FXML
    private TextField offerQuantity;
    @FXML
    private Hyperlink offerImage;

    @FXML
    private Button addOfferButton;
    @FXML
    private Text offerMenuWelcome;

    private Image selectedImage;
    private String base64Image;

    private TableView offerTable;

    private static ArrayList<String> categoryItems = new ArrayList<>();

    private int offerId;

    public void setLoggedInVolunteer(Volunteer signedInVolunteer) {
        this.loggedInVolunteer = signedInVolunteer;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public void setOffersTable(TableView offersTable) {
        this.offerTable = offersTable;
    }

    private void itemParser() {
        JSONParser parser = new JSONParser();

        try {
            String filePath = new File("").getAbsolutePath();
            JSONArray a = (JSONArray) parser.parse(new FileReader(filePath + "/src/main/resources/offersCategories.json"));

            for (Object o : a) {
                JSONObject countryObject = (JSONObject) o;

                categoryItems.add((String) countryObject.get("description"));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        itemParser();
        offerCategory.getItems().addAll(categoryItems);
    }

    public void handleAddImageAction(javafx.event.ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image..");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(offerImage.getScene().getWindow());
        if (selectedFile != null) {
            selectedImage = new Image(selectedFile.getPath());
            base64Image = VolunteerService.imageToBase64(selectedFile.getPath());
            JOptionPane.showMessageDialog(null, "File has been selected: " + selectedFile.getName(), "Success", 1);
            offerImage.setText(selectedFile.getName());

        }
    }

    public void handleAddOfferAction(javafx.event.ActionEvent event) throws IOException {
        try {
            /*String categoryAux;
            if (offerCategory.getValue() == null)
                categoryAux = "";
            else categoryAux = (String) offerCategory.getValue();
            loggedInVolunteer.setOffersNo(loggedInVolunteer.getOffersNo() + 1);
            VolunteerService.addItem(loggedInVolunteer.getUsername(), offerName.getText(), categoryAux, offerSupplies.getText(), offerQuantity.getText(), base64Image);
            JOptionPane.showMessageDialog(null, "Offer created successfully", "Success", 1);*/
            if (addOfferButton.getText().equals("Edit request")) {
                VolunteerService.editItem(offerId, offerName.getText(), (String) offerCategory.getValue(),offerSupplies.getText(), Float.valueOf(offerQuantity.getText()), base64Image);
                JOptionPane.showMessageDialog(null, "Offer updated succesfully", "Success", 1);
                if (offerTable != null)
                    offerTable.setItems(VolunteerPageController.getOffers(loggedInVolunteer.getUsername()));
            } else {
                VolunteerService.addItem(loggedInVolunteer.getUsername(), offerName.getText(), (String) offerCategory.getValue(), offerSupplies.getText(), offerQuantity.getText(), base64Image);
                JOptionPane.showMessageDialog(null, "Request created succesfully", "Success", 1);
                if (offerTable != null)
                    offerTable.setItems(VolunteerPageController.getOffers(loggedInVolunteer.getUsername()));
            }
        } catch (EmptyFieldException e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + "", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Quantity must be a number!", "Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void setOfferMenuWelcomeText(String text) {
        offerMenuWelcome.setText(text);
    }

    public void setAddOfferButtonText(String text) {
        addOfferButton.setText(text);
    }

    public void setOfferNamePlaceholder(String text) {
        offerName.setText(text);
    }

    public void setOfferCategoryPlaceholder(String text) {
        offerCategory.getSelectionModel().select(text);
    }

    public void setOfferSuppliesPlaceholder(String text) {
        offerSupplies.setText(text);
    }

    public void setOfferQuantityPlaceholder(String text) {
        offerQuantity.setText(text);
    }

    public void setOfferImagePlaceholder(String text) {
        offerImage.setText(text);
    }

}