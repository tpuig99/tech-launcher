package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.*;

import java.awt.*;
import java.net.URL;
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
    Content insertContent(long frameworkId, long userId, String title, URL url, ContentTypes type);
    int deleteContent(long contentId);
    Content changeContent(long contentId, String title, URL url, ContentTypes types);
    Comment insertComment(long frameworkId, long userId, String description);
    int deleteComment(long commentId);
    Comment changeComment(long commentId, String description);
}
