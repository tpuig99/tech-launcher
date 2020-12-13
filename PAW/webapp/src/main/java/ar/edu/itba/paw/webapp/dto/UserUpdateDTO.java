package ar.edu.itba.paw.webapp.dto;

public class UserUpdateDTO {
    private String description;
    private byte[] picture;

    public UserUpdateDTO () {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
