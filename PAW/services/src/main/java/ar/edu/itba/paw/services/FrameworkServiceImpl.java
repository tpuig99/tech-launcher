package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.FrameworkDao;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.FrameworkVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FrameworkServiceImpl implements FrameworkService {

    @Autowired
    private FrameworkDao frameworkDao;

    @Override
    public Optional<Framework> findById(long id) {
        return frameworkDao.findById(id);
    }

    @Override
    public List<Framework> getByCategory(FrameworkCategories category) {
        return frameworkDao.getByCategory(category);
    }

    @Override
    public List<Framework> getByNameOrCategory(String toSearch) {
        return frameworkDao.getByNameOrCategory(toSearch);
    }

    @Override
    public List<Framework> getAll() {
        return frameworkDao.getAll();
    }

    @Override
    public List<Framework> getCompetitors(Framework framework) {
        List<Framework> competitors = getByCategory(framework.getFrameCategory());
        competitors.remove(framework);
        return competitors;
    }

}
