package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerifyUser;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private String username;
    private String mail;
    private String description;
    private byte [] picture;
    private List<String> verificationNames;

    public static UserDTO fromUser (User user) {
        UserDTO dto = new UserDTO();
        dto.username = user.getUsername();
        dto.description = user.getDescription();
        dto.mail = user.getMail();
        dto.picture = user.getPicture();
        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public void setVerifications(List<VerifyUser> verifyUserList) {
        this.verificationNames = verifyUserList.stream().map(VerifyUser::getFrameworkName).collect(Collectors.toList());
    }

    public List<String> getVerifications() {
        return verificationNames;
    }
}
