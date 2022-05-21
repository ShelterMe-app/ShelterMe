package org.ShelterMe.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;
import javafx.css.PseudoClass;
import javafx.stage.Stage;
import org.ShelterMe.project.services.*;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        initDirectory();
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        AffectedService.initAffectedItemsDatabase();
        CommunicationService.initCommunicationDatabase();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        primaryStage.setTitle("ShelterMe");
        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.getIcons().add(new Image("file:docs/Logo.png"));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    private void initDirectory() {
        Path applicationHomePath = FileSystemService.APPLICATION_HOME_PATH;
        if (!Files.exists(applicationHomePath))
            applicationHomePath.toFile().mkdirs();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
