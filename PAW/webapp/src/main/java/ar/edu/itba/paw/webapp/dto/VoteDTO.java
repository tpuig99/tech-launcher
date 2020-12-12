package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.FrameworkVote;

public class VoteDTO {
    private Double count;

    public static VoteDTO fromFrameworkVote (FrameworkVote vote) {
        VoteDTO dto = new VoteDTO();
        dto.count = (double) vote.getStars();
        return dto;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
