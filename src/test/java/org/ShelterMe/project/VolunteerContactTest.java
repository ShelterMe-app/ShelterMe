package org.ShelterMe.project;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.ShelterMe.project.controllers.VolunteerContactController;
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

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.testfx.matcher.control.TextMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
@Category(TestFx.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VolunteerContactTest {

    public static final String PASSWORD = "uNiTTest!12";
    public static final String COUNTRY = "Romania";
    public static final String PHONENUMBER = "758214675";
    public static final String CODE = "RO";
    public static final Volunteer destination = new Volunteer("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER);
    public static final Affected loggedInAffected = new Affected("seb gab", PASSWORD, "Affected", "Seb Gab", COUNTRY, PHONENUMBER);
    FXMLLoader loader;
    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-8";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        AffectedService.initAffectedItemsDatabase();
        CommunicationService.initCommunicationDatabase();
        UserService.addUser("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER, CODE);
        UserService.addUser("seb gab", PASSWORD, "Affected", "Seb Gab", COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem("seb gab", "ahaaa", "Clothes", "Shirt", (float) 3.0, "ddddaa", "not good", "");

    }
    @Start
    public void start (@NotNull Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getClassLoader().getResource("contactVolunteerForm.fxml"));
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


    @Test
    @Order(1)
    public void testSetLoggedInVolunteer(@NotNull FxRobot robot)  {
        VolunteerContactController instance = loader.getController();
        instance.setLoggedInAffected(loggedInAffected, destination);
        FxAssert.verifyThat(robot.lookup("#title-text"), hasText("Contact Sebi Gabor"));
    }

    @Test
    @Order(2)
    void testHandleSendOfferActionNull(@NotNull FxRobot robot) {
        VolunteerContactController instance = loader.getController();
        instance.setLoggedInAffected(loggedInAffected, destination);
        robot.clickOn("#sendRequest");
        FxAssert.verifyThat(instance.getAlert().getHeaderText(), is("Select a request to send!"));
        FxAssert.verifyThat(instance.getAlert().getTitle(), is("Failed to contact Volunteer"));
        robot.press(KeyCode.ENTER);
    }

    @Test
    @Order(3)
    void testHandleSendOfferAction(@NotNull FxRobot robot) throws InterruptedException {
        VolunteerContactController instance = loader.getController();
        instance.setLoggedInAffected(loggedInAffected, destination);
        instance.getContactRequestsView().getSelectionModel().select(0);
        robot.clickOn("#sendRequest");
        FxAssert.verifyThat(instance.getAlert().getHeaderText(), is("Request sent successfully to Volunteer (Sebi Gabor)"));
        FxAssert.verifyThat(instance.getAlert().getTitle(), is("Requests sent"));
        robot.press(KeyCode.ENTER);
    }
    @Test
    @Order(4)
    void testHandleSendOfferAction2(@NotNull FxRobot robot) throws EmptyFieldException {
        CommunicationService.addCommunication('r', "seb gab", "SebiG", 1, 'p',"","", "", "");
        VolunteerContactController instance = loader.getController();
        instance.setLoggedInAffected(loggedInAffected, destination);
        instance.getContactRequestsView().getSelectionModel().select(0);
        robot.clickOn("#sendRequest");
        FxAssert.verifyThat(instance.getAlert().getTitle(), is("Failed to contact Volunteer"));
        robot.press(KeyCode.ENTER);
    }
}