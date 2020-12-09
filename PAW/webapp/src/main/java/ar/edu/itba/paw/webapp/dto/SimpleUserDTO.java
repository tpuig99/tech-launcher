package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.User;

public class SimpleUserDTO {
    private String username;
    private Boolean admin;
    private Boolean verify;
    private Boolean author;
    public static SimpleUserDTO fromUser(User user, Framework framework) {
        SimpleUserDTO dto = new SimpleUserDTO();
        dto.username=user.getUsername();
        dto.admin = user.isAdmin();
        dto.verify = user.isVerifyForFramework(framework.getId());
        dto.author = framework.getAuthor().getId().equals(user.getId());
        return dto;
    }

    public static SimpleUserDTO fromUser(User user ) {
        SimpleUserDTO dto = new SimpleUserDTO();
        dto.username=user.getUsername();
        dto.admin = user.isAdmin();
        dto.verify = false;
        dto.author = false;
        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }

    public Boolean getAuthor() {
        return author;
    }

    public void setAuthor(Boolean author) {
        this.author = author;
    }
}
