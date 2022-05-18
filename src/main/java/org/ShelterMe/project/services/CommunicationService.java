package org.ShelterMe.project.services;

import javafx.scene.image.Image;
import org.ShelterMe.project.model.*;
import org.apache.commons.io.FileUtils;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CommunicationService {
    private static ObjectRepository<Communication> communicationRepository;

    public static void initCommunicationDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(FileSystemService.getPathToFile("communication.db").toFile())
                .openOrCreate("test", "test");

        communicationRepository = database.getRepository(Communication.class);
    }

    public static void addCommunication(char type, String sourceUsername, String destinationUsername, int id, char status, String sourceMessage, String destinationMessage, String sourceContactMethods, String destinationContactMethods) {
        communicationRepository.insert(new Communication(type, sourceUsername, destinationUsername, id, status, sourceMessage, destinationMessage, sourceContactMethods, destinationContactMethods));
    }

    public static int getCounter() {
        int index = 0;
        for (Communication item : communicationRepository.find()) {
            if (item.getCommunicationId() > index) {
                index = item.getCommunicationId();
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

    public static ArrayList<Integer> getSourceIDs(String destination) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Communication item : communicationRepository.find()) {
            if (destination.equals(item.getDestinationUsername()) && !ids.contains(item.getId()) && !item.getInHistory()) {
                ids.add(item.getId());
            }
        }
        return ids;
    }

    public static String getSourceDescription(int id){
        for (Communication item:communicationRepository.find()){
            if(id == item.getId())
                return item.getSourceMessage();
        }
        return "";
    }

    public static String getSourceInfo(int id){
        for (Communication item:communicationRepository.find()){
            if(id == item.getId())
                return item.getSourceContactMethods();
        }
        return "";
    }

    public static String getSourceName(int id){
        for (Communication item:communicationRepository.find()){
            if(id == item.getId()) {
                return item.getSourceUsername();
            }
        }
        return "";
    }

    public static void closeRequest(String source, String destination, int id, char status){
        for (Communication item:communicationRepository.find()){
            if(id == item.getId() && item.getInHistory() == false && item.getSourceUsername().equals(source) && item.getDestinationUsername().equals(destination)) {
                item.setStatus(status);
                item.setInHistory(true);
                communicationRepository.update(item);
                break;
            }
        }
    }

    public static int getActiveRequestsNumber(String destination){
        int activeRequestNumber = 0;
        for (Communication item : communicationRepository.find()) {
            if (destination.equals(item.getDestinationUsername()) && !item.getInHistory()) {
                activeRequestNumber++;
            }
        }
        return activeRequestNumber;
    }
}
