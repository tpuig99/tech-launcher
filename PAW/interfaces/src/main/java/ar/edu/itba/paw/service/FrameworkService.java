package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.FrameworkType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FrameworkService {
    Optional<Framework> findById(long frameworkId);
    List<String> getFrameworkNames();
    List<Framework> getByCategory(FrameworkCategories category, long page);
    List<Framework> getCompetitors(Framework framework);
    List<Framework> getBestRatedFrameworks();
    List<Framework> getUserInterests(long userId);
    List<Framework> getByUser(long userId, long page);
    List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Date lastComment, Date lastUpdated, Integer order, long page);
    Integer searchResultsNumber(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Date lastComment, Date lastUpdated);
    int getAmountByCategory(FrameworkCategories categories);

    Optional<Integer> getByUserCount(long userId);

    Optional<Framework> create(String name, FrameworkCategories category, String description, String introduction, FrameworkType type, long userId, byte[] picture);
    Optional<Framework> update(long id,String name,FrameworkCategories category,String description,String introduction,FrameworkType type,byte[] picture);
    void delete(long id);

}
