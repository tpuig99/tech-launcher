package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Framework;

import java.util.List;
import java.util.stream.Collectors;

public class TechsHomeDTO {
    private List<FrameworkDTO> hots;
    private List<FrameworkDTO> interests;
    public static TechsHomeDTO fromTechs(List<Framework> frameworks) {
        final TechsHomeDTO dto = new TechsHomeDTO();
        dto.hots = frameworks.stream().map(FrameworkDTO::fromFramework).collect(Collectors.toList());
        return dto;
    }

    public List<FrameworkDTO> getHots() {
        return hots;
    }

    public void setHots(List<FrameworkDTO> hots) {
        this.hots = hots;
    }

    public List<FrameworkDTO> getInterests() {
        return interests;
    }

    public void setInterests(List<FrameworkDTO> interests) {
        this.interests = interests;
    }
}
