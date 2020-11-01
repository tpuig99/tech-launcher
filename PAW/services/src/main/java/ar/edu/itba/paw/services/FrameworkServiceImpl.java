package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.FrameworkDao;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.FrameworkVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FrameworkServiceImpl implements FrameworkService {
   @Autowired
    private FrameworkDao frameworkDao;

   private long PAGE_SIZE = 7;
   private long PAGE_SIZE_SEARCH = 24;

    @Transactional(readOnly = true)
    @Override
    public Optional<Framework> findById(long id) {
        return frameworkDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getFrameworkNames() {
        return frameworkDao.getFrameworkNames();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getByCategory(FrameworkCategories category, long page) {
        return frameworkDao.getByCategory(category, page, PAGE_SIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft,Integer starsRight,boolean nameFlag,Integer commentAmount,Timestamp lastComment,Timestamp lastUpdated, Integer order, long page) {
        if(starsLeft<starsRight)
            return frameworkDao.search(toSearch,categories,types,starsLeft,starsRight,nameFlag,commentAmount,lastComment,lastUpdated,order, page, PAGE_SIZE_SEARCH);
        return frameworkDao.search(toSearch,categories,types,starsRight,starsLeft,nameFlag,commentAmount,lastComment,lastUpdated,order, page, PAGE_SIZE_SEARCH);
    }

    @Transactional(readOnly = true)
    @Override
    public Integer searchResultsNumber(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Timestamp lastComment, Timestamp lastUpdated){
        if(starsLeft<starsRight)
            return frameworkDao.searchResultsNumber(toSearch,categories,types,starsLeft,starsRight,nameFlag,commentAmount,lastComment,lastUpdated);
        return frameworkDao.searchResultsNumber(toSearch,categories,types,starsRight,starsLeft,nameFlag,commentAmount,lastComment,lastUpdated);
    }

    @Override
    public int getAmountByCategory(FrameworkCategories categories) {
        return frameworkDao.getAmountByCategory(categories);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getByUser(long userId, long page) {
        return frameworkDao.getByUser(userId, page, PAGE_SIZE);
    }

    @Transactional
    @Override
    public Optional<Integer> getByUserCount( long userId ){
        return frameworkDao.getByUserCount(userId);
    }

    @Transactional
    @Override
    public Optional<Framework> create(String name, FrameworkCategories category, String description, String introduction, FrameworkType type,long userId, byte[] picture) {
        return frameworkDao.create(name,category,description,introduction,type,userId, picture);
    }

    @Transactional
    @Override
    public Optional<Framework> update(long id, String name, FrameworkCategories category, String description, String introduction, FrameworkType type, byte[] picture) {
        return frameworkDao.update(id,name,category,description,introduction,type,picture);
    }

    @Transactional
    @Override
    public void delete(long id) {
        frameworkDao.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getBestRatedFrameworks() {
        List<Framework> toReturn = frameworkDao.getByMinStars(4);
        return toReturn.size() > 5 ? toReturn.subList(0,4) : toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getUserInterests(long userId) { return frameworkDao.getUserInterests(userId); }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getCompetitors(Framework framework) {
        List<Framework> toReturn = getByCategory(framework.getCategory(), 1);
        toReturn.remove(framework);
        return toReturn.size() > 5 ? toReturn.subList(0,4) : toReturn;
    }

}
