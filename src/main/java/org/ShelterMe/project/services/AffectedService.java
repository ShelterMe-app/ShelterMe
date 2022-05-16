package org.ShelterMe.project.services;

import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.Affected;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.User;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.model.AffectedItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import java.util.Date;

import javafx.scene.image.Image;


public class AffectedService {
    private static ObjectRepository<AffectedItem> affectedItemsRepository;

    public static void initAffectedItemsDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(FileSystemService.getPathToFile("affected-items.db").toFile())
                .openOrCreate("test", "test");

        affectedItemsRepository = database.getRepository(AffectedItem.class);
    }

    public static AffectedItem getAffectedItems(String username) {
        for (AffectedItem item : affectedItemsRepository.find()) {
            if (Objects.equals(username, item.getUsername())) {
                return item;
            }
        }
        return null;
    }

    public static void addItem(String username, String name, String categories, String supplies, float quantity, String generalInformation, String healthCondition, String imageBase64) {
            affectedItemsRepository.insert(new AffectedItem(username, name, categories, supplies, quantity, generalInformation, healthCondition, imageBase64));
    }

    public static void editItem(String id, String name, String categories, String supplies, float quantity, String generalInformation, String healthCondition, String imageBase64) {
        for (AffectedItem item : affectedItemsRepository.find()) {
            if (Objects.equals(id, item.getId())) {
                item.setName(name);
                item.setCategory(categories);
                item.setSupplies(supplies);
                item.setQuantity(quantity);
                item.setGeneralInformation(generalInformation);
                item.setHealthCondition(healthCondition);
                affectedItemsRepository.update(item);
                break;
            }
        }
    }

    public static int getCounter() {
        if (affectedItemsRepository != null)
            return affectedItemsRepository.find().toList().size();
        else return 0;
    }
}
