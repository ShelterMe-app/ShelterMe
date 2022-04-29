package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
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

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.application.Platform;

public class RegistrationController {

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
            UserService.addUser(usernameField.getText(), passwordField.getText(), (String) role.getValue(), fullName.getText(), (String) country.getValue(), phoneNumber.getText(), countryShort.get(countries.indexOf((String) country.getValue())));
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
        stage.close();

        Parent login = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        Scene scene = new Scene(login);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }
}
