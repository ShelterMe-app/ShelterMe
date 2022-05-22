package org.ShelterMe.project;

import com.google.i18n.phonenumbers.NumberParseException;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.User;
import org.ShelterMe.project.services.FileSystemService;
import org.ShelterMe.project.services.UserService;
import org.apache.commons.io.FileUtils;
import org.assertj.core.internal.bytebuddy.pool.TypePool;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.assertions.api.Assertions.assertThat;

public class UserServiceTest {

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

    @BeforeAll
    public static void beforeAll() throws IOException {
        FileSystemService.APPLICATION_FOLDER = ".shelterme-test-7";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
    }

    @AfterAll
    public static void afterAll()  {
        UserService.closeDatabase();
    }

    @BeforeEach
    public void setUp() {
        UserService.resetDatabase();
    }

    @Test
    @DisplayName("Database is initialized, and there are no users")
    public void testDatabaseIsInitializedAndNoUserIsPersisted() {
        assertThat(UserService.getAllUsers()).isNotNull();
        assertThat(UserService.getAllUsers()).isEmpty();
    }

    @Test
    @DisplayName("User is successfully persisted to Database")
    public void testUserIsAddedToDatabase() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        assertThat(UserService.getAllUsers()).isNotEmpty();
        assertThat(UserService.getAllUsers()).size().isEqualTo(1);
        User user = UserService.getAllUsers().get(0);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getPassword()).isEqualTo(UserService.encodePassword(USERNAME, PASSWORD));
        assertThat(user.getRole()).isEqualTo(ROLE);
        assertThat(user.getFullName()).isEqualTo(FULLNAME);
        assertThat(user.getCountry()).isEqualTo(COUNTRY);
        assertThat(user.getPhoneNumber()).isEqualTo("758214675");
    }

    @Test
    @DisplayName("User's Full Name must contain at least 2 words")
    public void testUserFullNameWords() {
        assertThrows(FullNameFormatException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME_WORD, COUNTRY, PHONENUMBER, CODE);
        });

        try {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME_WORD, COUNTRY, PHONENUMBER, CODE);
        }catch(FullNameFormatException e) {
            assertThat(e.getMessage()).isEqualTo("Error: The entered full name is not formatted correctly: Full name needs to contain at least 2 words");
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        } catch (EmptyFieldException e) {
            e.printStackTrace();
        } catch (WeakPasswordException e) {
            e.printStackTrace();
        } catch (PhoneNumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("User's Phone Number must be correctly formatted for selected country")
    void testUserPhoneNumber() {
        assertThrows(PhoneNumberFormatException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, ROMANIA_WRONG_FORMAT_PHONE_NUMBER, CODE);
        });

        try {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, ROMANIA_WRONG_FORMAT_PHONE_NUMBER, CODE);
        }catch(PhoneNumberFormatException e) {
            assertThat(e.getPhoneNumber()).isEqualTo(ROMANIA_WRONG_FORMAT_PHONE_NUMBER);
        } catch(FullNameFormatException e) {
            e.printStackTrace();
        } catch (UsernameAlreadyExistsException e) {
            e.printStackTrace();
        } catch (EmptyFieldException e) {
            e.printStackTrace();
        } catch (WeakPasswordException e) {
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("User can not be added twice")
    public void testUserCanNotBeAddedTwice() {
        assertThrows(UsernameAlreadyExistsException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        });
        try {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        } catch(UsernameAlreadyExistsException e) {
            assertThat(e.getUsername()).isEqualTo(USERNAME);
        } catch(EmptyFieldException e) {
            e.printStackTrace();
        } catch (FullNameFormatException e) {
            e.printStackTrace();
        } catch (WeakPasswordException e) {
            e.printStackTrace();
        } catch (PhoneNumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("User can not have empty fields")
    public void testUserEmptyFields() {
        assertThrows(EmptyFieldException.class, () -> {
            UserService.addUser(EMPTY_FIELD, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        });
    }

    @Test
    @DisplayName("User can not have weak password")
    public void testUserStrongPassword() {
        assertThrows(WeakPasswordException.class, () -> {
            UserService.addUser(USERNAME, WEAK_PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        });
    }

    @Test
    @DisplayName("User must login with their own password")
    public void testLoginIncorrectPassword() {
        assertThrows(IncorrectPasswordException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
            UserService.verifyLogin(USERNAME, WEAK_PASSWORD);
        });

        try {
            UserService.verifyLogin(USERNAME, WEAK_PASSWORD);
        } catch(IncorrectPasswordException e) {
            assertThat(e.getUser()).isEqualTo(UserService.getUser(USERNAME));
        } catch (EmptyFieldException e) {
            e.printStackTrace();
        } catch (UsernameDoesNotExistException e) {
            e.printStackTrace();
        } catch (LockedAccountException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("User's username must exist when logging in")
    public void testLoginUsername() {
        assertThrows(UsernameDoesNotExistException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
            UserService.verifyLogin(DOES_NOT_EXIST_USERNAME, PASSWORD);
        });
    }

    @Test
    @DisplayName("User's are locked if 5 or more incorrect attemps are persisted")
    public void testLoginLockoutWrongPassword() {

        assertThrows(LockedAccountException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
            User user = UserService.getUser(USERNAME);
            user.setLocked(true);
            user.setLockedInUntil(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
            UserService.updateUserInDatabase(user);
            UserService.verifyLogin(USERNAME, WEAK_PASSWORD);
        });
    }

    @Test
    @DisplayName("User's are locked if 5 or more incorrect attemps are persisted, even if they enter the right password after that")
    public void testLoginLockoutCorrectPassword() {

        assertThrows(LockedAccountException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
            User user = UserService.getUser(USERNAME);
            user.setLocked(true);
            user.setLockedInUntil(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
            UserService.updateUserInDatabase(user);
            UserService.verifyLogin(USERNAME, PASSWORD);
        });
    }

    @Test
    @DisplayName("User's can log in after lockout expired and they enter the right password")
    public void testLoginLockoutExpiredCorrectPassword() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException, IncorrectPasswordException, UsernameDoesNotExistException, LockedAccountException {

            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
            User user = UserService.getUser(USERNAME);
            user.setLocked(true);
            user.setLockedInUntil(new Date(new Date().getTime()  - (1000 * 60 * 60 * 24)));
            UserService.updateUserInDatabase(user);
            user = UserService.verifyLogin(USERNAME, PASSWORD);
            assertThat(user).isNotNull();
    }

    @Test
    @DisplayName("Users locked date expires and entering the wrong password should trigger wrong password again")
    public void testLoginLockoutExpiredWrongPassword() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException, IncorrectPasswordException, UsernameDoesNotExistException, LockedAccountException {

        assertThrows(IncorrectPasswordException.class, () -> {
            UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
            User user = UserService.getUser(USERNAME);
            user.setLocked(true);
            user.setLockedInUntil(new Date(new Date().getTime()  - (1000 * 60 * 60 * 24)));
            UserService.updateUserInDatabase(user);
            user = UserService.verifyLogin(USERNAME, WEAK_PASSWORD);
        });
    }

    @Test
    @DisplayName("Users locked date expires and entering the wrong password should trigger wrong password again")
    public void testUserNotFoundInDatabase() {
        User user = UserService.getUser(USERNAME);
        assertThat(user).isNull();
    }


    @Test
    @DisplayName("All Volunteers are retrieved from the database")
    void testVolunteersToList() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "1", PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "2", PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "3", PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        List l = UserService.volunteersToList(COUNTRY);
        assertThat(l.get(0)).isEqualTo(UserService.getUser(USERNAME + "1"));
        assertThat(l.get(1)).isEqualTo(UserService.getUser(USERNAME + "3"));
    }

    @Test
    @DisplayName("All Affected are retrieved from the database")
    void testAffectedToList() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "1", PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "2", PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        UserService.addUser(USERNAME + "3", PASSWORD, ROLE_VOLUNTEER, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        List l = UserService.affectedToList(COUNTRY);
        assertThat(l.get(0)).isEqualTo(UserService.getUser(USERNAME));
        assertThat(l.get(1)).isEqualTo(UserService.getUser(USERNAME + "2"));
    }

    @Test
    @DisplayName("Username field must not be empty on login")
    void testEmptyUsernameLogin()  {
        assertThrows(EmptyFieldException.class, () -> {
            UserService.checkLoginEmptyFields("", PASSWORD);
        });
    }

    @Test
    @DisplayName("Password field must not be empty on login")
    void testEmptyPasswordLogin()  {
        assertThrows(EmptyFieldException.class, () -> {
            UserService.checkLoginEmptyFields(USERNAME, "");
        });
    }


    @Test
    @DisplayName("Password must have at least 8 characters")
    void testCheckPasswordLength() {
        try {
            UserService.checkPassword("abc");
        } catch (WeakPasswordException e) {
            assertThat(e.getMessage()).isEqualTo("Password is weak: Password must have at least 8 characters");
        }
    }
    @Test
    @DisplayName("Password must contain at least an upper character")
    void testCheckPasswordUpper() {
        try {
            UserService.checkPassword("abcdefgh");
        } catch (WeakPasswordException e) {
            assertThat(e.getMessage()).isEqualTo("Password is weak: Password must contain at least an upper character");
        }
    }
    @Test
    @DisplayName("Password must contain at least a lower character")
    void testCheckPasswordLower() {
        try {
            UserService.checkPassword("ABCDEFGH");
        } catch (WeakPasswordException e) {
            assertThat(e.getMessage()).isEqualTo("Password is weak: Password must contain at least a lower character");
        }
    }
    @Test
    @DisplayName("Password must contain at least one digit")
    void testCheckPasswordDigit() {
        try {
            UserService.checkPassword("abcDefgh");
        } catch (WeakPasswordException e) {
            assertThat(e.getMessage()).isEqualTo("Password is weak: Password must contain at least one digit");
        }
    }
    @Test
    @DisplayName("Password must contain at least a special character")
    void testCheckPasswordSpecial() {
        try {
            UserService.checkPassword("abcDefg1");
        } catch (WeakPasswordException e) {
            assertThat(e.getMessage()).isEqualTo("Password is weak: Password must contain at least a special character");
        }
    }

    @Test
    @DisplayName("Username field can not be empty on registration")
    void testCheckEmptyFieldUsername() {
        try {
            UserService.checkEmptyFields("", PASSWORD, ROLE, FULLNAME,COUNTRY, PHONENUMBER);
        } catch (EmptyFieldException e) {
            assertThat(e.getMessage()).isEqualTo("Error: One of the fields is empty. Enter info for field: username");
        }
    }

    @Test
    @DisplayName("Password field can not be empty on registration")
    void testCheckEmptyFieldPassword() {
        try {
            UserService.checkEmptyFields(USERNAME, "", ROLE, FULLNAME,COUNTRY, PHONENUMBER);
        } catch (EmptyFieldException e) {
            assertThat(e.getMessage()).isEqualTo("Error: One of the fields is empty. Enter info for field: password");
        }
    }

    @Test
    @DisplayName("Role field can not be empty on registration")
    void testCheckEmptyFieldRole() {
        try {
            UserService.checkEmptyFields(USERNAME, PASSWORD, "", FULLNAME,COUNTRY, PHONENUMBER);
        } catch (EmptyFieldException e) {
            assertThat(e.getMessage()).isEqualTo("Error: One of the fields is empty. Enter info for field: role");
        }
    }

    @Test
    @DisplayName("Full Name field can not be empty on registration")
    void testCheckEmptyFieldFullName() {
        try {
            UserService.checkEmptyFields(USERNAME, PASSWORD, ROLE, "",COUNTRY, PHONENUMBER);
        } catch (EmptyFieldException e) {
            assertThat(e.getMessage()).isEqualTo("Error: One of the fields is empty. Enter info for field: full name");
        }
    }

    @Test
    @DisplayName("Country field can not be empty on registration")
    void testCheckEmptyFieldCountry() {
        try {
            UserService.checkEmptyFields(USERNAME, PASSWORD, ROLE, FULLNAME,"", PHONENUMBER);
        } catch (EmptyFieldException e) {
            assertThat(e.getMessage()).isEqualTo("Error: One of the fields is empty. Enter info for field: country");
        }
    }

    @Test
    @DisplayName("Username field can not be empty on registration")
    void testCheckEmptyFieldPhoneNumber() {
        try {
            UserService.checkEmptyFields(USERNAME, PASSWORD, ROLE, FULLNAME,COUNTRY, "");
        } catch (EmptyFieldException e) {
            assertThat(e.getMessage()).isEqualTo("Error: One of the fields is empty. Enter info for field: phone number");
        }
    }

    @Test
    @DisplayName("User can be updated in database")
    void testUpdateUserDatabase() throws UsernameAlreadyExistsException, EmptyFieldException, FullNameFormatException, WeakPasswordException, PhoneNumberFormatException {
        UserService.addUser(USERNAME, PASSWORD, ROLE, FULLNAME, COUNTRY, PHONENUMBER, CODE);
        User user = UserService.getUser(USERNAME);
        user.setFullName("test");
        UserService.updateUserInDatabase(user);
        assertThat(user.getFullName()).isEqualTo("test");
    }

}