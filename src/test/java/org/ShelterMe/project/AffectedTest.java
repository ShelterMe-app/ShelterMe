package org.ShelterMe.project;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import javax.swing.*;
import java.io.IOException;
import java.security.Key;

import static org.hamcrest.CoreMatchers.*;
import static org.testfx.matcher.control.TextMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
@Category(TestFx.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AffectedTest {
    private final String PASSWORD = "TeST12345!!";
    private FXMLLoader loader;
    private Scene scene;

    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-7";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        AffectedService.initAffectedItemsDatabase();
        CommunicationService.initCommunicationDatabase();
    }

    @BeforeEach
    void setUp() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException, IOException {
        UserService.addUser("unit_test1", PASSWORD, "Affected", "Unit Test", "Romania", "712345678", "RO");
        UserService.addUser("unit_test2", PASSWORD, "Volunteer", "Unit Test", "Romania", "712345678", "RO");
        AffectedService.addItem("unit_test1", "New Request", "Clean useful goods", "Something", 1, "General Information", "Health Condition", AffectedService.imageToBase64("src\\test\\resources\\BackgroundImage.jpg"));
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
    @Order(1)
    public void testHomePageMenu(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#usernameField");
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Home");
        robot.clickOn("Requests");
        robot.clickOn("Edit Request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Remove request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Show Request Image");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Offers");
        robot.clickOn("Show Message");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Show Offer Image");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Volunteers");
        robot.clickOn("Contact Volunteer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("View Volunteer Info");
        robot.type(KeyCode.ENTER);
        robot.clickOn("History");
        robot.clickOn("Show Item");
        robot.type(KeyCode.ENTER);
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
    @Order(2)
    public void testRequestsPage(@NotNull FxRobot robot) throws InterruptedException, IOException {
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
        robot.clickOn("#requestSupplies");
        robot.write("Something");
        robot.clickOn("#requestQuantity");
        robot.write("1");
        robot.clickOn("#generalInformation");
        robot.write("General Information");
        robot.clickOn("#healthCondition");
        robot.write("Health Condition");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        Button b = robot.lookup("#addRequestButton").queryButton();
        Stage stage = (Stage) b.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stage.close();
                }
        );
        robot.clickOn("Home");
        FxAssert.verifyThat(AffectedService.getCounter(), is(2));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getUsername(), is("unit_test1"));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getName(), is("New Request"));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getSupplies(), is("Something"));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getQuantity(), is(1.0F));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getGeneralInformation(), is("General Information"));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getHealthCondition(), is("Health Condition"));
        robot.clickOn("Requests");
        TableView requestsTab = robot.lookup("#requestsTable").queryTableView();
        Platform.runLater(
                () -> {
                    requestsTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#requestsTable");
        robot.clickOn("Show Request Image");
        ImageView i = (ImageView) robot.lookup("#requestImage").query();
        Stage imageStage = (Stage) i.getScene().getWindow();
        Platform.runLater(
                () -> {
                    imageStage.close();
                }
        );
        robot.clickOn("#requestsTable");
        robot.clickOn("Edit Request");
        robot.clickOn("#requestName");
        robot.write("1");
        robot.clickOn("#requestCategory");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestSupplies");
        robot.write("1");
        robot.clickOn("#requestQuantity");
        robot.write("a");
        robot.clickOn("Edit request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestQuantity");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("1");
        robot.clickOn("#generalInformation");
        robot.write("1");
        robot.clickOn("#healthCondition");
        robot.write("1");
        robot.clickOn("#removeCurrentImage");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#addRequestButton");
        robot.type(KeyCode.ENTER);
        Button b1 = robot.lookup("#addRequestButton").queryButton();
        Stage stage1 = (Stage) b1.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stage1.close();
                }
        );
        FxAssert.verifyThat(AffectedService.getCounter(), is(2));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getUsername(), is("unit_test1"));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getName(), is("New Request1"));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getSupplies(), is("Something1"));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getQuantity(), is(1.01F));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getGeneralInformation(), is("General Information1"));
        FxAssert.verifyThat(AffectedService.getItemWithId(1).getHealthCondition(), is("Health Condition1"));
        Platform.runLater(
                () -> {
                    requestsTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("Remove request");
        robot.type(KeyCode.ENTER);

        robot.clickOn("Volunteers");
        TableView volunteersTab = robot.lookup("#volunteersTable").queryTableView();
        Platform.runLater(
                () -> {
                    volunteersTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#volunteersTable");
        robot.clickOn("Contact Volunteer");
        robot.clickOn("#contactMethods");
        robot.write("contact");
        robot.clickOn("#message");
        robot.write("message");
        robot.clickOn("Send");
        robot.type(KeyCode.ENTER);
        Button bExit = robot.lookup("Send").queryButton();
        robot.clickOn("Sign Out");
        Stage stageExit = (Stage) bExit.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageExit.close();

                }
        );
        Button bExit2 = robot.lookup("#loginButton").queryButton();
        Stage stageExit2 = (Stage) bExit2.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageExit2.close();

                }
        );
    }

    @Test
    @Order(3)
    public void testVolunteerContact(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#usernameField");
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Requests");
        robot.clickOn("New request");
        robot.clickOn("#requestName");
        robot.write("");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestName");
        robot.write("New Request");
        robot.clickOn("#requestCategory");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestCategory");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestSupplies");
        robot.write("");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestSupplies");
        robot.write("Something");
        robot.clickOn("#requestQuantity");
        robot.write("");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestQuantity");
        robot.write("a");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestQuantity");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("-1");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#requestQuantity");
        robot.type(KeyCode.BACK_SPACE);
        robot.type(KeyCode.BACK_SPACE);
        robot.write("1");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#generalInformation");
        robot.write("");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#generalInformation");
        robot.write("General Information");
        robot.clickOn("#healthCondition");
        robot.write("Health Condition");
        robot.clickOn("Add request");
        robot.type(KeyCode.ENTER);
        Button b = robot.lookup("#addRequestButton").queryButton();
        Stage stage = (Stage) b.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stage.close();
                }
        );

        robot.clickOn("Volunteers");
        TableView volunteersTab = robot.lookup("#volunteersTable").queryTableView();
        Platform.runLater(
                () -> {
                    volunteersTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#volunteersTable");
        robot.clickOn("View Volunteer Info");
        TableView volunteerOffersTable = robot.lookup("#volunteerOffersTable").queryTableView();
        Stage stageOffersTable = (Stage) volunteerOffersTable.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageOffersTable.close();
                }
        );
        robot.clickOn("Contact Volunteer");
        TableView contactRequestsView = robot.lookup("#contactRequestsView").queryTableView();
        Platform.runLater(
                () -> {
                    contactRequestsView.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#contactMethods");
        robot.write("contact");
        robot.clickOn("#message");
        robot.write("message");
        robot.clickOn("Send");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Sign Out");
        robot.clickOn("#usernameField");
        robot.write("unit_test2");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Requests (new)");
        TableView requestsInboxTable = robot.lookup("#requestsInboxTable").queryTableView();
        Platform.runLater(
                () -> {
                    requestsInboxTable.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#requestsInboxTable");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Show Message");
        robot.clickOn("#volunteerReplyMessage");
        robot.write("message");
        robot.clickOn("#volunteerContactInfo");
        robot.write("info");
        robot.clickOn("Approve");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Sign Out");
        robot.clickOn("#usernameField");
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("History (new)");
        TableView historyTable = robot.lookup("#historyTable").queryTableView();
        Platform.runLater(
                () -> {
                    historyTable.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#historyTable");
        robot.clickOn("Show Item");
        robot.clickOn("View Image");
        ImageView i = (ImageView) robot.lookup("#requestImage").query();
        Stage imageStage = (Stage) i.getScene().getWindow();
        Platform.runLater(
                () -> {
                    imageStage.close();
                }
        );
        Button b2 = robot.lookup("#itemViewImage").queryButton();
        Stage stage2 = (Stage) b2.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stage2.close();
                }
        );
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
    public void testVolunteerContactReject(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#usernameField");
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Volunteers");
        TableView volunteersTab = robot.lookup("#volunteersTable").queryTableView();
        Platform.runLater(
                () -> {
                    volunteersTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#volunteersTable");
        robot.clickOn("View Volunteer Info");
        TableView volunteerOffersTable = robot.lookup("#volunteerOffersTable").queryTableView();
        Stage stageOffersTable = (Stage) volunteerOffersTable.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageOffersTable.close();
                }
        );
        robot.clickOn("Contact Volunteer");
        TableView contactRequestsView = robot.lookup("#contactRequestsView").queryTableView();
        Platform.runLater(
                () -> {
                    contactRequestsView.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#contactMethods");
        robot.write("contact");
        robot.clickOn("#message");
        robot.write("message");
        robot.clickOn("Send");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Sign Out");
        robot.clickOn("#usernameField");
        robot.write("unit_test2");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Requests (new)");
        TableView requestsInboxTable = robot.lookup("#requestsInboxTable").queryTableView();
        Platform.runLater(
                () -> {
                    requestsInboxTable.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#requestsInboxTable");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Show Message");
        robot.clickOn("#volunteerReplyMessage");
        robot.write("message");
        robot.clickOn("#volunteerContactInfo");
        robot.write("info");
        robot.clickOn("Reject");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Sign Out");
        robot.clickOn("#usernameField");
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("History (new)");
        TableView historyTable = robot.lookup("#historyTable").queryTableView();
        Platform.runLater(
                () -> {
                    historyTable.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#historyTable");
        robot.clickOn("Show Item");
        robot.clickOn("View Image");
        ImageView i = (ImageView) robot.lookup("#requestImage").query();
        Stage imageStage = (Stage) i.getScene().getWindow();
        Platform.runLater(
                () -> {
                    imageStage.close();
                }
        );
        Button b2 = robot.lookup("#itemViewImage").queryButton();
        Stage stage2 = (Stage) b2.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stage2.close();
                }
        );
        robot.clickOn("Sign Out");
        Button bExit = robot.lookup("#loginButton").queryButton();
        Stage stageExit = (Stage) bExit.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageExit.close();

                }
        );
    }

}