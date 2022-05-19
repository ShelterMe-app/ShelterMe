package org.ShelterMe.project.services;

import org.ShelterMe.project.exceptions.*;
import org.ShelterMe.project.model.AffectedItem;
import org.ShelterMe.project.model.Volunteer;
import org.ShelterMe.project.model.VolunteerItem;
import org.apache.commons.io.FileUtils;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public static void addItem(String username, String name, String categories, String supplies, float quantity, String imageBase64) throws  EmptyFieldException{
        checkItemEmptyFields(name, categories, supplies);
        volunteerItemsRepository.insert(new VolunteerItem(username, name, categories, supplies,  quantity, imageBase64));
    }

    public static void checkItemEmptyFields(String name, String categories, String supplies) throws EmptyFieldException {
        if (name.length() == 0)
            throw new EmptyFieldException("name");
        if(categories.length() == 0)
            throw new EmptyFieldException("category");
        if(supplies.length() == 0)
            throw new EmptyFieldException("supplies");
    }

    public static List<VolunteerItem> databaseToList(String username) {
        Predicate<VolunteerItem> isUsername = volunteer -> volunteer.getUsername().equals(username);
        return volunteerItemsRepository.find().toList().stream().filter(isUsername).collect(Collectors.toList());
    }

    public static void editItem(int id, String name, String categories, String supplies, float quantity, String imageBase64) throws EmptyFieldException{
        checkItemEmptyFields(name, categories, supplies);
        for (VolunteerItem item : volunteerItemsRepository.find()) {
            if (Objects.equals(id, item.getId())) {
                if (name.length() > 0)
                    item.setName(name);
                if (categories.length() > 0)
                    item.setCategory(categories);
                if (supplies.length() > 0)
                    item.setSupplies(supplies);
                if (quantity > 0)
                    item.setQuantity(quantity);
                item.setImageBase64(imageBase64);
                volunteerItemsRepository.update(item);
                break;
            }
        }
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

    public static String imageToBase64(String filePath) throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
        String encodedString = Base64
                .getEncoder()
                .encodeToString(fileContent);
        return encodedString;
    }

    public static Image base64ToImage(String base64) throws IOException {
        if (base64 == null)
            return null;
        byte[] decodedBytes = Base64
                .getDecoder()
                .decode(base64);
        InputStream stream = new ByteArrayInputStream(decodedBytes);
        Image recoveredImage = new Image(stream);
        return recoveredImage;
    }

    public static void removeItem(int id) {
        for (VolunteerItem item : volunteerItemsRepository.find()) {
            if (Objects.equals(id, item.getId())) {
                volunteerItemsRepository.remove(item);
                break;
            }
        }
    }

    public static List<VolunteerItem> databaseToListInbox(ArrayList<Integer> ids) {
        Predicate<VolunteerItem> isId = offer -> ids.contains(offer.getId());
        return volunteerItemsRepository.find().toList().stream().filter(isId).collect(Collectors.toList());
    }

    public static String getOfferSourceUsername(int id) {
        for (VolunteerItem item:volunteerItemsRepository.find())
            if(id == item.getId())
                return item.getUsername();
        return "";
    }

    public static String getOfferName(int id) {
        for (VolunteerItem item:volunteerItemsRepository.find())
            if(id == item.getId())
                return item.getName();
        return "";
    }

    public static List<VolunteerItem> databaseToOfferHistory(ArrayList<Integer> ids) {
        Predicate<VolunteerItem> isId = affected -> ids.contains(affected.getId());
        return volunteerItemsRepository.find().toList().stream().filter(isId).collect(Collectors.toList());
    }

    public static VolunteerItem getItemWithId(int id) {
        for (VolunteerItem item : volunteerItemsRepository.find()) {
            if (Objects.equals(id, item.getId()))
                return item;
        }
        return null;
    }
}
