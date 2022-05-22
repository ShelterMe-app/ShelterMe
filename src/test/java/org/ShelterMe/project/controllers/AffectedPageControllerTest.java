package org.ShelterMe.project.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.Affected;
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
import org.ShelterMe.project.controllers.AffectedPageController;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.testfx.matcher.control.TextMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
@Category(TestFx.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AffectedPageControllerTest {
    private final String PASSWORD = "TeST12345!!";
    private FXMLLoader loader;
    private Scene scene;

    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-102";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        AffectedService.initAffectedItemsDatabase();
        CommunicationService.initCommunicationDatabase();
    }

    @BeforeEach
    void setUp() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser("unit_test1", PASSWORD, "Affected", "Unit Test", "Romania", "712345678", "RO");
        UserService.addUser("unit_test2", PASSWORD, "Volunteer", "Unit Test", "Romania", "712345678", "RO");
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
    public void testHomePageMenu(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#usernameField");
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Home");
        robot.clickOn("Requests");
        robot.clickOn("Offers");
        robot.clickOn("Volunteers");
        robot.clickOn("History");
        robot.clickOn("Sign Out");
    }

    @Test
    public void testRequestsPage(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#usernameField");
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Requests");
        robot.clickOn("New request");
        robot.clickOn("#requestName");
        robot.write("New Request");
        robot.clickOn("#requestCategory");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
    }

}