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

}
