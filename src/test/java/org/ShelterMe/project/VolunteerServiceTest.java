package org.ShelterMe.project;

import javafx.scene.image.Image;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.model.VolunteerItem;
import org.ShelterMe.project.services.FileSystemService;
import org.ShelterMe.project.services.UserService;
import org.ShelterMe.project.services.VolunteerService;
import org.apache.commons.io.FileUtils;
import org.assertj.core.internal.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class VolunteerServiceTest {

    public static final String USERNAME = "unit_test";
    public static final String DOES_NOT_EXIST_USERNAME = "unit";
    public static final String PASSWORD = "uNiTTest!12";
    public static final String WEAK_PASSWORD = "abecedar";
    public static final String ROLE = "Affected";
    public static final String ROLE_VOLUNTEER = "Volunteer";
    public static final String FULLNAME = "Unit Test";
    public static final String COUNTRY = "Romania";
    public static final String PHONENUMBER = "0758214675";
    public static final String CODE = "RO";

    public static final String NAME = "request";
    public static final String CATEGORY = "Housing";
    public static final String SUPPLIES = "supplies";
    public static final float QUANTITY = 1;
    public static final String IMAGEBASE64 = "";

    @BeforeAll
    public static void beforeAll() throws IOException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-4";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        VolunteerService.initVolunteerItemsDatabase();
    }

    @AfterAll
    public static void afterAll()  {
        UserService.closeDatabase();
        VolunteerService.closeDatabase();
    }

    @BeforeEach
    public void setUp() {
        UserService.resetDatabase();
        VolunteerService.resetDatabase();
    }


    @Test
    @DisplayName("Volunteer Items are succesfully tied to username and stored in database")
    void testAddVolunteerItems() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        assertThat(VolunteerService.databaseToList(USERNAME).size()).isEqualTo(3);
        assertThat(VolunteerService.databaseToList(USERNAME).get(0).getUsername()).isEqualTo(USERNAME);
        assertThat(VolunteerService.databaseToList(USERNAME).get(0).getName()).isEqualTo(NAME);
        assertThat(VolunteerService.databaseToList(USERNAME).get(1).getName()).isEqualTo(NAME + "1");
        assertThat(VolunteerService.databaseToList(USERNAME).get(2).getName()).isEqualTo(NAME + "2");
        assertThat(VolunteerService.databaseToList(USERNAME).get(0).getCategory()).isEqualTo(CATEGORY);
        assertThat(VolunteerService.databaseToList(USERNAME).get(0).getSupplies()).isEqualTo(SUPPLIES);
        assertThat(VolunteerService.databaseToList(USERNAME).get(0).getQuantity()).isEqualTo(QUANTITY);
        assertThat(VolunteerService.databaseToList(USERNAME).get(0).getImageBase64()).isEqualTo(IMAGEBASE64);
    }

    @Test
    @DisplayName("Volunteer Items can be edited in the database")
    void testEditVolunteerItems() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.editItem(1, NAME + "3", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        assertThat(VolunteerService.databaseToList(USERNAME).get(0).getName()).isEqualTo(NAME + "3");
    }

    @Test
    @DisplayName("Volunteer Items can be removed from the database")
    void testRemoveVolunteerItems() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.editItem(1, NAME + "3", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.removeItem(1);
        assertThat(VolunteerService.databaseToList(USERNAME).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Image encryption and decryption works correctly")
    void testImageEncryptioDecryption() throws IOException {
        String b = VolunteerService.imageToBase64("src\\test\\resources\\BackgroundImage.jpg");
        String b1 = null;
        Image image = VolunteerService.base64ToImage(b);
        Image image2 = VolunteerService.base64ToImage(b1);
        assertThat(image).isNotNull();
        assertThat(image2).isNull();


    }

    @Test
    @DisplayName("Request name can be retrieved from ID")
    void testGetRequestName() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        assertThat(VolunteerService.getOfferName(1)).isEqualTo(NAME);
        assertThat(VolunteerService.getOfferName(2)).isEqualTo(NAME + "1");
        assertThat(VolunteerService.getOfferName(3)).isEqualTo(NAME + "2");
        assertThat(VolunteerService.getOfferName(0)).isEqualTo("");
    }

    @Test
    @DisplayName("Offer username can be retrieved from ID")
    void testGetOfferUserName() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        assertThat(VolunteerService.getOfferSourceUsername(1)).isEqualTo(USERNAME);
        assertThat(VolunteerService.getOfferSourceUsername(0)).isEqualTo("");
    }

    @Test
    @DisplayName("Volunteer total offer number can be retrieved")
    void testGetVolunteerOfferNumber() throws EmptyFieldException, UsernameAlreadyExistsException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(DOES_NOT_EXIST_USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        assertThat(VolunteerService.getVolunteerOffersNumber(USERNAME)).isEqualTo(2);

    }

    @Test
    @DisplayName("Offers can be extracted quickly in a list via their ID's")
    void testDatabaseToOfferHistory() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(DOES_NOT_EXIST_USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        ArrayList<Integer> i = new ArrayList<>();
        i.add(1);
        i.add(4);
        i.add(7);
        List<VolunteerItem> l = VolunteerService.databaseToOfferHistory(i);
        assertThat(l.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Volunteer Item can be retrieved by ID")
    void testGetItemWithId() throws EmptyFieldException {
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerItem item = VolunteerService.getItemWithId(1);
        assertThat(item.getName()).isEqualTo(NAME);
        assertThat(item.getCategory()).isEqualTo(CATEGORY);
        assertThat(item.getSupplies()).isEqualTo(SUPPLIES);
        assertThat(item.getQuantity()).isEqualTo(QUANTITY);
        assertThat(item.getImageBase64()).isEqualTo(IMAGEBASE64);
        VolunteerItem item1 = VolunteerService.getItemWithId(2);
        assertThat(item1).isNull();
    }

    @Test
    @DisplayName("Offers can be extracted quickly in a list via their ID's")
    void testDatabaseToListInbox() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "1", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME + "2", CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        ArrayList<Integer> i = new ArrayList<>();
        i.add(1);
        i.add(4);
        i.add(7);
        List<VolunteerItem> l = VolunteerService.databaseToListInbox(i);
        assertThat(l.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Volunteer item details can not be empty")
    public void testEmptyFields() throws EmptyFieldException {
        assertThrows(EmptyFieldException.class, () -> {
            VolunteerService.checkItemEmptyFields("","","");
        });
        assertThrows(EmptyFieldException.class, () -> {
            VolunteerService.checkItemEmptyFields(NAME,"","");
        });
        assertThrows(EmptyFieldException.class, () -> {
            VolunteerService.checkItemEmptyFields(NAME,CATEGORY,"");
        });
        VolunteerService.checkItemEmptyFields(NAME,CATEGORY,SUPPLIES);
    }

}