package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.persistence.FrameworkDao;
import ar.edu.itba.paw.service.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrameworkServiceImpl implements FrameworkService {

    @Autowired
    private FrameworkDao frameworkDao;

    @Override
    public Framework findById(long id) {
        return frameworkDao.findById(id);
    }

    @Override
    public List<Framework> getByCategory(FrameworkCategories category) {
        return frameworkDao.getByCategory(category);
    }

    @Override
    public List<Framework> getAll() {
        return null;
    }

    @Override
    public double getStars(long id) {
        return 0;
    }

    @Override
    public List<Comment> getComments(long id) {
        return null;
    }

    @Override
    public List<Content> getContent(long id) {
        return null;
    }


}
