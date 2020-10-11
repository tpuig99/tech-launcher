package ar.edu.itba.paw.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {
    private long id;
    private String username;
    private String mail;
    private String password;
    private boolean enable = false;
    private String description;
    private boolean allowMod;
    private boolean admin;
    private String contentType;
    private String base64image;
    private List<VerifyUser> verifications;

    public User(long id, String username, String mail, String password, boolean enable, String description, boolean allowMod,boolean admin, String contentType, String base64image) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.enable = enable;
        this.description = description;
        this.allowMod = allowMod;
        this.admin = admin;
        this.contentType = contentType;
        this.base64image = base64image;
        verifications = new ArrayList<>();
    }

    public String getBase64image() {
        return base64image;
    }

    public String getContentType() {
        return contentType;
    }

    public String getDescription() {
        return description;
    }
    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean isAllowMod() {
        return allowMod;
    }

    public void setVerifications(List<VerifyUser> verifications) {
        this.verifications.addAll(verifications);
    }

    public List<VerifyUser> getVerifications() {
        return verifications;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isVerifyForFramework(long frameworkId){
        for (VerifyUser v: verifications) {
            if(v.getFrameworkId()==frameworkId){
                return !v.isPending();
            }

        }
        return false;
    }
    public boolean hasAppliedToFramework(long frameworkId){
        for (VerifyUser v: verifications) {
            if(v.getFrameworkId()==frameworkId){
                return true;
            }

        }
        return false;
    }
    public boolean isAdmin() {
        return admin;
    }
    public boolean isVerify() {
        for (VerifyUser v: verifications) {
            if(!v.isPending())
                return true;
        }
        return false;
    }
}
