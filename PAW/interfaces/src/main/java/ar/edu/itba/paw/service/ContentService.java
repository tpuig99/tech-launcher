package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface ContentService {
    Optional<Content> getById(long contentId);
    List<Content> getContentByFramework(long frameworkId);
    List<Content> getContentByFrameworkAndUser(long frameworkId, long userId);
    List<Content> getContentByUser(long userId);
    List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type);
    List<Content> getNotPendingContentByFrameworkAndType(long frameworkId, ContentTypes type);
    List<Content> getPendingContentByFrameworkAndType(long frameworkId, ContentTypes type);
    Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type, Boolean pending);
    int deleteContent(long contentId);
    Optional<Content> changeContent(long contentId, String title, String link, ContentTypes types);

}
