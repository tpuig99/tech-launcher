package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.VerifyUser;

public class VerifyUserDTO {
    private String username;
    private Boolean admin;
    private Boolean pending;

    public static VerifyUserDTO fromVerifyUser(VerifyUser verifyUser) {
        VerifyUserDTO dto = new VerifyUserDTO();
        dto.username= verifyUser.getUser().getUsername();
        dto.admin = verifyUser.getUser().isAdmin();
        dto.pending = verifyUser.isPending();
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

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }
}
