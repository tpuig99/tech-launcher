package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Framework;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesDTO {
    private List<FrameworkDTO> techs;
    private int amount;
    public static CategoriesDTO fromCategories(List<Framework> frameworks, int size, UriInfo uriInfo) {
        final CategoriesDTO dto = new CategoriesDTO();
        dto.amount = size;
        dto.techs = frameworks.stream().map((Framework framework) -> FrameworkDTO.fromExtern(framework,uriInfo)).collect(Collectors.toList());
        return dto;
    }

    public List<FrameworkDTO> getTechs() {
        return techs;
    }

    public void setTechs(List<FrameworkDTO> techs) {
        this.techs = techs;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
