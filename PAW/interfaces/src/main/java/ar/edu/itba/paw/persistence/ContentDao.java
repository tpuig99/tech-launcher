package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;

import java.net.URL;
import java.util.List;

public interface ContentDao {
    Content getById(long contentId);
    List<Content> getContentByFramework(long frameworkId);
    List<Content> getContentByFrameworkAndUser(long frameworkId, long userId);
    List<Content> getContentByFrameworkAndType(long userId, ContentTypes type);
    List<Content> getContentByUser(long userId);
    Content insertContent(long frameworkId, long userId, String title, URL url, ContentTypes type);
    int deleteContent(long contentId);
    Content changeContent(long contentId, String title, URL url, ContentTypes types);
}
