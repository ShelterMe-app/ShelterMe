package org.ShelterMe.project;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.ShelterMe.project.controllers.AffectedContactController;
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
public class AffectedContactTest {

    public static final String PASSWORD = "uNiTTest!12";
    public static final String COUNTRY = "Romania";
    public static final String PHONENUMBER = "758214675";
    public static final String CODE = "RO";
    public static final Volunteer logedInVolunteer = new Volunteer("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER);
    public static final Affected destination = new Affected("seb gab", PASSWORD, "Affected", "Seb Gab", COUNTRY, PHONENUMBER);
    private FXMLLoader loader;
    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-1";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        AffectedService.initAffectedItemsDatabase();
        CommunicationService.initCommunicationDatabase();
        UserService.addUser("SebiG", PASSWORD, "Volunteer", "Sebi Gabor", COUNTRY, PHONENUMBER, CODE);
        UserService.addUser("seb gab", PASSWORD, "Affected", "Seb Gab", COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem("SebiG", "mda", "Clothes", "shirt", (float) 3.0, "");
    }
    @Start
    public void start (@NotNull Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getClassLoader().getResource("contactAffectedForm.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
        stage.toFront();
    }

    @AfterAll
    public static void afterAll() {
        UserService.closeDatabase();
        VolunteerService.closeDatabase();
        AffectedService.closeDatabase();
        CommunicationService.closeDatabase();
    }


    @Test
    @Order(1)
    public void setLoggedInVolunteer(@NotNull FxRobot robot)  {
        AffectedContactController instance = loader.getController();
        instance.setLoggedInVolunteer(logedInVolunteer, destination);
        FxAssert.verifyThat(robot.lookup("#title-text"), hasText("Contact Seb Gab"));
    }

    @Test
    @Order(2)
    void handleSendOfferActionNull(@NotNull FxRobot robot) {
        AffectedContactController instance = loader.getController();
        instance.setLoggedInVolunteer(logedInVolunteer, destination);
        robot.clickOn("#sendOffer");
        FxAssert.verifyThat(instance.getAlert().getHeaderText(), is("Select an offer to send!"));
        FxAssert.verifyThat(instance.getAlert().getTitle(), is("Failed to contact Affected"));
        robot.press(KeyCode.ENTER);
    }

    @Test
    @Order(3)
    void handleSendOfferAction(@NotNull FxRobot robot) throws InterruptedException {
        AffectedContactController instance = loader.getController();
        instance.setLoggedInVolunteer(logedInVolunteer, destination);
        instance.getContactOffersView().getSelectionModel().select(0);
        robot.clickOn("#sendOffer");
        FxAssert.verifyThat(instance.getAlert().getHeaderText(), is("Offer sent successfully to Affected (Seb Gab)"));
        FxAssert.verifyThat(instance.getAlert().getTitle(), is("Offers sent"));
        robot.press(KeyCode.ENTER);
    }
    @Test
    @Order(4)
    void handleSendOfferAction2(@NotNull FxRobot robot) throws EmptyFieldException {
        CommunicationService.addCommunication('o', "SebiG", "seb gab", 1, 'p',"","", "", "");
        AffectedContactController instance = loader.getController();
        instance.setLoggedInVolunteer(logedInVolunteer, destination);
        instance.getContactOffersView().getSelectionModel().select(0);
        robot.clickOn("#sendOffer");
        FxAssert.verifyThat(instance.getAlert().getTitle(), is("Failed to contact Affected"));
        robot.press(KeyCode.ENTER);
    }
}