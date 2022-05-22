package org.ShelterMe.project;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
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
class VolunteerTest {
    private final String PASSWORD = "TeST12345!!";
    private FXMLLoader loader;
    private Scene scene;

    @BeforeAll
    public static void beforeAll() throws IOException, UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-10";
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
        VolunteerService.addItem("unit_test2", "New Offer", "Clean useful goods", "Something", 1,  VolunteerService.imageToBase64("src\\test\\resources\\BackgroundImage.jpg"));
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
        robot.write("unit_test2");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Home");
        robot.clickOn("Offers");
        robot.clickOn("Edit offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Remove offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Show Offer Image");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Requests");
        robot.clickOn("Show Message");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Show Request Image");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Affected");
        robot.clickOn("Contact Affected");
        robot.type(KeyCode.ENTER);
        robot.clickOn("View Affected Info");
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
    public void testAddOffer(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#usernameField");
        robot.write("unit_test2");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Offers");
        robot.clickOn("New offer");
        robot.clickOn("#offerName");
        robot.write("New Offer");
        robot.clickOn("#offerCategory");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerSupplies");
        robot.write("Something");
        robot.clickOn("#offerQuantity");
        robot.write("1");
        robot.clickOn("Add offer");
        robot.type(KeyCode.ENTER);


        Button b = robot.lookup("#addOfferButton").queryButton();
        Stage stage = (Stage) b.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stage.close();
                }
        );

        robot.clickOn("Home");
        FxAssert.verifyThat(VolunteerService.getCounter(), is(2));
        FxAssert.verifyThat(VolunteerService.getItemWithId(1).getUsername(), is("unit_test2"));
        FxAssert.verifyThat(VolunteerService.getItemWithId(1).getName(), is("New Offer"));
        FxAssert.verifyThat(VolunteerService.getItemWithId(1).getSupplies(), is("Something"));
        FxAssert.verifyThat(VolunteerService.getItemWithId(1).getQuantity(), is(1.0F));
        robot.clickOn("Offers");
        TableView offersTab = robot.lookup("#offersTable").queryTableView();
        Platform.runLater(
                () -> {
                    offersTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#offersTable");
        robot.clickOn("Show Offer Image");
        ImageView i = (ImageView) robot.lookup("#offerImage").query();
        Stage imageStage = (Stage) i.getScene().getWindow();
        Platform.runLater(
                () -> {
                    imageStage.close();
                }
        );
        robot.clickOn("#offersTable");
        robot.clickOn("Edit offer");
        robot.clickOn("#offerName");
        robot.write("1");
        robot.clickOn("#offerCategory");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerSupplies");
        robot.write("1");
        robot.clickOn("#offerQuantity");
        robot.write("a");
        robot.clickOn("Edit offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerQuantity");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("1");
        robot.clickOn("#removeCurrentImage");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#addOfferButton");
        robot.type(KeyCode.ENTER);

        Button b2 = robot.lookup("#addOfferButton").queryButton();
        Stage stage2 = (Stage) b2.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stage2.close();
                }
        );
        FxAssert.verifyThat(VolunteerService.getCounter(), is(2));
        FxAssert.verifyThat(VolunteerService.getItemWithId(1).getUsername(), is("unit_test2"));
        FxAssert.verifyThat(VolunteerService.getItemWithId(1).getName(), is("New Offer1"));
        FxAssert.verifyThat(VolunteerService.getItemWithId(1).getSupplies(), is("Something1"));
        FxAssert.verifyThat(VolunteerService.getItemWithId(1).getQuantity(), is(1.01F));

        Platform.runLater(
                () -> {
                    offersTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("Remove offer");
        robot.type(KeyCode.ENTER);

        robot.clickOn("Affected");
        TableView affectedTab = robot.lookup("#affectedTable").queryTableView();
        Platform.runLater(
                () -> {
                    affectedTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#affectedTable");
        robot.clickOn("Contact Affected");
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
    public void testAffectedContact(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#usernameField");
        robot.write("unit_test2");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Offers");
        robot.clickOn("New offer");
        robot.clickOn("#offerName");
        robot.write("");
        robot.clickOn("Add offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerName");
        robot.write("New Offer");
        robot.clickOn("#offerCategory");
        robot.clickOn("Add offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerCategory");
        robot.type(KeyCode.DOWN);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerSupplies");
        robot.write("");
        robot.clickOn("Add offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerSupplies");
        robot.write("Something");
        robot.clickOn("#offerQuantity");
        robot.write("");
        robot.clickOn("Add offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerQuantity");
        robot.write("a");
        robot.clickOn("Add offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerQuantity");
        robot.type(KeyCode.BACK_SPACE);
        robot.write("-1");
        robot.clickOn("Add offer");
        robot.type(KeyCode.ENTER);
        robot.clickOn("#offerQuantity");
        robot.type(KeyCode.BACK_SPACE);
        robot.type(KeyCode.BACK_SPACE);
        robot.write("1");
        robot.clickOn("Add offer");
        robot.type(KeyCode.ENTER);
        Button b = robot.lookup("#addOfferButton").queryButton();
        Stage stage = (Stage) b.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stage.close();
                }
        );

        robot.clickOn("Affected");
        TableView affectedTab = robot.lookup("#affectedTable").queryTableView();
        Platform.runLater(
                () -> {
                    affectedTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#affectedTable");
        robot.clickOn("View Affected Info");
        TableView volunteerOffersTable = robot.lookup("#affectedRequestsTable").queryTableView();
        Stage stageRequestsTable = (Stage) volunteerOffersTable.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageRequestsTable.close();
                }
        );
        robot.clickOn("Contact Affected");
        TableView contactOffersView = robot.lookup("#contactOffersView").queryTableView();
        Platform.runLater(
                () -> {
                    contactOffersView.getSelectionModel().select(0);
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
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Offers (new)");
        TableView offersInboxTable = robot.lookup("#offersInboxTable").queryTableView();
        Platform.runLater(
                () -> {
                    offersInboxTable.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#offersInboxTable");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Show Message");
        robot.clickOn("#affectedReplyMessage");
        robot.write("message");
        robot.clickOn("#affectedContactInfo");
        robot.write("info");
        robot.clickOn("Approve");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Sign Out");
        robot.clickOn("#usernameField");
        robot.write("unit_test2");
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
    public void testAffectedContactReject(@NotNull FxRobot robot) throws InterruptedException {
        robot.clickOn("#usernameField");
        robot.write("unit_test2");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Affected");
        TableView affectedTab = robot.lookup("#affectedTable").queryTableView();
        Platform.runLater(
                () -> {
                    affectedTab.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#affectedTable");
        robot.clickOn("View Affected Info");
        TableView affectedRequestsTable = robot.lookup("#affectedRequestsTable").queryTableView();
        Stage stageOffersTable = (Stage) affectedRequestsTable.getScene().getWindow();
        Platform.runLater(
                () -> {
                    stageOffersTable.close();
                }
        );
        robot.clickOn("Contact Affected");
        TableView contactRequestsView = robot.lookup("#contactOffersView").queryTableView();
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
        robot.write("unit_test1");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);
        robot.clickOn("#loginButton");
        Thread.sleep(2000);
        robot.clickOn("Offers (new)");
        TableView offersInboxTable = robot.lookup("#offersInboxTable").queryTableView();
        Platform.runLater(
                () -> {
                    offersInboxTable.getSelectionModel().select(0);
                }
        );
        robot.clickOn("#offersInboxTable");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Show Message");
        robot.clickOn("#affectedReplyMessage");
        robot.write("message");
        robot.clickOn("#affectedContactInfo");
        robot.write("info");
        robot.clickOn("Reject");
        robot.type(KeyCode.ENTER);
        robot.clickOn("Sign Out");
        robot.clickOn("#usernameField");
        robot.write("unit_test2");
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