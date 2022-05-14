package org.ShelterMe.project.model;

import javafx.scene.image.Image;
import org.dizitart.no2.objects.Id;

public class VolunteerItem {
    @Id
    private String username;
    private String name;
    private String category;
    private String supplies;
    private float quantity;
    private Image image;

    public VolunteerItem(String username, String name, String category, String supplies, float quantity, Image image) {
        this.username = username;
        this.name = name;
        this.category = category;
        this.supplies = supplies;
        this.quantity = quantity;
        this.image = image;
    }

    public VolunteerItem(String username, String name, String category, String supplies, float quantity, java.awt.Image image) {
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

    public void setImage(Image image) {
        this.image = image;
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

    public Image getImage() {
        return image;
    }
}
