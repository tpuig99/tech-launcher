package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.*;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface FrameworkService {
    Optional<Framework> findById(long frameworkId);
    List<String> getFrameworkNames();
    List<Framework> getByCategory(FrameworkCategories category);
    List<Framework> getByType(FrameworkType type);
    List<Framework> getAll();
    List<Framework> getCompetitors(Framework framework);
    List<Framework> getByWord(String toSearch);
    List<Framework> getBestRatedFrameworks();
    List<Framework> getUserInterests(long userId);
    List<Framework> getByMultipleCategories(List<FrameworkCategories> categories);
    List<Framework> getByMultipleTypes(List<FrameworkType> types);
    List<Framework> getByMinStars(int stars);
    List<Framework> search(String toSearch,List<FrameworkCategories> categories,List<FrameworkType> types,Integer stars,boolean nameFlag);

    void  orderByStars(List<Framework> frameworks, Integer order);
    void  orderByInteraction(List<Framework> frameworks, Integer order);
    void  orderByReleaseDate(List<Framework> frameworks, Integer order);
    void  orderByCommentsAmount(List<Framework> frameworks, Integer order);

    void create(String name,FrameworkCategories category,String description,String introduction,FrameworkType type,long userId);
}
