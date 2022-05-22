package org.ShelterMe.project.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.Affected;
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
class AffectedPageControllerTest {

    private FXMLLoader loader;
    //private static final Affected loggedInAffected = new Affected("seb gab", "uNiTTest!12", "Affected", "Seb Gab", "RO", "758214675");
    //private static AffectedPageController instance = new AffectedPageController();
    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-1000";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        AffectedService.initAffectedItemsDatabase();
        CommunicationService.initCommunicationDatabase();
    }
    @Start
    public void start (@NotNull Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getClassLoader().getResource("affectedPage.fxml"));
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

    /*@Test
    void setSignedInAs(@NotNull FxRobot robot) {
        instance.handleHomePage();
        FxAssert.verifyThat(robot.lookup("#stack").lookup("#homeTab").lookup("#welcomeMessage").queryText(), hasText("Welcome, Seb Gab"));
    }*/

    /*

    @Test
    void handleSignOut() {
    }

    @Test
    void initialize() {
    }

    @Test
    void handleHomePage() {
    }

    @Test
    void handleRequestsPage() {
    }

    @Test
    void handleVolunteersPage() {
    }

    @Test
    void handleOffersPage() {
    }

    @Test
    void handleHistoryPage() {
    }

    @Test
    void handleAddRequest() {
    }

    @Test
    void getRequests() {
    }

    @Test
    void getVolunteers() {
    }

    @Test
    void getOffersInbox() {
    }

    @Test
    void getHistory() {
    }

    @Test
    void handleTableClick() {
    }

    @Test
    void handleRequestImage() {
    }

    @Test
    void handleEditRequest() {
    }

    @Test
    void handleRemoveRequest() {
    }


    @Test
    void handleContactVolunteer() {
    }

    @Test
    void handleVolunteerInfo() {
    }

    @Test
    void handleOfferInboxTableClick() {
    }

    @Test
    void handleOfferInboxImage() {
    }

    @Test
    void handleShowMessage() {
    }

    @Test
    void handleShowItem() {
    }

    @Test
    void handleHistoryTableClick() {
    }

     */
}