package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;

import java.util.List;

public interface FrameworkService {
    Framework findById(long id);
    List<Framework> getByCategory(FrameworkCategories category);
    List<Framework> getAll();
    double getStars(long id);
    List<Comment> getComments(long id);
    List<Content> getContent(long id);
}
