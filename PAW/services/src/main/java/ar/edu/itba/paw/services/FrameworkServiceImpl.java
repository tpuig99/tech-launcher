package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.FrameworkDao;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.FrameworkVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Optional<Framework> findById(long id) {
        return frameworkDao.findById(id);
    }

    @Override
    public List<String> getFrameworkNames() {
        return frameworkDao.getFrameworkNames();
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
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer stars,boolean nameFlag) {
        return frameworkDao.search(toSearch,categories,types,stars,nameFlag);
    }

    @Override
    public void orderByStars(List<Framework> frameworks, Integer order) {
        if(order>0){
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
        if(order<0){
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
    public List<Framework> getByUser(long userId) {
        return frameworkDao.getByUser(userId);
    }

    @Override
    public Optional<Framework> create(String name, FrameworkCategories category, String description, String introduction, FrameworkType type,long userId, byte[] picture) {
        return frameworkDao.create(name,category,description,introduction,type,userId, picture);
    }

    @Override
    public List<Framework> getBestRatedFrameworks() {
        List<Framework> toReturn = frameworkDao.getByMinStars(4);
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
