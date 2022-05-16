package org.ShelterMe.project.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.model.AffectedItem;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.ShelterMe.project.model.Affected;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AffectedPageController{
    private Affected loggedInAffected;

    @FXML
    private Label signedInAsLabel;
    @FXML
    private JFXButton signOutButton;
    @FXML
    private JFXButton requestsButton;
    @FXML
    private  JFXButton homeButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox requestsTab;
    @FXML
    private VBox homeTab;

    @FXML
    private TableView requestsTable;

    private Image requestImage;

    public void setSignedInAs(Affected loggedInAffected) {
        this.loggedInAffected = loggedInAffected;
        signedInAsLabel.setText("Welcome, " + loggedInAffected.getFullName() + "!");
        requestsTable.setItems(getRequests(loggedInAffected.getUsername()));
    }

    public void handleSignOut(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) signOutButton.getScene().getWindow();
        GotoLoginController.goToLogin(stage, event);
    }

    @FXML
    public void initialize() {
        TableColumn<AffectedItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<AffectedItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(200);
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<AffectedItem, String> suppliesColumn = new TableColumn<>("Supplies");
        suppliesColumn.setMinWidth(200);
        suppliesColumn.setCellValueFactory(new PropertyValueFactory<>("supplies"));
        TableColumn<AffectedItem, Float> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(200);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<AffectedItem, String> generalInformationColumn = new TableColumn<>("General Information");
        generalInformationColumn.setMinWidth(200);
        generalInformationColumn.setCellValueFactory(new PropertyValueFactory<>("generalInformation"));
        TableColumn<AffectedItem, String> healthConditionColumn = new TableColumn<>("Health Condition");
        healthConditionColumn.setMinWidth(200);
        healthConditionColumn.setCellValueFactory(new PropertyValueFactory<>("healthCondition"));
        requestsTable.getColumns().addAll(nameColumn, categoryColumn, suppliesColumn, quantityColumn, generalInformationColumn, healthConditionColumn);
    }



    public void handleHomePage() {
        homeTab.setVisible(true);
        homeTab.setManaged(true);
        requestsTab.setVisible(false);
        requestsTab.setVisible(false);
    }
    public void handleRequestsPage() {
        homeTab.setVisible(false);
        homeTab.setManaged(false);
        requestsTab.setVisible(true);
        requestsTab.setManaged(true);

    }

    public void handleAddRequest(javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addRequestForm.fxml"));
        Parent addRequest = loader.load();
        RequestMenuController newController = loader.getController();
        newController.setLoggedInAffected(loggedInAffected);
        newController.setRequestsTable(requestsTable);
        Scene scene = new Scene(addRequest);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(requestsTab.getScene().getWindow());
        newStage.setTitle("ShelterMe - Add a Request");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);

    }

    public static ObservableList<AffectedItem> getRequests(String username) {

        return FXCollections.observableList(AffectedService.databaseToList(username));
    }

    public void handleTableClick(MouseEvent event) throws IOException  {
        if (requestsTable.getSelectionModel().getSelectedItem() != null)
            requestImage = AffectedService.base64ToImage(((AffectedItem)requestsTable.getSelectionModel().getSelectedItem()).getImageBase64());
    }

    public void handleRequestImage(javafx.event.ActionEvent event) throws IOException {
        if (requestImage == null) {
            JOptionPane.showMessageDialog(null, "This request has no image", "Failed to open image", 1);
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("requestImageDialog.fxml"));
        Parent imageDialog = loader.load();
        RequestImageDialogController controller = loader.getController();
        controller.setRequestImage(requestImage);
        Scene scene = new Scene(imageDialog);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.setTitle("ShelterMe - Request image");
        newStage.getIcons().add(new Image("file:docs/Logo.png"));
        newStage.show();
        newStage.setResizable(false);

    }

    public void handleEditRequest(javafx.event.ActionEvent event) throws IOException {
        if (requestsTable.getSelectionModel().getSelectedItem() != null) {
            AffectedItem request = (AffectedItem)requestsTable.getSelectionModel().getSelectedItem();
            int requestId = request.getId();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("addRequestForm.fxml"));
            Parent editRequest = loader.load();
            RequestMenuController newController = loader.getController();
            newController.setLoggedInAffected(loggedInAffected);
            newController.setRequestsTable(requestsTable);
            newController.setRequestId(requestId);
            newController.setRequestMenuWelcomeText("Edit Request");
            newController.setAddRequestButtonText("Edit request");
            newController.setRequestNamePlaceholder(request.getName());
            newController.setRequestCategoryPlaceholder(request.getCategory());
            newController.setRequestSuppliesPlaceholder(request.getSupplies());
            newController.setRequestQuantityPlaceholder(Float.toString(request.getQuantity()));
            newController.setGeneralInformationPlaceholder(request.getGeneralInformation());
            newController.setHealthConditionPlaceholder(request.getHealthCondition());
            newController.setRequestImagePlaceholder("Update image");
            Scene scene = new Scene(editRequest);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(requestsTab.getScene().getWindow());
            newStage.setTitle("ShelterMe - Edit a Request");
            newStage.getIcons().add(new Image("file:docs/Logo.png"));
            newStage.show();
            newStage.setResizable(false);
        } else {
            JOptionPane.showMessageDialog(null, "Select a request in order to edit", "Failed to edit request", 1);
        }
    }


}
