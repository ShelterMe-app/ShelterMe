package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.Communication;
import org.ShelterMe.project.model.User;
import org.ShelterMe.project.model.VolunteerItem;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.services.VolunteerService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HistoryItemViewController {
    private User loggedInUser;
    @FXML
    private Text historyItemViewWelcome;
    @FXML
    private Text itemName;
    @FXML
    private Text itemCategory;
    @FXML
    private Text itemSupplies;
    @FXML
    private Text itemQuantity;
    @FXML
    private Text historyGeneralInformationMessage;
    @FXML
    private Text itemGeneralInformation;
    @FXML
    private Text historyHealthConditionMessage;
    @FXML
    private Text itemHealthCondition;

    private TableView historyTable;

    private Image historyImage;

    private String typeString;

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void setHistoryImage(Image historyImage) {
        this.historyImage = historyImage;
    }

    public void setHistoryTable(TableView historyTable) {
        this.historyTable = historyTable;
        Object o = historyTable.getSelectionModel().getSelectedItem();
        Communication com = (Communication)o;
        Object item = (com.isType() == 'r' ? AffectedService.getItemWithId(com.getId()) : VolunteerService.getItemWithId(com.getId()));
        typeString = com.isType() == 'r' ? "request" : "offer";
        if (item instanceof AffectedItem) {
            AffectedItem affectedItem = (AffectedItem)item;
            if (com.getSourceUsername().equals(loggedInUser.getUsername()))
                historyItemViewWelcome.setText("Your request");
            else
                historyItemViewWelcome.setText(com.getSourceUsername() + "'s request");
            itemName.setText(affectedItem.getName());
            itemCategory.setText(affectedItem.getCategory());
            itemSupplies.setText(affectedItem.getSupplies());
            itemQuantity.setText(Float.toString(affectedItem.getQuantity()));
            itemGeneralInformation.setText(affectedItem.getGeneralInformation());
            if (affectedItem.getHealthCondition().length() > 0)
                itemHealthCondition.setText(affectedItem.getHealthCondition());
            else
                itemHealthCondition.setText("None");

        } else {
            VolunteerItem volunteerItem = (VolunteerItem) item;
            if (com.getSourceUsername().equals(loggedInUser.getUsername()))
                historyItemViewWelcome.setText("Your offer");
            else
                historyItemViewWelcome.setText(com.getSourceUsername() + "'s offer");
            itemName.setText(volunteerItem.getName());
            itemCategory.setText(volunteerItem.getCategory());
            itemSupplies.setText(volunteerItem.getSupplies());
            itemQuantity.setText(Float.toString(volunteerItem.getQuantity()));
            historyGeneralInformationMessage.setVisible(false);
            historyHealthConditionMessage.setVisible(false);
        }
    }


    public void handleItemViewImage() throws IOException {
            if (historyImage == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failed to view image");
                alert.setHeaderText("This " + typeString + " has no image");
                alert.showAndWait();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("requestImageDialog.fxml"));
                Parent history = loader.load();
                RequestImageDialogController newController = loader.getController();
                newController.setRequestImage(historyImage);
                Scene scene = new Scene(history);
                Stage newStage = new Stage();
                newStage.setScene(scene);
                newStage.initModality(Modality.WINDOW_MODAL);
                newStage.initOwner(historyTable.getScene().getWindow());
                newStage.setTitle("ShelterMe - View Image");
                newStage.getIcons().add(new Image("file:docs/Logo.png"));
                newStage.show();
                newStage.setResizable(false);
            }
    }
}
