package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;

import java.net.URL;
import java.util.List;

public interface ContentService {
    Content getById(long contentId);
    List<Content> getContentByFramework(long frameworkId);
    List<Content> getContentByFrameworkAndUser(long frameworkId, long userId);
    List<Content> getContentByUser(long userId);
    List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type);
    Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type);
    int deleteContent(long contentId);
    Content changeContent(long contentId, String title, String link, ContentTypes types);

}
