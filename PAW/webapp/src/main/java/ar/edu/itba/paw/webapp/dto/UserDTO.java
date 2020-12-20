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
    private List<VerifyUserDTO> verifications;
    private Boolean enabled;
    private Boolean allowedModeration;
    private Boolean admin;
    private String comments;
    private String content;
    private String techs;
    private String votes;
    private String posts;
    private Boolean verify;
    private int commentAmount;
    private int contentAmount;
    private int votesAmount;
    private int techsAmount;
    private int postsAmount;
    private String image;


    public static UserDTO fromUser (User user, UriInfo uriInfo) {
        UserDTO dto = new UserDTO();
        dto.username = user.getUsername();
        dto.description = user.getDescription();
        dto.mail = user.getMail();
        dto.verify = false;
        if (user.getVerifications() != null && !user.getVerifications().isEmpty()) {
            dto.verifications = user.getVerifications().stream().map(VerifyUserDTO::fromProfile).collect(Collectors.toList());
            dto.verify = true;
        }
        dto.enabled = user.isEnable();
        dto.allowedModeration = user.isAllowMod();
        dto.admin = user.isAdmin();
//        dto.comments = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/comments").build().toString();
//        dto.content = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/contents").build().toString();
//        dto.votes = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/votes").build().toString();
//        dto.techs = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/techs").build().toString();

        dto.comments = "users/"+user.getId()+"/comments";
        dto.content = "users/"+user.getId()+"/contents";
        dto.votes = "users/"+user.getId()+"/votes";
        dto.techs = "users/"+user.getId()+"/techs";
        dto.posts = "users/"+user.getId()+"/posts";
        if(user.getPicture() != null)
            dto.image = uriInfo.getBaseUriBuilder().path("/users/"+user.getId()+"/image").build().toString();
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

    public List<VerifyUserDTO> getVerifications() {
        return verifications;
    }

    public void setVerifications(List<VerifyUserDTO> verifications) {
        this.verifications = verifications;
    }

    public void setVerificationsName(List<VerifyUser> verifyUserList) {
        this.verifications = verifyUserList.stream().map(VerifyUserDTO::fromProfile).collect(Collectors.toList());
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

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }

    public int getCommentAmount() {
        return commentAmount;
    }

    public void setCommentAmount(int commentAmount) {
        this.commentAmount = commentAmount;
    }

    public int getContentAmount() {
        return contentAmount;
    }

    public void setContentAmount(int contentAmount) {
        this.contentAmount = contentAmount;
    }

    public int getVotesAmount() {
        return votesAmount;
    }

    public void setVotesAmount(int votesAmount) {
        this.votesAmount = votesAmount;
    }

    public int getTechsAmount() {
        return techsAmount;
    }

    public void setTechsAmount(int techsAmount) {
        this.techsAmount = techsAmount;
    }

    public int getPostsAmount() {
        return postsAmount;
    }

    public void setPostsAmount(int postsAmount) {
        this.postsAmount = postsAmount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
