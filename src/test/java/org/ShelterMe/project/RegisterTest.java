package org.ShelterMe.project;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.services.*;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.osgi.service.TestFx;

import java.awt.*;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
@Category(TestFx.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegisterTest {


    public static final String PASSWORD = "uNiTTest!12";
    public static final String COUNTRY = "Romania";
    public static final String PHONENUMBER = "758214675";
    public static final String CODE = "RO";
    private FXMLLoader loader;
    private Scene scene;
    public static final Volunteer logedInVolunteer = new Volunteer("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER);

    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-6";
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
        UserService.addUser("unit_test1", PASSWORD, "Affected", "Seb Gab", COUNTRY, PHONENUMBER, CODE);
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
        loader = new FXMLLoader(getClass().getClassLoader().getResource("register.fxml"));
        stage.setScene(scene = new Scene(loader.load()));
        stage.centerOnScreen();
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
    @Order(1)
    void testRegisterSuccessVolunteer(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#fullName");
        robot.write("Test Account");
        robot.clickOn("#country");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#phoneNumber");
        robot.write("1152461869");
        robot.clickOn("#usernameField");
        robot.write("unit_test");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#registerButton");
        Text registerMessage = (Text) scene.lookup("#registrationMessage");
        FxAssert.verifyThat(registerMessage.getText(),is("Account created successfully! You will be redirected to the login page in 5 seconds."));
        FxAssert.verifyThat(UserService.getUser("unit_test").getRole(), is("Volunteer"));
        Button bExit = robot.lookup("#registerButton").queryButton();
        Stage stageExit = (Stage) bExit.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageExit.close();
                }
        );
    }

    @Test
    @Order(2)
    void testRegisterSuccessAffected(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#fullName");
        robot.write("Test Account");
        robot.clickOn("#country");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#phoneNumber");
        robot.write("1152461869");
        robot.clickOn("#usernameField");
        robot.write("unit_test");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#registerButton");
        Text registerMessage = (Text) scene.lookup("#registrationMessage");
        FxAssert.verifyThat(registerMessage.getText(),is("Account created successfully! You will be redirected to the login page in 5 seconds."));
        FxAssert.verifyThat(UserService.getUser("unit_test").getRole(), is("Affected"));
        Button bExit = robot.lookup("#registerButton").queryButton();
        Stage stageExit = (Stage) bExit.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageExit.close();
                }
        );
    }


    @Test
    @Order(3)
    void testRegisterSuccessLogin(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#fullName");
        robot.write("Test Account");
        robot.clickOn("#country");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#phoneNumber");
        robot.write("1152461869");
        robot.clickOn("#usernameField");
        robot.write("unit_test");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#role");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#registerButton");
        Text registerMessage = (Text) scene.lookup("#registrationMessage");
        FxAssert.verifyThat(registerMessage.getText(),is("Account created successfully! You will be redirected to the login page in 5 seconds."));
        Thread.sleep(5500);
        robot.clickOn("#usernameField");
        robot.write("unit_test");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        FxAssert.verifyThat(UserService.getUser("unit_test").getCurrentFailedAttempts(), is(0));
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
    @Order(4)
    void testRegisterFail(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#fullName");
        robot.write("Test");
        robot.clickOn("#phoneNumber");
        robot.write("115246186");
        robot.clickOn("#usernameField");
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write("abcdefg");
        robot.clickOn("#registerButton");
        Text registerMessage = (Text) scene.lookup("#registrationMessage");
        FxAssert.verifyThat(registerMessage.getText(),is("Error: One of the fields is empty. Enter info for field: country"));
        robot.clickOn("#country");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Error: One of the fields is empty. Enter info for field: role"));
        robot.clickOn("#role");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Error: The entered full name is not formatted correctly: Full name needs to contain at least 2 words"));
        robot.clickOn("#fullName");
        robot.write(" Account");
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Error: The entered phone number is not formatted correctly for the country chosen: Argentina"));
        robot.clickOn("#phoneNumber");
        robot.write("9");
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Error: An account with the following username already exists: unit_test1"));
        robot.clickOn("#usernameField");
        robot.write("1");
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Password is weak: Password must have at least 8 characters"));
        robot.clickOn("#passwordField");
        robot.write("h");
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Password is weak: Password must contain at least an upper character"));
        robot.clickOn("#passwordField");
        robot.write("A");
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Password is weak: Password must contain at least one digit"));
        robot.clickOn("#passwordField");
        robot.write("1");
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Password is weak: Password must contain at least a special character"));
        robot.clickOn("#passwordField");
        robot.write("$");
        robot.clickOn("#registerButton");
        FxAssert.verifyThat(registerMessage.getText(),is("Account created successfully! You will be redirected to the login page in 5 seconds."));
        Button bExit = robot.lookup("#registerButton").queryButton();
        Stage stageExit = (Stage) bExit.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageExit.close();
                }
        );
    }



}