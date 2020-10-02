package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;

import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface ContentService {
    /***Getters***/
    Optional<Content> getById(long contentId);
    List<Content> getContentByFramework(long frameworkId);
    List<Content> getContentByFrameworkAndUser(long frameworkId, long userId);
    List<Content> getContentByUser(long userId);
    List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type);
    /***Content methods***/
    Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type);
    int deleteContent(long contentId);
    Optional<Content> changeContent(long contentId, String title, String link, ContentTypes types);
    /***Votes methods***/
    void voteUp(long contentId, long userId);
    void voteDown(long contentId,long userId);
}
