package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.*;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface FrameworkService {
    Optional<Framework> findById(long frameworkId);
    List<Framework> getByCategory(FrameworkCategories category);
    List<Framework> getByType(FrameworkType type);
    List<Framework> getAll();
    List<Framework> getCompetitors(Framework framework);
    List<Framework> getByWord(String toSearch);
    List<Framework> getBestRatedFrameworks();
    List<Framework> getUserInterests(long userId);
    List<Framework> search(String toSearch,FrameworkCategories category,FrameworkType type);
    List<Framework> search(String toSearch,List<FrameworkCategories> categories,List<FrameworkType> types,Integer stars,Integer order);
    List<Framework> getByMultipleCategories(List<FrameworkCategories> categories);
    List<Framework> getByMultipleTypes(List<FrameworkType> types);
    List<Framework> getByMinStars(int stars);
}
