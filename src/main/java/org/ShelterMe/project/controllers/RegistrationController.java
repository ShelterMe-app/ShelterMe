package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.services.UserService;

import java.util.ArrayList;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RegistrationController {

    @FXML
    private Text registrationMessage;
    @FXML
    private TextField fullName;
    @FXML
    private ChoiceBox address;
    @FXML
    private TextField phoneNumber;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private ChoiceBox role;

    private static ArrayList<String> countries = new ArrayList<>();
    private static ArrayList<String> countryShort = new ArrayList<>();

    private static void countryParser() {
        JSONParser parser = new JSONParser();

        try {

            JSONArray a = (JSONArray) parser.parse(new FileReader("C:\\Users\\drago\\Desktop\\ShelterMe\\src\\main\\java\\org\\ShelterMe\\project\\controllers\\countries.json"));

            for (Object o : a)
            {
                JSONObject country = (JSONObject) o;

                countries.add((String) country.get("name"));
                countryShort.add((String) country.get("code"));
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        countryParser();
        address.getItems().addAll(countries);
        role.getItems().addAll("Affected", "Volunteer");
    }

    @FXML
    public void handleRegisterAction() {
        try {
            UserService.addUser(usernameField.getText(), passwordField.getText(), (String) role.getValue(), fullName.getText(), (String) address.getValue(), phoneNumber.getText(), countryShort.get(countries.indexOf((String) address.getValue())));
            registrationMessage.setText("Account created successfully!");
        } catch (UsernameAlreadyExistsException | EmptyFieldException | PhoneNumberFormatException | WeakPasswordException | FullNameFormatException e) {
            registrationMessage.setText(e.getMessage());
        }
    }
}
