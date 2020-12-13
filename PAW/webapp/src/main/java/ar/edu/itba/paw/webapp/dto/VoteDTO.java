package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.FrameworkVote;

public class VoteDTO {
    private Long voteId;
    private String frameworkName;
    private String frameworkCategory;
    private Double count;

    public static VoteDTO fromFrameworkVote (FrameworkVote vote) {
        VoteDTO dto = new VoteDTO();
        dto.count = (double) vote.getStars();
        dto.frameworkName = vote.getFrameworkName();
        dto.frameworkCategory = vote.getCategory();
        dto.voteId = vote.getVoteId();
        return dto;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
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
