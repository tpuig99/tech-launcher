package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.persistence.ContentDao;
import ar.edu.itba.paw.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    ContentDao content;

    @Override
    public Content getById(long contentId) {
        return content.getById(contentId);
    }

    @Override
    public List<Content> getContentByFramework(long frameworkId) {
        return content.getContentByFramework(frameworkId);
    }

    @Override
    public List<Content> getContentByFrameworkAndUser(long frameworkId, long userId) {
        return content.getContentByFrameworkAndUser(frameworkId, userId);
    }

    @Override
    public List<Content> getContentByUser(long userId) {
        return content.getContentByUser(userId);
    }

    @Override
    public List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type) {
        return content.getContentByFrameworkAndType(frameworkId, type);
    }

    @Override
    public Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type, Boolean pending) {
        return content.insertContent(frameworkId, userId, title, link, type, pending);
    }

    @Override
    public int deleteContent(long contentId) {
        return content.deleteContent(contentId);
    }

    @Override
    public Content changeContent(long contentId, String title, String link, ContentTypes types) {
        return content.changeContent(contentId, title, link, types);
    }
}
