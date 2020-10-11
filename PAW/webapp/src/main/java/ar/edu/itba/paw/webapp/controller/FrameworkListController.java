package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.FrameworkType;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.TranslationService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class FrameworkListController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @Autowired
    private TranslationService ts;

    final long startPage = 1;
    final long PAGESIZE = 7;

    private String getMessageWithoutArguments(String code) {
        return messageSource.getMessage(code, Collections.EMPTY_LIST.toArray(), LocaleContextHolder.getLocale());
    }

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
                                       @RequestParam(required = false) final Integer lastUpdate,
                                       @RequestParam(value = "page", required = false) final Long page){

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

        String dateCommentTranslation = "";
        if(lastComment!=null) {
            switch (lastComment) {
                case 1:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_days");
                    dateComment = LocalDate.now().minusDays(3);
                    break;
                case 2:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_week");
                    dateComment = LocalDate.now().minusWeeks(1);
                    break;
                case 3:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_month");
                    dateComment = LocalDate.now().minusMonths(1);
                    break;
                case 4:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_months");
                    dateComment = LocalDate.now().minusMonths(3);
                    break;
                case 5:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_year");
                    dateComment = LocalDate.now().minusYears(1);
                    break;
            }
        }

        String dateUpdateTranslation = "";
        if(lastUpdate!=null) {
            switch (lastUpdate) {
                case 1:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_days");
                    dateUpdate = LocalDate.now().minusDays(3);
                    break;
                case 2:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_week");
                    dateUpdate = LocalDate.now().minusWeeks(1);
                    break;
                case 3:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_month");
                    dateUpdate = LocalDate.now().minusMonths(1);
                    break;
                case 4:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_months");
                    dateUpdate = LocalDate.now().minusMonths(3);
                    break;
                case 5:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_year");
                    dateUpdate = LocalDate.now().minusYears(1);
                    break;
            }
        }
        if(dateComment!=null){
            tscomment=Timestamp.valueOf(dateComment.atStartOfDay());
        }
        if(dateUpdate!=null){
            tsUpdated=Timestamp.valueOf(dateUpdate.atStartOfDay());
        }
        List<Framework> frameworks = fs.search(!toSearch.equals("") ? toSearch  : null, categoriesList.isEmpty() ? null : categoriesList ,typesList.isEmpty() ? null : typesList, starsLeft == null ? 0 : starsLeft,starsRight== null ? 5 : starsRight, nameFlag,commentAmount == null ? 0:commentAmount,tscomment,tsUpdated, page == null ? startPage:page);
        if(order!=null && order!=0){
            switch (Math.abs(order)){
                case 1:
                    fs.orderByStars(frameworks, order);
                    break;
                case 2:
                    fs.orderByCommentsAmount(frameworks, order);
                    break;
                case 3:
                    fs.orderByReleaseDate(frameworks, order);
                    break;
                case 4:
                    fs.orderByInteraction(frameworks, order);
                    break;
            }
        }

        
        mav.addObject("matchingFrameworks", frameworks);
        mav.addObject("page", page == null ? startPage:page);
        mav.addObject("page_size", PAGESIZE);
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("categories", allCategories );
        mav.addObject("categories_translated", ts.getAllCategories());
        mav.addObject("frameworkNames",fs.getFrameworkNames());
        mav.addObject("types", allTypes);
        mav.addObject("types_translated", ts.getAllTypes());
        mav.addObject("search_page", true);

        //Search Results For:
        mav.addObject("techNameQuery", toSearch );
        mav.addObject("categoriesQuery", categoriesQuery );
        mav.addObject("typesQuery", typesQuery );
        mav.addObject("starsQuery1", starsLeft );
        mav.addObject("starsQuery2", starsRight );
        mav.addObject("commentAmount", commentAmount);
        mav.addObject("nameFlagQuery", nameFlag);
        mav.addObject("dateComment",lastComment);
        mav.addObject("dateCommentTranslation", dateCommentTranslation);
        mav.addObject("dateUpdate",lastUpdate);
        mav.addObject("dateUpdateTranslation", dateUpdateTranslation);

        if (order != null) {
            mav.addObject("sortValue", Math.abs(order));
            mav.addObject("orderValue", (int) Math.signum(order));
        }  else {
            mav.addObject("sortValue", 0);
            mav.addObject("orderValue", 1);
        }


        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }

        return mav;
    }
}


