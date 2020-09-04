package ar.edu.itba.paw.services;

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
    public List<Framework> getFrameworks(FrameworkCategories category) {
        return frameworkDao.getFrameworks(category);
    }
}
