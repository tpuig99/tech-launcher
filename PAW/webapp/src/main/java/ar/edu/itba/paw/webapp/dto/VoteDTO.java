package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.FrameworkVote;

public class VoteDTO {
    private Double count;
    private String techName;

    public static VoteDTO fromFrameworkVote (FrameworkVote vote) {
        VoteDTO dto = new VoteDTO();
        dto.count = (double) vote.getStars();
        dto.techName = vote.getFrameworkName();
        return dto;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
