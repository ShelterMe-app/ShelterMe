package org.ShelterMe.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.ShelterMe.project.exceptions.UsernameAlreadyExistsException;
import org.ShelterMe.project.services.UserService;

public class LoginController {

    @FXML
    private Text loginMessage;
    @FXML
    private PasswordField passwordField;
}
