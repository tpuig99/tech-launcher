package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.PostService;
import ar.edu.itba.paw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class ExploreController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExploreController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @Autowired
    private PostService ps;

    private final long START_PAGE = 1;
    private final long TECHS_PAGE_SIZE = 24;
    private final long POSTS_PAGE_SIZE = 5;

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
                                       @RequestParam(value = "page", required = false) final Long page,
                                       @RequestParam(value = "postsPage", required = false) final Long postsPage,
                                       @RequestParam(required = false) final boolean isPost){

        final ModelAndView mav = new ModelAndView("frameworks/explore");
        List<FrameworkCategories> categoriesList = new ArrayList<>();
        List<String> categoriesQuery = new ArrayList<>();
        List<FrameworkType> typesList = new ArrayList<>();
        List<String> typesQuery = new ArrayList<>();

        LOGGER.info("Explore: Searching results for: {}", toSearch);
        LOGGER.info("Explore: Searching categories among: {}", categories);
        LOGGER.info("Explore: Searching types among: {}", types);
        LOGGER.info("Explore: Searching stars between: {} and {}", starsLeft, starsRight);
        LOGGER.info("Explore: Using search only by name: {}", nameFlag);
        LOGGER.info("Explore: Searching by comment amount = {}", commentAmount);

        for( String c : categories){
            categoriesQuery.add(c);
            categoriesList.add(FrameworkCategories.valueOf(c));
        }

        for( String c : types){
            typesQuery.add(c);
            typesList.add(FrameworkType.valueOf(c));
        }

        List<String> allCategories = fs.getAllCategories();
        List<String> allTypes = fs.getAllTypes();

        if(allCategories.contains(toSearch)){
            categoriesList.add(FrameworkCategories.valueOf(toSearch));
            toSearch="";
        } else if(allTypes.contains(toSearch)){
            toSearch="";
        }

        Timestamp tscomment = null;
        Timestamp tsUpdated = null;
        LocalDate dateComment = null;
        LocalDate dateUpdate = null;

        String dateCommentTranslation = "";
        if(lastComment!=null) {
            LOGGER.info("Explore: Searching 'last comment' according to criteria {}", lastComment);
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
            LOGGER.info("Explore: Searching 'last update' according to criteria {}", lastUpdate);
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

       /* --------------------- TECHS --------------------- */

        List<Framework> frameworks = fs.search(!toSearch.equals("") ? toSearch  : null, categoriesList.isEmpty() ? null : categoriesList ,typesList.isEmpty() ? null : typesList, starsLeft == null ? 0 : starsLeft,starsRight== null ? 5 : starsRight, nameFlag,commentAmount == null ? 0:commentAmount,tscomment,tsUpdated, order,page == null ? START_PAGE :page);
        Integer searchResultsNumber = fs.searchResultsNumber(!toSearch.equals("") ? toSearch  : null, categoriesList.isEmpty() ? null : categoriesList ,typesList.isEmpty() ? null : typesList, starsLeft == null ? 0 : starsLeft,starsRight== null ? 5 : starsRight, nameFlag,commentAmount == null ? 0:commentAmount,tscomment,tsUpdated);
        LOGGER.info("Explore: Found {} matching results", searchResultsNumber);

        mav.addObject("matchingFrameworks", frameworks);
        mav.addObject("page", page == null ? START_PAGE :page);
        mav.addObject("page_size", TECHS_PAGE_SIZE);
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("categories", allCategories);
//        mav.addObject("categories_translated", ts.getAllCategories());
        mav.addObject("frameworkNames",fs.getFrameworkNames());
        mav.addObject("types", allTypes);
//        mav.addObject("types_translated", ts.getAllTypes());
        mav.addObject("search_page", true);
        mav.addObject("searchResultsNumber", searchResultsNumber);

        //Search Results For TECHS :
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


        /* --------------------- POSTS --------------------- */

       // mav.addObject("posts", ps.getAll(postsPage == null ? 1 : postsPage, POSTS_PAGE_SIZE) );
        mav.addObject("postsPage", postsPage);
        mav.addObject("postsPageSize", POSTS_PAGE_SIZE);
        mav.addObject("postsAmount", ps.getPostsAmount());
        mav.addObject("isPost", isPost);


        List<String> tags = new ArrayList<>();
        tags.addAll(categories);
        tags.addAll(types);

        List<Post> posts = ps.search(!toSearch.equals("") ? toSearch  : null,tags.isEmpty() ? null : tags,0,0,commentAmount == null ? 0:commentAmount,tscomment,tsUpdated,order,postsPage == null ? START_PAGE :postsPage, POSTS_PAGE_SIZE);
        mav.addObject("postsResults", posts.size());
        mav.addObject("posts", posts);

        /* -------------------------------------------------- */

        if (order != null) {
            mav.addObject("sortValue", Math.abs(order));
            mav.addObject("orderValue", (int) Math.signum(order));
        }  else {
            mav.addObject("sortValue", 0);
            mav.addObject("orderValue", 1);
        }


        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));

        return mav;
    }
}


