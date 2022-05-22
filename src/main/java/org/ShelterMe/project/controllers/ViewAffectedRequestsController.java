package org.ShelterMe.project.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.VolunteerItem;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.services.AffectedService;

public class ViewAffectedRequestsController {

    private Affected affected;

    @FXML
    private TableView affectedRequestsTable;
    @FXML
    private Text infoAffectedWelcome;
    @FXML
    private Text infoAffectedDetail;

    @FXML
    public void initialize() {
        TableColumn<VolunteerItem, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<VolunteerItem, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(200);
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<VolunteerItem, String> suppliesColumn = new TableColumn<>("Supplies");
        suppliesColumn.setMinWidth(200);
        suppliesColumn.setCellValueFactory(new PropertyValueFactory<>("supplies"));
        TableColumn<VolunteerItem, Float> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(200);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<AffectedItem, String> generalInformationColumn = new TableColumn<>("General Information");
        generalInformationColumn.setMinWidth(200);
        generalInformationColumn.setCellValueFactory(new PropertyValueFactory<>("generalInformation"));
        TableColumn<AffectedItem, String> healthConditionColumn = new TableColumn<>("Health Condition");
        healthConditionColumn.setMinWidth(200);
        healthConditionColumn.setCellValueFactory(new PropertyValueFactory<>("healthCondition"));
        affectedRequestsTable.getColumns().addAll(nameColumn, categoryColumn, suppliesColumn, quantityColumn, generalInformationColumn, healthConditionColumn);
    }

    public void setAffected(Affected affected) {
        this.affected = affected;
        affectedRequestsTable.setItems(FXCollections.observableList(AffectedService.databaseToList(affected.getUsername())));
        infoAffectedWelcome.setText("Info about " + affected.getFullName());
        infoAffectedDetail.setText(affected.getFullName() + "'s available requests");
    }

}