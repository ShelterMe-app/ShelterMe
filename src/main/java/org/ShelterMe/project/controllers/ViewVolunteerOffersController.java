package org.ShelterMe.project.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.UserService;
import org.ShelterMe.project.services.VolunteerService;

public class ViewVolunteerOffersController {

    private Volunteer volunteer;

    @FXML
    private TableView volunteerOffersTable;
    @FXML
    private Text infoVolunteerWelcome;
    @FXML
    private Text infoVolunteerDetail;

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
        volunteerOffersTable.getColumns().addAll(nameColumn, categoryColumn, suppliesColumn, quantityColumn);
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
        volunteerOffersTable.setItems(FXCollections.observableList(VolunteerService.databaseToList(volunteer.getUsername())));
        infoVolunteerWelcome.setText("Info about " + volunteer.getFullName());
        infoVolunteerDetail.setText(volunteer.getFullName() + "'s available offers");
    }

}
