package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface FrameworkDao {
    Optional<Framework> findById(long id);
    List<Framework> getByCategory(FrameworkCategories category);
    List<Framework> getByType(FrameworkType type);
    List<Framework> getByCategoryAndType(FrameworkType type, FrameworkCategories category);
    List<Framework> getByCategoryAndWord(FrameworkCategories category,String word);
    List<Framework> getByTypeAndWord(FrameworkType type, String word);
    List<Framework> getByCategoryAndTypeAndWord(FrameworkType type, FrameworkCategories category, String word);
    List<Framework> getAll();
    List<Framework> getByWord(String toSearch);
}
