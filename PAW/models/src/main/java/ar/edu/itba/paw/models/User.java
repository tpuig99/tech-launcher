package ar.edu.itba.paw.models;

public class User {
    private long id;
    private String username;
    private String mail;
    private String password;

    public User(final long id,final String username,final String mail) {
        this.id = id;
        this.username = username;
    }

    public User(long id, String username, String mail, String password) {
        this.id = id;
        this.username = username;
        this.mail = mail;
        this.password = password;
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
}
