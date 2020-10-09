package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.FrameworkType;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class FrameworkListController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @RequestMapping(path = {"/search"}, method = RequestMethod.GET)
    public ModelAndView advancedSearch(@RequestParam(required = false, defaultValue = "") String toSearch,
                                       @RequestParam(required = false, defaultValue = "") final List<String> categories,
                                       @RequestParam(required = false, defaultValue = "") final List<String> types,
                                       @RequestParam(required = false) final Integer starsLeft,
                                       @RequestParam(required = false) final Integer starsRight,
                                       @RequestParam(required = false) final boolean nameFlag,
                                       @RequestParam(required = false) final Integer order,
                                       @RequestParam(required = false) final Integer commentAmount,
                                       @RequestParam(required = false) final Integer lastComment,
                                       @RequestParam(required = false) final Integer lastUpdate){

        final ModelAndView mav = new ModelAndView("frameworks/frameworks_list");
        List<FrameworkCategories> categoriesList = new ArrayList<>();
        List<String> categoriesQuery = new ArrayList<>();
        List<FrameworkType> typesList = new ArrayList<>();
        List<String> typesQuery = new ArrayList<>();

        for( String c : categories){
            String aux = c.replaceAll("%20", " ");
            categoriesQuery.add(aux);
            categoriesList.add(FrameworkCategories.getByName(aux));
        }


        for( String c : types){
            String aux = c.replaceAll("%20", " ");
            typesQuery.add(aux);
            typesList.add(FrameworkType.getByName(aux));
        }

        List<String> allCategories = FrameworkCategories.getAllCategories();
        List<String> allTypes = FrameworkType.getAllTypes();

        if(allCategories.contains(toSearch)){
            categoriesList.add(FrameworkCategories.getByName(toSearch));
            toSearch="";
        } else if(allTypes.contains(toSearch)){
            typesList.contains(toSearch);
            toSearch="";
        }
        Timestamp tscomment = null;
        Timestamp tsUpdated = null;
        LocalDate dateComment = null;
        LocalDate dateUpdate = null;
        if(lastComment!=null) {
            switch (lastComment) {
                case 1: {
                    dateComment = LocalDate.now().minusDays(3);
                    break;
                }
                case 2: {
                    dateComment = LocalDate.now().minusWeeks(1);
                    break;
                }
                case 3: {
                    dateComment = LocalDate.now().minusMonths(1);
                    break;
                }
                case 4: {
                    dateComment = LocalDate.now().minusMonths(3);
                    break;
                }
                case 5: {
                    dateComment = LocalDate.now().minusYears(1);
                    break;
                }
            }
        }
        if(lastUpdate!=null) {
            switch (lastUpdate) {
                case 1: {
                    dateUpdate = LocalDate.now().minusDays(3);
                    break;
                }
                case 2: {
                    dateUpdate = LocalDate.now().minusWeeks(1);
                    break;
                }
                case 3: {
                    dateUpdate = LocalDate.now().minusMonths(1);
                    break;
                }
                case 4: {
                    dateUpdate = LocalDate.now().minusMonths(3);
                    break;
                }
                case 5: {
                    dateUpdate = LocalDate.now().minusYears(1);
                    break;
                }
            }
        }
        if(dateComment!=null){
            tscomment=Timestamp.valueOf(dateComment.atStartOfDay());
        }
        if(dateUpdate!=null){
            tsUpdated=Timestamp.valueOf(dateUpdate.atStartOfDay());
        }
        List<Framework> frameworks = fs.search(!toSearch.equals("") ? toSearch  : null, categoriesList.isEmpty() ? null : categoriesList ,typesList.isEmpty() ? null : typesList, starsLeft == null ? 0 : starsLeft,starsRight== null ? 5 : starsRight, nameFlag,commentAmount == null ? 0:commentAmount,tscomment,tsUpdated);
        if(order!=null && order!=0){
            switch (order){
                case -1:
                case 1: {
                    fs.orderByStars(frameworks, order);
                    break;
                }
                case 2:
                case -2: {
                    fs.orderByCommentsAmount(frameworks, order);
                    break;
                }
                case 3:
                case -3: {
                    fs.orderByReleaseDate(frameworks, order);
                    break;
                }
                case 4:
                case -4: {
                    fs.orderByInteraction(frameworks, order);
                    break;
                }
            }
        }

        
        mav.addObject("matchingFrameworks", frameworks);
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("categories", allCategories );
        mav.addObject("frameworkNames",fs.getFrameworkNames());
        mav.addObject("types", allTypes);

        //Search Results For:
        mav.addObject("techNameQuery", toSearch );
        mav.addObject("categoriesQuery", categoriesQuery );
        mav.addObject("typesQuery", typesQuery );
        mav.addObject("starsQuery1", starsLeft );
        mav.addObject("starsQuery2", starsRight );
        mav.addObject("commentAmount", commentAmount);
        mav.addObject("orderQuery", order );
        mav.addObject("nameFlagQuery", nameFlag);
        mav.addObject("dateComment",lastComment);
        mav.addObject("dateUpdate",lastUpdate);
        mav.addObject("selectOrder",order);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }

        return mav;
    }
}


