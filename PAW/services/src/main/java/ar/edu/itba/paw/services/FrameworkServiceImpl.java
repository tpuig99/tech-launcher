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

   private long PAGESIZE = 7;
   private long PAGESIZE_SEARCH = 24;

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
        return frameworkDao.getByCategory(category, page, PAGESIZE);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getByType(FrameworkType type) {
        return frameworkDao.getByType(type);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getByWord(String toSearch) {
        return frameworkDao.getByWord(toSearch);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft,Integer starsRight,boolean nameFlag,Integer commentAmount,Timestamp lastComment,Timestamp lastUpdated, long page) {
        if(starsLeft<starsRight)
            return frameworkDao.search(toSearch,categories,types,starsLeft,starsRight,nameFlag,commentAmount,lastComment,lastUpdated, page, PAGESIZE_SEARCH);
        return frameworkDao.search(toSearch,categories,types,starsRight,starsLeft,nameFlag,commentAmount,lastComment,lastUpdated, page, PAGESIZE_SEARCH);
    }

    @Transactional(readOnly = true)
    @Override
    public Integer searchResultsNumber(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft, Integer starsRight, boolean nameFlag, Integer commentAmount, Timestamp lastComment, Timestamp lastUpdated){
        if(starsLeft<starsRight)
            return frameworkDao.searchResultsNumber(toSearch,categories,types,starsLeft,starsRight,nameFlag,commentAmount,lastComment,lastUpdated);
        return frameworkDao.searchResultsNumber(toSearch,categories,types,starsRight,starsLeft,nameFlag,commentAmount,lastComment,lastUpdated);
    }

    @Override
    public void orderByStars(List<Framework> frameworks, Integer order) {
        if(order<0){
            frameworks.sort(new Comparator<Framework>() {
                @Override
                public int compare(Framework o1, Framework o2) {
                    return Double.compare(o1.getStars(),o2.getStars());
                }
            });
            return;
        }
            frameworks.sort(new Comparator<Framework>() {
                @Override
                public int compare(Framework o1, Framework o2) {
                    return Double.compare(o2.getStars(),o1.getStars());
                }
            });
    }

    @Override
    public void orderByInteraction(List<Framework> frameworks, Integer order) {
        if(order<0){
            frameworks.sort(new Comparator<Framework>() {
                @Override
                public int compare(Framework o1, Framework o2) {
                    if(o1.getLastComment()==null && o2.getLastComment()==null)
                        return 0;
                    else if(o1.getLastComment()==null)
                        return -1;
                    else if(o2.getLastComment()==null)
                        return 1;
                    return o1.getLastComment().after(o2.getLastComment()) ? 1 : -1;
                }
            });
            return;
        }
        frameworks.sort(new Comparator<Framework>() {
            @Override
            public int compare(Framework o1, Framework o2) {
                if(o1.getLastComment()==null && o2.getLastComment()==null)
                    return 0;
                else if(o1.getLastComment()==null)
                    return 1;
                else if(o2.getLastComment()==null)
                    return -1;
                return o1.getLastComment().before(o2.getLastComment()) ? 1 : -1;
            }
        });
    }

    @Override
    public void orderByReleaseDate(List<Framework> frameworks, Integer order) {
        if(order>0){
            frameworks.sort(new Comparator<Framework>() {
                @Override
                public int compare(Framework o1, Framework o2) {
                    return o1.getPublish_date().after(o2.getPublish_date()) ? 1 : -1;
                }
            });
            return;
        }
        frameworks.sort(new Comparator<Framework>() {
            @Override
            public int compare(Framework o1, Framework o2) {
                return o1.getPublish_date().before(o2.getPublish_date()) ? 1 : -1;
            }
        });
    }

    @Override
    public void orderByCommentsAmount(List<Framework> frameworks, Integer order) {
        if(order<0){
            frameworks.sort(new Comparator<Framework>() {
                @Override
                public int compare(Framework o1, Framework o2) {
                    return o1.getCommentsAmount()-o2.getCommentsAmount();
                }
            });
            return;
        }
        frameworks.sort(new Comparator<Framework>() {
            @Override
            public int compare(Framework o1, Framework o2) {
                return o2.getCommentsAmount()-o1.getCommentsAmount();
            }
        });
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getByMultipleCategories(List<FrameworkCategories> categories) {
        return frameworkDao.getByMultipleCategories(categories);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getByMultipleTypes(List<FrameworkType> types) {
        return frameworkDao.getByMultipleTypes(types);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getByMinStars(int stars) {
        return frameworkDao.getByMinStars(stars);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getByUser(long userId, long page) {
        return frameworkDao.getByUser(userId, page, PAGESIZE);
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
    public List<Framework> getAll() {
        return frameworkDao.getAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Framework> getCompetitors(Framework framework) {
        List<Framework> toReturn = getByCategory(framework.getFrameCategory(), 1);
        toReturn.remove(framework);
        return toReturn.size() > 5 ? toReturn.subList(0,4) : toReturn;
    }

}
