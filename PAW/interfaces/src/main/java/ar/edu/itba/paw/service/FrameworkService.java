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
    double getStars(long frameworkId);
    List<Comment> getComments(long frameworkId);
    List<Content> getContent(long frameworkId);
    int getVotesCant(long frameworkId);
    List<Framework> getCompetitors(Framework framework);
    List<Framework> getByWord(String toSearch);
    List<Framework> search(String toSearch,FrameworkCategories category,FrameworkType type);
    Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type, Boolean pending);
    int deleteContent(long contentId);
    Optional<Content> changeContent(long contentId, String title, String link, ContentTypes types);
    Comment insertComment(long frameworkId, long userId, String description);
    int deleteComment(long commentId);
    Optional<Comment> changeComment(long commentId, String description);

}
