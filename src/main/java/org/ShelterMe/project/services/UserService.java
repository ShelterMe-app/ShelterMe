package org.ShelterMe.project.services;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class UserService {

    private static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(FileSystemService.getPathToFile("registration.db").toFile())
                .openOrCreate("test", "test");

        userRepository = database.getRepository(User.class);
    }

    public static void addUser(String username, String password, String role, String fullName, String address, String phoneNumber) throws UsernameAlreadyExistsException, EmptyFieldException, PhoneNumberFormatException, WeakPasswordException, FullNameFormatException {
        checkEmptyFields(username, password, role, fullName, address, phoneNumber);
        fullName = checkFullNameFormat(fullName);
        phoneNumber = checkPhoneNumberFormat(phoneNumber);
        checkUserDoesNotAlreadyExist(username);
        checkPassword(password);
        userRepository.insert(new User(username, encodePassword(username, password), role, fullName, address, phoneNumber));
    }

    private static void checkEmptyFields(String username, String password, String role, String fullName, String address, String phoneNumber) throws EmptyFieldException {
        if (username.length() == 0)
            throw new EmptyFieldException("username");
        if (password.length() == 0)
            throw new EmptyFieldException("password");
        if (role.length() == 0)
            throw new EmptyFieldException("role");
        if (fullName.length() == 0)
            throw new EmptyFieldException("full name");
        if (address.length() == 0)
            throw new EmptyFieldException("address");
        if (phoneNumber.length() == 0)
            throw new EmptyFieldException("phone number");
    }

    private static void checkUserDoesNotAlreadyExist(String username) throws UsernameAlreadyExistsException {
        for (User user : userRepository.find()) {
            if (Objects.equals(username, user.getUsername()))
                throw new UsernameAlreadyExistsException(username);
        }
    }

    private static String checkPhoneNumberFormat(String phoneNumber) throws PhoneNumberFormatException {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumber, "RO");
            boolean isValid = phoneUtil.isValidNumber(phoneNumberProto);
            if (isValid == false)
                throw new PhoneNumberFormatException(phoneNumber);
            return Long.toString(phoneNumberProto.getNationalNumber());
        } catch (NumberParseException e) {
            throw new PhoneNumberFormatException(phoneNumber);
        }
    }

    private static String checkFullNameFormat(String FullName) throws FullNameFormatException {
        String[] words = StringUtils.normalizeSpace(FullName).split("\\s+");
        if (words.length < 2)
            throw new FullNameFormatException("Full name needs to contain at least 2 words");
        return WordUtils.capitalizeFully(StringUtils.lowerCase(StringUtils.normalizeSpace(FullName)));
    }

    private static void checkPassword(String password) throws WeakPasswordException {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasSpecial = false;
        boolean hasDigit = false;
        boolean minimum = false;

        int total = password.length();
        if (total >= 8) {
            minimum = true;
            for (int i = 0; i < total; i++) {
                char currentChar = password.charAt(i);
                if (Character.isUpperCase(currentChar) == true)
                    hasUpper = true;
                else if (Character.isLowerCase(currentChar) == true)
                    hasLower = true;
                else if (Character.isDigit(currentChar) == true)
                    hasDigit = true;
                else hasSpecial = true;
            }
        }

        if (minimum == false)
            throw new WeakPasswordException("Password must have at least 8 characters");
        else if (hasUpper == false)
            throw new WeakPasswordException("Password must contain at least an upper character");
        else if (hasLower == false)
            throw new WeakPasswordException("Password must contain at least a lower character");
        else if (hasDigit == false)
            throw new WeakPasswordException("Password must contain at least one digit");
        else if (hasSpecial == false)
            throw new WeakPasswordException("Password must contain at least a special character");


    }



    private static String encodePassword(String salt, String password) {
        MessageDigest md = getMessageDigest();
        md.update(salt.getBytes(StandardCharsets.UTF_8));

        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        // This is the way a password should be encoded when checking the credentials
        return new String(hashedPassword, StandardCharsets.UTF_8)
                .replace("\"", ""); //to be able to save in JSON format
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 does not exist!");
        }
        return md;
    }


}
