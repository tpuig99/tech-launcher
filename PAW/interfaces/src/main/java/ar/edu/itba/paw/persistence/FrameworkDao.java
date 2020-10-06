package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface FrameworkDao {
    Optional<Framework> findById(long id);
    List<String> getFrameworkNames();
    List<Framework> getByCategory(FrameworkCategories category);
    List<Framework> getByType(FrameworkType type);
    List<Framework> getByCategoryAndType(FrameworkType type, FrameworkCategories category);
    List<Framework> getByCategoryAndWord(FrameworkCategories category,String word);
    List<Framework> getByTypeAndWord(FrameworkType type, String word);
    List<Framework> getByCategoryAndTypeAndWord(FrameworkType type, FrameworkCategories category, String word);
    List<Framework> getAll();
    List<Framework> getByWord(String toSearch);
    List<Framework> getUserInterests(long userId);
    List<Framework> getByCategoryOrType(FrameworkType frameType, FrameworkCategories frameCategory);
    List<Framework> getByMultipleCategories(List<FrameworkCategories> categories);
    List<Framework> getByMinStars(int stars);
    List<Framework> getByMultipleTypes(List<FrameworkType> types);
    List<Framework> getByUser(long userId);
    List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer stars,boolean nameFlag);
    Optional<Framework> create(String name,FrameworkCategories category,String description,String introduction,FrameworkType type,long userId);    }
