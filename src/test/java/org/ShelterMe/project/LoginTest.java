package org.ShelterMe.project;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
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

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
@Category(TestFx.class)
class LoginTest {
    public static final String PASSWORD = "uNiTTest!12";
    public static final String COUNTRY = "Romania";
    public static final String PHONENUMBER = "758214675";
    public static final String CODE = "RO";
    private FXMLLoader loader;
    private FXMLLoader loaderDest1;
    private Scene scene;
    public static final Volunteer logedInVolunteer = new Volunteer("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER);

    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-5";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        AffectedService.initAffectedItemsDatabase();
        CommunicationService.initCommunicationDatabase();
    }

    @BeforeEach
    void setUp() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER, CODE);
        UserService.addUser("seb gab", PASSWORD, "Affected", "Seb Gab", COUNTRY, PHONENUMBER, CODE);
    }

    @AfterEach
    void tearDown() {
        UserService.resetDatabase();
        AffectedService.resetDatabase();
        VolunteerService.resetDatabase();
        CommunicationService.resetDatabase();
    }

    @Start
    public void start (@NotNull Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        loaderDest1 = new FXMLLoader(getClass().getClassLoader().getResource("volunteerPage.fxml"));
        stage.setScene(scene = new Scene(loader.load()));
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

    @Test
    void testHandleLoginActionSuccess(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write("SebiG");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        FxAssert.verifyThat(logedInVolunteer.getCurrentFailedAttempts(), is(0));
        robot.clickOn("#loginButton");
        robot.clickOn("Sign Out");
        Button bExit = robot.lookup("#loginButton").queryButton();
        Stage stageExit = (Stage) bExit.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageExit.close();
                }
        );
    }

    @Test
    void testHandleLoginActionWrong(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write("SebiG");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD + "1");
        robot.clickOn("#loginButton");
        Text loginMessage = (Text) scene.lookup("#loginMessage");
        FxAssert.verifyThat(loginMessage.getText(), is("Incorrect password!"));
    }

    @Test
    void testHandleLoginActionEmptyField(@NotNull FxRobot robot) {
        Text loginMessage = (Text) scene.lookup("#loginMessage");
        robot.clickOn("#loginButton");
        FxAssert.verifyThat(loginMessage.getText(), is("Error: One of the fields is empty. Enter info for field: username"));
        robot.clickOn("#usernameField");
        robot.write("Sebi");
        robot.clickOn("#loginButton");
        FxAssert.verifyThat(loginMessage.getText(), is("Error: One of the fields is empty. Enter info for field: password"));
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        FxAssert.verifyThat(loginMessage.getText(), is("Username Sebi does not exist! If you don't have an account, please sign up."));
    }

    @Test
    void testHandleLoginActionLockout(@NotNull FxRobot robot) {
        robot.clickOn("#usernameField");
        robot.write("SebiG");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD + "1");
        robot.clickOn("#loginButton");
        Text loginMessage = (Text) scene.lookup("#loginMessage");
        FxAssert.verifyThat(loginMessage.getText(), is("Incorrect password!"));
        robot.clickOn("#loginButton");
        FxAssert.verifyThat(loginMessage.getText(), is("Incorrect password!"));
        robot.clickOn("#loginButton");
        FxAssert.verifyThat(loginMessage.getText(), is("Incorrect password!"));
        robot.clickOn("#loginButton");
        FxAssert.verifyThat(loginMessage.getText(), is("Incorrect password!"));
        robot.clickOn("#loginButton");
        FxAssert.verifyThat(loginMessage.getText(), containsString("You have exhausted all attempts. You can try again after:"));
    }

    @Test
    void testHandleRegisterMenu(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#registerPressed");
        Thread.sleep(1500);
        Button b = robot.lookup("#registerButton").queryButton();
        FxAssert.verifyThat(b.getText(),is("Register"));
        Stage stageExit = (Stage) b.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageExit.close();
                }
        );
    }
}