package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(sequenceName = "users_user_id_seq", name = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name",length = 100, nullable = false, unique = true)
    private String username;

    @Column(length = 100, nullable = false, unique = true)
    private String mail;

    @Column(length = 100)
    private String password;

    @Column(name = "enabled",nullable = false)
    private boolean enable = false;
    @Column(name = "user_description",length = 200, nullable = false)
    private String description;
    @Column(name = "allow_moderator", nullable = false)
    private boolean allowMod;

    @Lob
    private byte[] picture;
    String base64image;
    String contentType;

    /*this refers to the other relation mapped in Admin*/
    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user")
    @JoinColumn(name = "user_id")
    private Admin admin;

    /*this refers to the other relation mapped in VerifyUser*/
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JoinColumn(name = "user_id")
    private List<VerifyUser> verifications;

    /*this refers to the other relation mapped in Comment*/
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JoinColumn(name = "user_id")
    private List<Comment> comments;

    /*this refers to the other relation mapped in CommentVote*/
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JoinColumn(name = "user_id")
    private List<CommentVote> commentVotes;

    /*this refers to the other relation mapped in ReportComment*/
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JoinColumn(name = "user_id")
    private List<ReportComment> commentsReported;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JoinColumn(name = "user_id")
    private List<Framework> ownedFrameworks;



    public User() {
    }
    public User(String username, String mail, String password, boolean enable, String description, boolean allowMod,byte[] picture) {
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.enable = enable;
        this.description = description;
        this.allowMod = allowMod;
        this.picture = picture;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAllowMod() {
        return allowMod;
    }

    public void setAllowMod(boolean allowMod) {
        this.allowMod = allowMod;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBase64image() {
        if(base64image==null)
            calculateStringImage();
        return base64image;
    }

    private void calculateStringImage() {
        if (picture != null) {
            base64image = new String(picture, StandardCharsets.UTF_8);
            try {
                contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(picture));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBase64image(String base64image) {
        this.base64image = base64image;
    }
    public boolean isAdmin() {
        return admin!=null;
    }
    public void setVerifications(List<VerifyUser> verifications) {
        //this.verifications.addAll(verifications);
        this.verifications = verifications;
    }

    public List<VerifyUser> getVerifications() {
        return verifications;
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
    public boolean isVerify() {
        for (VerifyUser v: verifications) {
            if(!v.isPending())
                return true;
        }
        return false;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
