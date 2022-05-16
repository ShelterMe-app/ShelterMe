package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
    private TextArea generalInformation;
    @FXML
    private TextArea healthCondition;
    @FXML
    private Hyperlink offerImage;

    private Image selectedImage;
    private String base64Image;

    private static ArrayList<String> categoryItems = new ArrayList<>();

    public void setLoggedInVolunteer(Volunteer signedInVolunteer) {
        this.loggedInVolunteer = signedInVolunteer;
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
            base64Image = imageToBase64(selectedFile.getPath());
            JOptionPane.showMessageDialog(null, "File has been selected: " + selectedFile.getName(), "Success", 1);
            offerImage.setText(selectedFile.getName());

        }
    }

    public void handleAddOfferAction(javafx.event.ActionEvent event) throws IOException {
        try {
            String categoryAux;
            if (offerCategory.getValue() == null)
                categoryAux = "";
            else categoryAux = (String) offerCategory.getValue();
            loggedInVolunteer.setOffersNo(loggedInVolunteer.getOffersNo() + 1);
            VolunteerService.addItem(loggedInVolunteer.getUsername(), offerName.getText(), categoryAux, offerSupplies.getText(), offerQuantity.getText(), base64Image);
            JOptionPane.showMessageDialog(null, "Offer created successfully", "Success", 1);
        } catch (EmptyFieldException e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + "", "Error", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Quantity must be a number!", "Error",JOptionPane.WARNING_MESSAGE);
        }
    }

    private String imageToBase64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64
                .getEncoder()
                .encodeToString(fileContent);
        return encodedString;
    }

    private Image Base64ToString(String base64) throws IOException {
        byte[] decodedBytes = Base64
                .getDecoder()
                .decode(base64);
        InputStream stream = new ByteArrayInputStream(decodedBytes);
        Image recoveredImage = new Image(stream);
        return recoveredImage;
    }
}