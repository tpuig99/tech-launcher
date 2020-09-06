package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;

import java.awt.*;
import java.util.List;

public interface FrameworkService {
    Framework findById(long frameworkId);
    List<Framework> getByCategory(FrameworkCategories category);
    List<Framework> getAll();
    double getStars(long frameworkId);
    List<Comment> getComments(long frameworkId);
    List<Content> getContent(long frameworkId);
    int getVotesCant(long frameworkId);
    List<Framework> getCompetitors(Framework framework);
}
