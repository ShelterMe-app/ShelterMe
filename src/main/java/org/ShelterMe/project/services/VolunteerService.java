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

    public static void addItem(String username, String name, String categories, String supplies, float quantity, String imageBase64) {
        volunteerItemsRepository.insert(new VolunteerItem(username, name, categories, supplies, quantity, imageBase64));
    }

    public static int getCounter() {
        if (volunteerItemsRepository != null)
            return volunteerItemsRepository.find().toList().size();
        else return 0;
    }

}
