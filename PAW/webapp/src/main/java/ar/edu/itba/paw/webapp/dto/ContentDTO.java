package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;

import java.util.Date;

public class ContentDTO {
    private SimpleUserDTO user;
    private Date date;
    private String title;
    private String link;
    private String type;
    public static ContentDTO fromContent(Content content) {
        final ContentDTO dto = new ContentDTO();
        dto.date = content.getTimestamp();
        dto.link = content.getLink();
        dto.user = SimpleUserDTO.fromUser(content.getUser(), content.getFramework());
        dto.title = content.getTitle();
        dto.type = content.getType().name();
        return dto;
    }

    public SimpleUserDTO getUser() {
        return user;
    }

    public void setUser(SimpleUserDTO user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
