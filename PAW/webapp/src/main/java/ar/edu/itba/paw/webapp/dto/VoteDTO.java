package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.FrameworkVote;

import javax.ws.rs.core.UriInfo;

public class VoteDTO {
    private String techLocation;
    private String frameworkName;
    private String frameworkCategory;
    private Double count;

    public static VoteDTO fromFrameworkVote (FrameworkVote vote, UriInfo uriInfo) {
        VoteDTO dto = new VoteDTO();
        dto.count = (double) vote.getStars();
        dto.frameworkName = vote.getFrameworkName();
        dto.frameworkCategory = vote.getCategory();
        dto.techLocation = uriInfo.getBaseUriBuilder().path("/techs/"+vote.getFrameworkId()).build().toString();
        return dto;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public String getTechLocation() {
        return techLocation;
    }

    public void setTechLocation(String techLocation) {
        this.techLocation = techLocation;
    }

    public String getFrameworkName() {
        return frameworkName;
    }

    public void setFrameworkName(String frameworkName) {
        this.frameworkName = frameworkName;
    }

    public String getFrameworkCategory() {
        return frameworkCategory;
    }

    public void setFrameworkCategory(String frameworkCategory) {
        this.frameworkCategory = frameworkCategory;
    }
}
