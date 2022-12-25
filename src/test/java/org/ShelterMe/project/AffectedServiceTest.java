package org.ShelterMe.project;

import javafx.scene.image.Image;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.services.AffectedService;
import org.ShelterMe.project.services.FileSystemService;
import org.ShelterMe.project.services.UserService;
import org.apache.commons.io.FileUtils;
import org.assertj.core.internal.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.assertions.api.Assertions.assertThat;

class AffectedServiceTest {

    public static final String USERNAME = "unit_test";
    public static final String DOES_NOT_EXIST_USERNAME = "unit";
    public static final String PASSWORD = "uNiTTest!12";
    public static final String WEAK_PASSWORD = "abecedar";
    public static final String ROLE = "Affected";
    public static final String ROLE_VOLUNTEER = "Volunteer";
    public static final String FULLNAME = "Unit Test";
    public static final String COUNTRY = "Romania";
    public static final String PHONENUMBER = "0712345678";
    public static final String CODE = "RO";

    public static final String NAME = "request";
    public static final String CATEGORY = "Housing";
    public static final String SUPPLIES = "supplies";
    public static final float QUANTITY = 1;
    public static final String GENERALINFORMATION = "General";
    public static final String HEALTHCONDITION = "Health";
    public static final String IMAGEBASE64 = "";

    @BeforeAll
    public static void beforeAll() throws IOException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-2";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        AffectedService.initAffectedItemsDatabase();
    }

    @AfterAll
    public static void afterAll()  {
        UserService.closeDatabase();
        AffectedService.closeDatabase();
    }

    @BeforeEach
    public void setUp() {
        UserService.resetDatabase();
        AffectedService.resetDatabase();
    }


    @Test
    @DisplayName("Affected Items are succesfully tied to username and stored in database")
    void testAddAffectedItems() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        assertThat(AffectedService.databaseToList(USERNAME).size()).isEqualTo(3);
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getUsername()).isEqualTo(USERNAME);
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getName()).isEqualTo(NAME);
        assertThat(AffectedService.databaseToList(USERNAME).get(1).getName()).isEqualTo(NAME + "1");
        assertThat(AffectedService.databaseToList(USERNAME).get(2).getName()).isEqualTo(NAME + "2");
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getCategory()).isEqualTo(CATEGORY);
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getSupplies()).isEqualTo(SUPPLIES);
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getQuantity()).isEqualTo(QUANTITY);
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getGeneralInformation()).isEqualTo(GENERALINFORMATION);
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getHealthCondition()).isEqualTo(HEALTHCONDITION);
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getImageBase64()).isEqualTo(IMAGEBASE64);
    }

    @Test
    @DisplayName("Affected Items can be edited in the database")
    void testEditAffectedItems() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.editItem(1, NAME + "3", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        assertThat(AffectedService.databaseToList(USERNAME).get(0).getName()).isEqualTo(NAME + "3");
    }

    @Test
    @DisplayName("Affected Items can be removed from the database")
    void testRemoveAffectedItems() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.editItem(1, NAME + "3", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.removeItem(1);
        assertThat(AffectedService.databaseToList(USERNAME).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Image encryption and decryption works correctly")
    void testImageEncryptioDecryption() throws IOException {
        String b = AffectedService.imageToBase64("src\\test\\resources\\BackgroundImage.jpg");
        String b1 = null;
        Image image = AffectedService.base64ToImage(b);
        Image image2 = AffectedService.base64ToImage(b1);
        assertThat(image).isNotNull();
        assertThat(image2).isNull();


    }

    @Test
    @DisplayName("Request name can be retrieved from ID")
    void testGetRequestName() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        assertThat(AffectedService.getRequestName(1)).isEqualTo(NAME);
        assertThat(AffectedService.getRequestName(2)).isEqualTo(NAME + "1");
        assertThat(AffectedService.getRequestName(3)).isEqualTo(NAME + "2");
        assertThat(AffectedService.getRequestName(0)).isEqualTo("");
    }

    @Test
    @DisplayName("Request username can be retrieved from ID")
    void testGetRequestUserName() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        assertThat(AffectedService.getRequestDestinationUsername(1)).isEqualTo(USERNAME);
        assertThat(AffectedService.getRequestDestinationUsername(0)).isEqualTo("");
    }

    @Test
    @DisplayName("Affected total request number can be retrieved")
    void testGetAffectedRequestsNumber() throws EmptyFieldException, UsernameAlreadyExistsException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(DOES_NOT_EXIST_USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        assertThat(AffectedService.getAffectedRequestsNumber(USERNAME)).isEqualTo(2);

    }

    @Test
    @DisplayName("Requests can be extracted quickly in a list via their ID's")
    void testDatabaseToRequestHistory() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        ArrayList<Integer> i = new ArrayList<>();
        i.add(1);
        i.add(4);
        i.add(7);
        List<AffectedItem> l = AffectedService.databaseToRequestHistory(i);
        assertThat(l.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Affected Item can be retrieved by ID")
    void testGetItemWithId() throws EmptyFieldException {
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedItem item = AffectedService.getItemWithId(1);
        assertThat(item.getName()).isEqualTo(NAME);
        assertThat(item.getCategory()).isEqualTo(CATEGORY);
        assertThat(item.getSupplies()).isEqualTo(SUPPLIES);
        assertThat(item.getQuantity()).isEqualTo(QUANTITY);
        assertThat(item.getGeneralInformation()).isEqualTo(GENERALINFORMATION);
        assertThat(item.getHealthCondition()).isEqualTo(HEALTHCONDITION);
        assertThat(item.getImageBase64()).isEqualTo(IMAGEBASE64);
        AffectedItem item1 = AffectedService.getItemWithId(2);
        assertThat(item1).isNull();
    }

    @Test
    @DisplayName("Requests can be extracted quickly in a list via their ID's")
    void testDatabaseToListInbox() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        AffectedService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        ArrayList<Integer> i = new ArrayList<>();
        i.add(1);
        i.add(4);
        i.add(7);
        List<AffectedItem> l = AffectedService.databaseToListInbox(i);
        assertThat(l.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Affected item details can not be empty")
    public void testEmptyFields() throws EmptyFieldException {
        assertThrows(EmptyFieldException.class, () -> {
            AffectedService.checkItemEmptyFields("","","","");
        });
        assertThrows(EmptyFieldException.class, () -> {
            AffectedService.checkItemEmptyFields(null,"","","");
        });
        assertThrows(EmptyFieldException.class, () -> {
            AffectedService.checkItemEmptyFields(NAME,"","","");
        });
        assertThrows(EmptyFieldException.class, () -> {
            AffectedService.checkItemEmptyFields(NAME,null,"","");
        });
        assertThrows(EmptyFieldException.class, () -> {
            AffectedService.checkItemEmptyFields(NAME,CATEGORY,"","");
        });
        assertThrows(EmptyFieldException.class, () -> {
            AffectedService.checkItemEmptyFields(NAME,CATEGORY,null,"");
        });
        assertThrows(EmptyFieldException.class, () -> {
            AffectedService.checkItemEmptyFields(NAME,CATEGORY,SUPPLIES,"");
        });
        assertThrows(EmptyFieldException.class, () -> {
            AffectedService.checkItemEmptyFields(NAME,CATEGORY,SUPPLIES,null);
        });
        AffectedService.checkItemEmptyFields(NAME,CATEGORY,SUPPLIES,GENERALINFORMATION);
    }
}