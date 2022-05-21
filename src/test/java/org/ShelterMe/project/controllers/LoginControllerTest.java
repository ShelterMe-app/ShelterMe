package org.ShelterMe.project.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.*;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assert;
import org.assertj.core.internal.bytebuddy.matcher.ElementMatchers;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.osgi.service.TestFx;


import static org.hamcrest.CoreMatchers.*;
import static org.testfx.matcher.control.TextMatchers.hasText;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
@Category(TestFx.class)
class LoginControllerTest {
    public static final String PASSWORD = "uNiTTest!12";
    public static final String COUNTRY = "Romania";
    public static final String PHONENUMBER = "758214675";
    public static final String CODE = "RO";
    private FXMLLoader loader;
    private FXMLLoader loaderDest1;
    public static final Volunteer logedInVolunteer = new Volunteer("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER);

    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-102";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        AffectedService.initAffectedItemsDatabase();
        CommunicationService.initCommunicationDatabase();
        UserService.addUser("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER, CODE);
        UserService.addUser("seb gab", PASSWORD, "Affected", "Seb Gab", COUNTRY, PHONENUMBER, CODE);
    }
    @Start
    public void start (@NotNull Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        loaderDest1 = new FXMLLoader(getClass().getClassLoader().getResource("volunteerPage.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
        stage.toFront();
    }

    @AfterAll
    public static void afterAll(){
        UserService.closeDatabase();
        VolunteerService.closeDatabase();
        AffectedService.closeDatabase();
        CommunicationService.closeDatabase();
    }

    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleLoginAction(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write("SebiG");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        //FxAssert.verifyThat(logedInVolunteer.getCurrentFailedAttempts(), is(0));
    }

    @Test
    void handleRegisterMenu() {
    }
}