package org.ShelterMe.project.services;

import javafx.scene.image.Image;
import org.ShelterMe.project.model.Communication;
import org.ShelterMe.project.model.User;
import org.ShelterMe.project.model.Volunteer;
import org.apache.commons.io.FileUtils;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;

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
        User destination = UserService.getUser(destinationUsername);
        if (destination instanceof Volunteer) {
            Volunteer destinationVolunteer = (Volunteer) destination;
            destinationVolunteer.setNewOffer(true);
            UserService.updateUserInDatabase(destinationVolunteer);
        }
    }

    public static int getCounter() {
        if (communicationRepository.find().toList().size() > 0)
            return communicationRepository.find().toList().get(communicationRepository.find().toList().size() - 1).getCommunicationId();
        else return 0;
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
            if (destination.equals(item.getDestinationUsername()) && !ids.contains(item.getId())) {
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
                System.out.println(item.getSourceUsername());
                return item.getSourceUsername();
            }
        }
        return "";
    }
}
