package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import org.ShelterMe.project.services.AffectedService;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.ShelterMe.project.model.Affected;

import java.util.Base64;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;

public class RequestMenuController {

    private Affected loggedInAffected;

    @FXML
    private TextField requestName;
    @FXML
    private ComboBox requestCategory;
    @FXML
    private TextArea requestSupplies;
    @FXML
    private TextField requestQuantity;
    @FXML
    private TextArea generalInformation;
    @FXML
    private TextArea healthCondition;
    @FXML
    private Hyperlink requestImage;

    private Image selectedImage;
    private String base64Image;

    private static ArrayList<String> categoryItems = new ArrayList<>();

    public void setLoggedInAffected(Affected signedInAffected) {
        this.loggedInAffected = signedInAffected;
    }

    private void itemParser() {
        JSONParser parser = new JSONParser();

        try {
            String filePath = new File("").getAbsolutePath();
            JSONArray a = (JSONArray) parser.parse(new FileReader(filePath + "/src/main/resources/requestsCategories.json"));

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
        requestCategory.getItems().addAll(categoryItems);
    }

    public void handleAddImageAction(javafx.event.ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image..");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(requestImage.getScene().getWindow());
        if (selectedFile != null) {
            selectedImage = new Image(selectedFile.getPath());
            base64Image = imageToBase64(selectedFile.getPath());
            JOptionPane.showMessageDialog(null, "File has been selected: " + selectedFile.getName(), "Success", 1);
            requestImage.setText(selectedFile.getName());

        }
    }

    public void handleAddRequestAction(javafx.event.ActionEvent event) throws IOException {

        AffectedService.addItem(loggedInAffected.getUsername(), requestName.getText(), (String) requestCategory.getValue(), requestSupplies.getText(), Float.valueOf(requestQuantity.getText()), generalInformation.getText(), healthCondition.getText(), base64Image);
        JOptionPane.showMessageDialog(null, "Request created succesfully", "Success", 1);
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
