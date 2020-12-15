package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerifyUser;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private String username;
    private String mail;
    private String password;
    private String description;
    private List<String> verificationNames;
    private Boolean enabled;
    private Boolean allowedModeration;
    private Boolean admin;
    private String comments;
    private String content;
    private String techs;
    private String votes;


    public static UserDTO fromUser (User user, UriInfo uriInfo) {
        UserDTO dto = new UserDTO();
        dto.username = user.getUsername();
        dto.description = user.getDescription();
        dto.mail = user.getMail();
        dto.verificationNames = user.getVerifications().stream().map(VerifyUser::getFrameworkName).collect(Collectors.toList());
        dto.enabled = user.isEnable();
        dto.allowedModeration = user.isAllowMod();
        dto.admin = user.isAdmin();
        dto.comments = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/comments").build().toString();
        dto.content = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/contents").build().toString();
        dto.votes = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/votes").build().toString();
        dto.techs = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/techs").build().toString();

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

    public void setVerifications(List<VerifyUser> verifyUserList) {
        this.verificationNames = verifyUserList.stream().map(VerifyUser::getFrameworkName).collect(Collectors.toList());
    }

    public List<String> getVerifications() {
        return verificationNames;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAllowedModeration() {
        return allowedModeration;
    }

    public void setAllowedModeration(Boolean allowedModeration) {
        this.allowedModeration = allowedModeration;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<String> getVerificationNames() {
        return verificationNames;
    }

    public void setVerificationNames(List<String> verificationNames) {
        this.verificationNames = verificationNames;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTechs() {
        return techs;
    }

    public void setTechs(String techs) {
        this.techs = techs;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}
