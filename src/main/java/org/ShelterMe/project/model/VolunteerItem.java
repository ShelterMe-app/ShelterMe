package org.ShelterMe.project.model;

import javafx.scene.image.Image;
import org.ShelterMe.project.services.VolunteerService;
import org.dizitart.no2.objects.Id;

public class VolunteerItem {
    @Id
    public int id;
    private String username;
    private String name;
    private String category;
    private String supplies;
    private float quantity;
    private String imageBase64;

    public VolunteerItem(String username, String name, String category, String supplies, float quantity, String imageBase64) {
        this.id = VolunteerService.getCounter() + 1;
        this.username = username;
        this.name = name;
        this.category = category;
        this.supplies = supplies;
        this.quantity = quantity;
        this.imageBase64 = imageBase64;
    }

    public VolunteerItem() {
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

    public String getImageBase64() {
        return imageBase64;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
