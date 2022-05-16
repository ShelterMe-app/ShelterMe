package org.ShelterMe.project.services;

import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.User;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.model.VolunteerItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import javafx.scene.image.Image;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import java.util.Date;

public class VolunteerService {
    private static ObjectRepository<VolunteerItem> volunteerItemsRepository;

    public static void initVolunteerItemsDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(FileSystemService.getPathToFile("volunteer-items.db").toFile())
                .openOrCreate("test", "test");

        volunteerItemsRepository = database.getRepository(VolunteerItem.class);
    }

    public static VolunteerItem getVolunteerItems(String username) {
        for (VolunteerItem item : volunteerItemsRepository.find()) {
            if (Objects.equals(username, item.getUsername())) {
                return item;
            }
        }
        return null;
    }

    public static void addItem(String username, String name, String categories, String supplies, String quantity, String imageBase64) throws  EmptyFieldException{
        chackItemEmptyFields(name, categories, quantity);
        volunteerItemsRepository.insert(new VolunteerItem(username, name, categories, supplies,  Float.valueOf(quantity), imageBase64));
    }

    public static void chackItemEmptyFields(String name, String categories, String quantity) throws EmptyFieldException {
        if (name.length() == 0)
            throw new EmptyFieldException("name");
        if(categories.length() == 0)
            throw new EmptyFieldException("category");
        if(quantity.length() == 0)
            throw new EmptyFieldException("quantity");
    }

    public static int getCounter() {
        int index = 0;
        for (VolunteerItem item : volunteerItemsRepository.find()) {
            if (item.getId() > index) {
                index = item.getId();
            }
        }
        return index;
    }

    public static int getVolunteerOffersNumber(String username){
        int counter = 0;
        for (VolunteerItem item : volunteerItemsRepository.find()) {
            if (Objects.equals(username, item.getUsername())) {
                counter++;
            }
        }
        return counter;
    }

}
