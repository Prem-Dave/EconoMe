package com.example.budgetapp.dataclass;

public class UserProfile {

    String id;
    String name;
    String email;
    String password;
    String profilePicture;
    String key;
    String gender;
    String birthDate;

    public UserProfile(String id, String name, String email, String password, String profilePicture, String key, String gender, String birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.key = key;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
