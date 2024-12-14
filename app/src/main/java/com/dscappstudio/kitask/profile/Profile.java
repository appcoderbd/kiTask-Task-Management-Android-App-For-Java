package com.dscappstudio.kitask.profile;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profile")
public class Profile {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String email;
    private String phone;
    private String profilePhotoUri; // URI for profile photo

    // Constructor
    public Profile(String name, String email, String phone, String profilePhotoUri) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profilePhotoUri = profilePhotoUri;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePhotoUri() {
        return profilePhotoUri;
    }

    public void setProfilePhotoUri(String profilePhotoUri) {
        this.profilePhotoUri = profilePhotoUri;
    }
}
