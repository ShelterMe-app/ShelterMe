package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.ShelterMe.project.exceptions.QuantityFormatException;
import org.ShelterMe.project.services.AffectedService;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.ShelterMe.project.exceptions.EmptyFieldException;

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
    @FXML
    private Hyperlink removeCurrentImage;
    @FXML
    private Button addRequestButton;
    @FXML
    private Text requestMenuWelcome;

    private Image selectedImage;
    private String base64Image;

    private TableView requestsTable;

    private static ArrayList<String> categoryItems = new ArrayList<>();

    private int requestId;

    public void setLoggedInAffected(Affected signedInAffected) {
        this.loggedInAffected = signedInAffected;
    }

    public void setRequestsTable(TableView requestsTable) {
        this.requestsTable = requestsTable;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
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
            base64Image = AffectedService.imageToBase64(selectedFile.getPath());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("File has been selected: " + selectedFile.getName());
            alert.showAndWait();
            requestImage.setText(selectedFile.getName());

        }
    }

    public void handleRemoveImageAction(javafx.event.ActionEvent event) throws IOException {
        base64Image = null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Image has been removed");
        alert.showAndWait();
        setRemoveCurrentImageStatus(false);
    }

    public void handleAddRequestAction(javafx.event.ActionEvent event) throws IOException {
        if (addRequestButton.getText().equals("Edit request")) {
           try {
               if (Float.valueOf(requestQuantity.getText()) <= 0)
                   throw new QuantityFormatException();
               AffectedService.editItem(requestId, requestName.getText(), (String) requestCategory.getValue(), requestSupplies.getText(), Float.valueOf(requestQuantity.getText()), generalInformation.getText(), healthCondition.getText(), base64Image);
               Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
               alert.setTitle("Success");
               alert.setHeaderText("Request updated succesfully");
               alert.showAndWait();
               if (requestsTable != null)
                   requestsTable.setItems(AffectedPageController.getRequests(loggedInAffected.getUsername()));
           }catch(EmptyFieldException e) {
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Error");
               alert.setHeaderText(e.getMessage());
               alert.showAndWait();
           } catch(NumberFormatException e) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText("Quantity must be a number!");
               alert.showAndWait();
           } catch(QuantityFormatException e) {
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText(e.getMessage());
               alert.showAndWait();
           }
        } else { try {
            if (Float.valueOf(requestQuantity.getText()) <= 0)
                throw new QuantityFormatException();
            AffectedService.addItem(loggedInAffected.getUsername(), requestName.getText(), (String) requestCategory.getValue(), requestSupplies.getText(), Float.valueOf(requestQuantity.getText()), generalInformation.getText(), healthCondition.getText(), base64Image);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Request created succesfully");
            alert.showAndWait();
            if (requestsTable != null)
                requestsTable.setItems(AffectedPageController.getRequests(loggedInAffected.getUsername()));
        }catch(EmptyFieldException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        } catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Quantity must be a number!");
            alert.showAndWait();
        } catch(QuantityFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }

        }

    }

    public void setRequestMenuWelcomeText(String text) {
        requestMenuWelcome.setText(text);
    }

    public void setAddRequestButtonText(String text) {
        addRequestButton.setText(text);
    }

    public void setRequestNamePlaceholder(String text) {
        requestName.setText(text);
    }

    public void setRequestCategoryPlaceholder(String text) {
        requestCategory.getSelectionModel().select(text);
    }

    public void setRequestSuppliesPlaceholder(String text) {
        requestSupplies.setText(text);
    }

    public void setRequestQuantityPlaceholder(String text) {
        requestQuantity.setText(text);
    }

    public void setGeneralInformationPlaceholder(String text) {
        generalInformation.setText(text);
    }

    public void setHealthConditionPlaceholder(String text) {
        healthCondition.setText(text);
    }

    public void setRequestImagePlaceholder(String text) {
        requestImage.setText(text);
    }

    public void setRemoveCurrentImageStatus(boolean value) {
        removeCurrentImage.setVisible(value);
    }
}
