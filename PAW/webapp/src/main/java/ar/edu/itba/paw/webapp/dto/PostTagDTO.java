package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.PostTag;

public class PostTagDTO {
    private String tagName;

    public static PostTagDTO fromPostTag( PostTag tag ){
        PostTagDTO dto = new PostTagDTO();
        dto.tagName = tag.getTagName();
        return dto;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
