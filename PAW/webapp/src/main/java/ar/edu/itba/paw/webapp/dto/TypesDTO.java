package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Framework;

import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

public class TypesDTO {
    private List<FrameworkDTO> techs;
    private Integer amount;
    private String type;
    private String location;

    public static TypesDTO fromTypes(String type, UriInfo uriInfo){
        final TypesDTO dto = new TypesDTO();
        dto.type = type;
        return dto;
    }

    public List<FrameworkDTO> getTechs() {
        return techs;
    }

    public void setTechs(List<FrameworkDTO> techs) {
        this.techs = techs;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
