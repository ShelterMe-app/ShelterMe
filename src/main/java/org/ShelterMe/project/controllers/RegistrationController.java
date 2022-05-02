package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.services.UserService;

import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.stage.Stage;
import javafx.application.Platform;

public class RegistrationController{

    @FXML
    private Text registrationMessage;
    @FXML
    private TextField fullName;
    @FXML
    private ChoiceBox country;
    @FXML
    private TextField phoneNumber;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox role;
    @FXML
    private Hyperlink signInPressed;

    private static ArrayList<String> countries = new ArrayList<>();
    private static ArrayList<String> countryShort = new ArrayList<>();

    private static void countryParser() {
        JSONParser parser = new JSONParser();

        try {
            String filePath = new File("").getAbsolutePath();
            JSONArray a = (JSONArray) parser.parse(new FileReader(filePath + "/src/main/resources/countries.json"));

            for (Object o : a)
            {
                JSONObject countryObject = (JSONObject) o;

                countries.add((String) countryObject.get("name"));
                countryShort.add((String) countryObject.get("code"));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        countryParser();
        country.getItems().addAll(countries);
        role.getItems().addAll("Affected", "Volunteer");
    }

    @FXML
    public void handleRegisterAction(javafx.event.ActionEvent event) {
        try {
            String roleAux;
            if (role.getValue() == null)
                roleAux = "";
            else roleAux = (String) role.getValue();
            String countryAux;
            if (country.getValue() == null)
                countryAux = "";
            else countryAux = (String) country.getValue();
            String countryShortAux;
            try {
                countryShortAux = countryShort.get(countries.indexOf(countryAux));
            } catch(IndexOutOfBoundsException e) {
                countryShortAux = "";
            }
            UserService.addUser(usernameField.getText(), passwordField.getText(), roleAux, fullName.getText(), countryAux, phoneNumber.getText(), countryShortAux);
            registrationMessage.setText("Account created successfully! You will be redirected to the login page in 5 seconds.");
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handleSignInMenu(event);
                            } catch (IOException e) {
                                registrationMessage.setText(e.getMessage());
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    registrationMessage.setText(e.getMessage());
                }
            }).start();

        } catch (UsernameAlreadyExistsException | EmptyFieldException | PhoneNumberFormatException | WeakPasswordException | FullNameFormatException e) {
            registrationMessage.setText(e.getMessage());
        }
    }

    @FXML
    public void handleSignInMenu(javafx.event.ActionEvent event) throws IOException {
        Stage stage = (Stage) signInPressed.getScene().getWindow();
        GotoLoginController.goToLogin(stage, event);
    }
}
