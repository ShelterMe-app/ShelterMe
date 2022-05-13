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
}
