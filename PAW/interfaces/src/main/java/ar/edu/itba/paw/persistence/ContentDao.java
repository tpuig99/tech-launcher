package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface ContentDao {
    Optional<Content> getById(long contentId);
    List<Content> getContentByFramework(long frameworkId);
    List<Content> getContentByFrameworkAndUser(long frameworkId, long userId);
    List<Content> getContentByFrameworkAndType(long userId, ContentTypes type);
    List<Content> getContentByUser(long userId);
    List<Content> getContentByFrameworkAndTypeAndTitle(long frameworkId, ContentTypes type,String title);
    Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type);
    int deleteContent(long contentId);
    Optional<Content> changeContent(long contentId, String title, String link, ContentTypes types);
}
