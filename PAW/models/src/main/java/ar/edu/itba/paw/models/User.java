package ar.edu.itba.paw.models;

public class User {
    private long id;
    private String username;
    private String mail;
    private String password;
    private boolean enable = false;
    private String description;

    public User(final long id,final String username,final String mail) {
        this.id = id;
        this.username = username;
        this.mail = mail;
    }

    public User(long id, String username, String mail, String password) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public User(long id, String username, String mail, String password, boolean enable, String description) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.enable = enable;
        this.description = description;
    }

    public User(long id, String username, String mail, String password, boolean enable) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
        this.enable = enable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                '}';
    }
}
