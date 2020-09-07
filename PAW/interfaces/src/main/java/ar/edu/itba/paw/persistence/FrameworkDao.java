package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;

import java.util.List;

public interface FrameworkDao {
    Framework findById(long id);
    List<Framework> getByCategory(FrameworkCategories category);
    List<Framework> getAll();
}
