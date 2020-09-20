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
    private List<VerifyUser> verifications;

    public User(long id, String username, String mail, String password, boolean enable, String description, boolean allowMod,boolean admin) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.enable = enable;
        this.description = description;
        this.allowMod = allowMod;
        this.admin = admin;
        verifications = new ArrayList<>();
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

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    public boolean isVerifyForFramework(long frameworkId){
        for (VerifyUser v: verifications) {
            if(v.getFrameworkId()==frameworkId)
                return true;
        }
        return false;
    }

    public boolean isAdmin() {
        return admin;
    }
    public boolean isVerify() {
        return verifications.size() != 0;
    }
}
