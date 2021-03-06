package org.ShelterMe.project.services;

import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.AffectedItem;
import org.apache.commons.io.FileUtils;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import javafx.scene.image.Image;


public class AffectedService {
    private static ObjectRepository<AffectedItem> affectedItemsRepository;

    public static void initAffectedItemsDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(FileSystemService.getPathToFile("affected-items.db").toFile())
                .openOrCreate("test", "test");
        affectedItemsRepository = database.getRepository(AffectedItem.class);
    }

    public static void addItem(String username, String name, String categories, String supplies, float quantity, String generalInformation, String healthCondition, String imageBase64) throws  EmptyFieldException {
            checkItemEmptyFields(name, categories, supplies, generalInformation);
            affectedItemsRepository.insert(new AffectedItem(username, name, categories, supplies, quantity, generalInformation, healthCondition, imageBase64));
    }

    public static void editItem(int id, String name, String categories, String supplies, float quantity, String generalInformation, String healthCondition, String imageBase64) throws EmptyFieldException {
        checkItemEmptyFields(name, categories, supplies, generalInformation);
        for (AffectedItem item : affectedItemsRepository.find()) {
            if (Objects.equals(id, item.getId())) {
                if (name != null && name.length() > 0)
                    item.setName(name);
                if (categories != null && categories.length() > 0)
                    item.setCategory(categories);
                if (supplies != null && supplies.length() > 0)
                    item.setSupplies(supplies);
                if (quantity > 0)
                    item.setQuantity(quantity);
                if (generalInformation!= null && generalInformation.length() > 0)
                    item.setGeneralInformation(generalInformation);
                if (healthCondition != null && healthCondition.length() > 0)
                     item.setHealthCondition(healthCondition);
                item.setImageBase64(imageBase64);
                affectedItemsRepository.update(item);
                break;
            }
        }
    }

    public static void removeItem(int id) {
        for (AffectedItem item : affectedItemsRepository.find()) {
            if (Objects.equals(id, item.getId())) {
                affectedItemsRepository.remove(item);
                break;
            }
        }
    }

    public static void checkItemEmptyFields(String name, String categories, String supplies, String generalInformation) throws EmptyFieldException {
        if (name == null || name.length() == 0)
            throw new EmptyFieldException("name");
        if(categories == null || categories.length() == 0)
            throw new EmptyFieldException("category");
        if (supplies == null || supplies.length() == 0)
            throw new EmptyFieldException("supplies");
        if (generalInformation == null || generalInformation.length() == 0)
            throw new EmptyFieldException("general information");
    }

    public static List<AffectedItem> databaseToList(String username) {
        Predicate<AffectedItem> isUsername = affected -> affected.getUsername().equals(username);
        return affectedItemsRepository.find().toList().stream().filter(isUsername).collect(Collectors.toList());
    }

    public static List<AffectedItem> databaseToListInbox(ArrayList<Integer> ids) {
        Predicate<AffectedItem> isUsername = affected -> ids.contains(affected.getId());
        return affectedItemsRepository.find().toList().stream().filter(isUsername).collect(Collectors.toList());
    }

    public static int getCounter() {
        int index = 0;
        for (AffectedItem item : affectedItemsRepository.find()) {
            if (item.getId() > index) {
                index = item.getId();
            }
        }
        return index;
    }

    public static String imageToBase64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64
                .getEncoder()
                .encodeToString(fileContent);
        return encodedString;
    }

    public static Image base64ToImage(String base64) {
        if (base64 == null)
            return null;
        byte[] decodedBytes = Base64
                .getDecoder()
                .decode(base64);
        InputStream stream = new ByteArrayInputStream(decodedBytes);
        Image recoveredImage = new Image(stream);
        return recoveredImage;
    }

    public static String getRequestName(int id) {
        for (AffectedItem item:affectedItemsRepository.find())
            if(id == item.getId())
                return item.getName();
        return "";
    }

    public static String getRequestDestinationUsername(int id) {
        for (AffectedItem item : affectedItemsRepository.find())
            if (id == item.getId())
                return item.getUsername();
        return "";
    }

    public static int getAffectedRequestsNumber(String username) {
        int counter = 0;
        for (AffectedItem item : affectedItemsRepository.find()) {
            if (Objects.equals(username, item.getUsername())) {
                counter++;
            }
        }
        return counter;
    }


    public static List<AffectedItem> databaseToRequestHistory(ArrayList<Integer> ids) {
        Predicate<AffectedItem> isId = affected -> ids.contains(affected.getId());
        return affectedItemsRepository.find().toList().stream().filter(isId).collect(Collectors.toList());
    }

    public static AffectedItem getItemWithId(int id) {
        for (AffectedItem item : affectedItemsRepository.find()) {
            if (Objects.equals(id, item.getId()))
                return item;
        }
        return null;
    }

    public static void closeDatabase(){
        affectedItemsRepository.close();
    }

    public static void resetDatabase() {
        for (AffectedItem item : affectedItemsRepository.find()) {
            affectedItemsRepository.remove(item);
        }
    }

}
