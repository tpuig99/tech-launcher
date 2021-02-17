package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExploreService {

    Date getParsedDateOption(Integer dateOption);
    List<FrameworkCategories> getParsedCategories(List<String> categories);
    List<FrameworkType> getParsedTypes(List<String> types);
    List<String> getTags(List<String> categories, List<String> types);

    boolean isExploringByMultiple(List<String> list);
}
