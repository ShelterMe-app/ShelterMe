package org.ShelterMe.project.services;

import org.ShelterMe.project.model.Communication;
import org.ShelterMe.project.model.User;
import org.ShelterMe.project.model.Volunteer;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

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
}
