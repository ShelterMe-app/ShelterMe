package org.ShelterMe.project.model;

import javafx.scene.image.Image;
import org.ShelterMe.project.services.AffectedService;
import org.dizitart.no2.objects.Id;

public class AffectedItem {
    @Id
    private int id;
    private String username;
    private String name;
    private String category;
    private String supplies;
    private float quantity;
    private String generalInformation;
    private String healthCondition;
    private String imageBase64;

    public AffectedItem(String username, String name, String category, String supplies, float quantity, String generalInformation, String healthCondition, String imageBase64) {
        this.id = AffectedService.getCounter() + 1;
        this.username = username;
        this.name = name;
        this.category = category;
        this.supplies = supplies;
        this.quantity = quantity;
        this.generalInformation = generalInformation;
        this.healthCondition = healthCondition;
        this.imageBase64 = imageBase64;
    }

    public AffectedItem() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSupplies(String supplies) {
        this.supplies = supplies;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getSupplies() {
        return supplies;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getGeneralInformation() {
        return generalInformation;
    }

    public void setGeneralInformation(String generalInformation) {
        this.generalInformation = generalInformation;
    }

    public String getHealthCondition() {
        return healthCondition;
    }

    public void setHealthCondition(String healthCondition) {
        this.healthCondition = healthCondition;
    }

    public int getId() {
        return id;
    }
}