package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.VerifyUser;

import java.util.Date;


public class VerifyUserDTO {
    private String username;
    private Boolean admin;
    private Boolean pending;
    private String frameworkName;
    private String location, techLocation;
    private String description;
    private Date timestamp;

    public static VerifyUserDTO fromVerifyUser(VerifyUser verifyUser) {
        VerifyUserDTO dto = new VerifyUserDTO();
        dto.username= verifyUser.getUser().getUsername();
        dto.admin = verifyUser.getUser().isAdmin();
        dto.pending = verifyUser.isPending();
        dto.frameworkName = verifyUser.getFrameworkName();
        dto.techLocation = "techs/" + verifyUser.getFramework().getId();
        if( !verifyUser.isPending()) {
            dto.location = "mod/" + verifyUser.getVerificationId();
        } else {
            dto.location = "mod/pending/" + verifyUser.getVerificationId();
        }
        dto.description = verifyUser.getCommentDescription();
        if( dto.description != null) {
            dto.timestamp = verifyUser.getComment().getTimestamp();
        }
        return dto;
    }

    public String getTechLocation() {
        return techLocation;
    }

    public void setTechLocation(String techLocation) {
        this.techLocation = techLocation;
    }

    public static VerifyUserDTO fromProfile(VerifyUser verifyUser) {
        VerifyUserDTO dto = new VerifyUserDTO();
        dto.frameworkName = verifyUser.getFrameworkName();
        dto.location = "techs/"+verifyUser.getFrameworkId();
        dto.pending = verifyUser.isPending();

        return dto;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public String getFrameworkName() {
        return frameworkName;
    }

    public void setFrameworkName(String frameworkName) {
        this.frameworkName = frameworkName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
