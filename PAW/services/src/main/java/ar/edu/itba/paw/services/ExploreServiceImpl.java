package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.FrameworkType;
import ar.edu.itba.paw.service.ExploreService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ExploreServiceImpl implements ExploreService {

    final int DAYS = 1, WEEK = 2, MONTH = 3, MONTHS = 4, YEAR = 5;

    @Override
    public Date getParsedDateOption(Integer dateOption) {
        LocalDate localDate = null;
        switch (dateOption) {
            case DAYS:
                localDate = LocalDate.now().minusDays(3);
                break;
            case WEEK:
                localDate = LocalDate.now().minusWeeks(1);
                break;
            case MONTH:
                localDate = LocalDate.now().minusMonths(1);
                break;
            case MONTHS:
                localDate = LocalDate.now().minusMonths(3);
                break;
            case YEAR:
                localDate = LocalDate.now().minusYears(1);
                break;
        }

        if(localDate != null){
            return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        }
        return null;
    }

    @Override
    public List<FrameworkCategories> getParsedCategories(List<String> categories) {
        List<FrameworkCategories> parsedCategories = new ArrayList<>();
        for (String c : categories) {
            parsedCategories.add(FrameworkCategories.valueOf(c));
        }
        return parsedCategories;
    }

    @Override
    public List<FrameworkType> getParsedTypes(List<String> types) {
        List<FrameworkType> parsedTypes = new ArrayList<>();
        for (String t : types) {
            parsedTypes.add(FrameworkType.valueOf(t));
        }
        return parsedTypes;
    }

    @Override
    public List<String> getTags(List<String> categories, List<String> types) {
        List<String> tags = new ArrayList<>();
        tags.addAll(categories);
        tags.addAll(types);
        if(tags.isEmpty()){
            return null;
        }
        return tags;
    }

    @Override
    public boolean isExploringByMultiple(List<String> list) {
        return (list.size() != 1 || !list.get(0).equals(""));
    }
}
