package org.ShelterMe.project.services;

import org.ShelterMe.project.model.Communication;
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

    public static void addCommunication(char type, String sourceUsername, String destinationUsername, int id) {
        communicationRepository.insert(new Communication(type, sourceUsername, destinationUsername, id));
    }

    public static int getCounter() {
        if (communicationRepository.find().toList().size() > 0)
            return communicationRepository.find().toList().get(communicationRepository.find().toList().size() - 1).getId();
        else return 0;
    }
}
