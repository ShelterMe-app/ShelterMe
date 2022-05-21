package org.ShelterMe.project.services;

import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.Communication;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.assertions.api.Assertions.assertThat;

class CommunicationServiceTest {
    public static final String USERNAME = "unit_test";
    public static final String DOES_NOT_EXIST_USERNAME = "unit";
    public static final String PASSWORD = "uNiTTest!12";
    public static final String WEAK_PASSWORD = "abecedar";
    public static final String ROLE = "Affected";
    public static final String ROLE_VOLUNTEER = "Volunteer";
    public static final String FULLNAME = "Unit Test";
    public static final String COUNTRY = "Romania";
    public static final String PHONENUMBER = "0758214675";
    public static final String ROMANIA_WRONG_FORMAT_PHONE_NUMBER = "0512345678";
    public static final String CODE = "RO";
    public static final String EMPTY_FIELD = "";
    public static final String FULLNAME_WORD = "Unit";

    public static final String NAME = "request";
    public static final String CATEGORY = "Housing";
    public static final String SUPPLIES = "supplies";
    public static final float QUANTITY = 1;
    public static final String GENERALINFORMATION = "General";
    public static final String HEALTHCONDITION = "Health";
    public static final String IMAGEBASE64 = "";


    public static final char TYPE_REQUEST = 'r';
    public static final char TYPE_OFFER = 'o';
    public static final String SOURCEUSERNAME = "unit_test1";
    public static final String DESTINATIONUSERNAME = "unit_test2";
    public static final int ID = 1; // id of request or offer
    public static final char STATUS_PENDING = 'p';
    public static final char STATUS_ACCEPTED = 'a';
    public static final char STATUS_REJECTED = 'r';
    public static final String SOURCEMESSAGE = "help";
    public static final String DESTINATIONMESSAGE = "ok";
    public static final String SOURCECONTACTMETHODS = "mail";
    public static final String DESTINATIONCONTACTMETHODS = "phone";
    public static final Boolean ISINHISTORY_TRUE = true;
    public static final Boolean ISINHISTORY_FALSE = false;




    @BeforeAll
    public static void beforeAll() throws IOException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-3";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        AffectedService.initAffectedItemsDatabase();
        VolunteerService.initVolunteerItemsDatabase();
        CommunicationService.initCommunicationDatabase();
    }

    @AfterAll
    public static void afterAll()  {
        UserService.closeDatabase();
        AffectedService.closeDatabase();
        VolunteerService.closeDatabase();
        CommunicationService.closeDatabase();
    }

    @BeforeEach
    public void setUp() {
        UserService.resetDatabase();
        AffectedService.resetDatabase();
        VolunteerService.resetDatabase();
        CommunicationService.resetDatabase();
    }

    @Test
    @DisplayName("Communications are added and request or offer info can be extracted")
    void testAddCommunications() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "1", PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        CommunicationService.addCommunication(TYPE_REQUEST, USERNAME, USERNAME + "1", 1, STATUS_PENDING, SOURCEMESSAGE, DESTINATIONMESSAGE, SOURCECONTACTMETHODS, DESTINATIONCONTACTMETHODS);
        assertThrows(CommunicationExistsException.class, () -> {
            CommunicationService.existsCommunication(1, USERNAME, USERNAME + "1", "request", "Volunteer");
        });
        assertThat(CommunicationService.getSourceInfo(1)).isEqualTo(SOURCECONTACTMETHODS);
        assertThat(CommunicationService.getSourceDescription(1)).isEqualTo(SOURCEMESSAGE);
        assertThat(CommunicationService.getSourceName(1)).isEqualTo(USERNAME);
        assertThat(CommunicationService.getSourceInfo(2).length()).isEqualTo(0);
        assertThat(CommunicationService.getSourceDescription(2).length()).isEqualTo(0);
        assertThat(CommunicationService.getSourceName(2).length()).isEqualTo(0);
        CommunicationService.addCommunication(TYPE_OFFER, USERNAME + "1", USERNAME, 1, STATUS_PENDING, SOURCEMESSAGE, DESTINATIONMESSAGE, SOURCECONTACTMETHODS, DESTINATIONCONTACTMETHODS);
        assertThrows(CommunicationExistsException.class, () -> {
            CommunicationService.existsCommunication(1, USERNAME + "1", USERNAME, "offer", "Affected");
        });
        assertThat(CommunicationService.getHistory(USERNAME).size()).isEqualTo(0);
        assertThat(CommunicationService.getHistory(USERNAME + "1").size()).isEqualTo(0);
        assertThat(CommunicationService.getActiveRequestsNumber(USERNAME)).isEqualTo(1);
        assertThat(CommunicationService.getActiveRequestsNumber(USERNAME + "1")).isEqualTo(1);
    }

    @Test
    @DisplayName("Communications sent to history can be extracted in a list, and their information can be viewed")
    public void testHistoryViewCommunication() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "1", PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        CommunicationService.addCommunication(TYPE_REQUEST, USERNAME, USERNAME + "1", 1, STATUS_PENDING, SOURCEMESSAGE, DESTINATIONMESSAGE, SOURCECONTACTMETHODS, DESTINATIONCONTACTMETHODS);
        CommunicationService.closeRequest(USERNAME, USERNAME + "1", ID, STATUS_ACCEPTED, DESTINATIONMESSAGE, DESTINATIONCONTACTMETHODS);
        List<Communication> h = CommunicationService.getHistory(USERNAME);
        Communication com = h.get(0);
        assertThat(com.getId()).isEqualTo(ID);
        assertThat(com.getSourceUsername()).isEqualTo(USERNAME);
        assertThat(com.getDestinationUsername()).isEqualTo(USERNAME + "1");
        assertThat(com.getStatus()).isEqualTo(STATUS_ACCEPTED);
        assertThat(com.getSourceContactMethods()).isEqualTo(SOURCECONTACTMETHODS);
        assertThat(com.getSourceMessage()).isEqualTo(SOURCEMESSAGE);
        assertThat(com.getDestinationContactMethods()).isEqualTo(DESTINATIONCONTACTMETHODS);
        assertThat(com.getCommunicationId()).isEqualTo(1);
        assertThat(com.getInHistory()).isEqualTo(true);
        assertThat(com.isType()).isEqualTo(TYPE_REQUEST);
        assertThat(com.getDestinationMessage()).isEqualTo(DESTINATIONMESSAGE);
    }

    @Test
    @DisplayName("Non-history elements for destination are retrieved")
    public void testDestination() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "1", PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        AffectedService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, GENERALINFORMATION, HEALTHCONDITION, IMAGEBASE64);
        VolunteerService.addItem(USERNAME, NAME, CATEGORY, SUPPLIES, QUANTITY, IMAGEBASE64);
        CommunicationService.addCommunication(TYPE_REQUEST, USERNAME, USERNAME + "1", 1, STATUS_PENDING, SOURCEMESSAGE, DESTINATIONMESSAGE, SOURCECONTACTMETHODS, DESTINATIONCONTACTMETHODS);
        List<Integer> d = CommunicationService.getSourceIDs(USERNAME + "1");
        assertThat(d.get(0)).isEqualTo(1);

    }

}