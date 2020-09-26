package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.FrameworkDao;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.FrameworkVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Framework> getByType(FrameworkType type) {
        return frameworkDao.getByType(type);
    }

    @Override
    public List<Framework> getByWord(String toSearch) {
        return frameworkDao.getByWord(toSearch);
    }

    @Override
    public List<Framework> search(String toSearch, FrameworkCategories category, FrameworkType type) {
        if(!toSearch.isEmpty() && category==null && type==null){
            return  frameworkDao.getByWord(toSearch);
        }
        if(!toSearch.isEmpty() && category!=null && type==null){
            return  frameworkDao.getByCategoryAndWord(category,toSearch);
        }
        if(!toSearch.isEmpty() && category==null && type!=null){
            return  frameworkDao.getByTypeAndWord(type,toSearch);
        }
        if(!toSearch.isEmpty() && category!=null && type!=null){
            return  frameworkDao.getByCategoryAndTypeAndWord(type,category,toSearch);
        }
        if(toSearch.isEmpty() && category==null && type!=null){
            return  frameworkDao.getByType(type);
        }
        if(toSearch.isEmpty() && category!=null && type!=null){
            return  frameworkDao.getByCategoryAndType(type,category);
        }
        if(toSearch.isEmpty() && category!=null && type==null){
            return  frameworkDao.getByCategory(category);
        }
        return new ArrayList<Framework>();
    }

    @Override
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer stars) {
        return frameworkDao.search(toSearch,categories,types,stars);
    }

    @Override
    public List<Framework> getByMultipleCategories(List<FrameworkCategories> categories) {
        return frameworkDao.getByMultipleCategories(categories);
    }

    @Override
    public List<Framework> getByMultipleTypes(List<FrameworkType> types) {
        return frameworkDao.getByMultipleTypes(types);
    }

    @Override
    public List<Framework> getByMinStars(int stars) {
        return frameworkDao.getByMinStars(stars);
    }

    @Override
    public List<Framework> getBestRatedFrameworks() {
        List<Framework> toReturn = getAll().stream().filter(framework -> framework.getStars() > 4).collect(Collectors.toList());
        return toReturn.size() > 5 ? toReturn.subList(0,4) : toReturn;
    }

    @Override
    public List<Framework> getUserInterests(long userId) { return frameworkDao.getUserInterests(userId); }

    @Override
    public List<Framework> getAll() {
        return frameworkDao.getAll();
    }

    @Override
    public List<Framework> getCompetitors(Framework framework) {
        List<Framework> toReturn = getByCategory(framework.getFrameCategory());
        toReturn.remove(framework);
        return toReturn.size() > 5 ? toReturn.subList(0,4) : toReturn;
    }

}
