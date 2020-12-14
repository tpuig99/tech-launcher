package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.PostService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;


import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Path("explore")
@Component
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

    @Context
    private UriInfo uriInfo;

    private final long START_PAGE = 1;
    private final long TECHS_PAGE_SIZE = 24;
    private final long POSTS_PAGE_SIZE = 5;

    private String getMessageWithoutArguments(String code) {
        return messageSource.getMessage(code, Collections.EMPTY_LIST.toArray(), LocaleContextHolder.getLocale());
    }

    @GET
    public Response advancedSearch(@QueryParam("to_search") @DefaultValue("") String toSearch,
                                   @QueryParam("categories") final List<String> categories,
                                   @QueryParam("types") final List<String> types,
                                   @QueryParam("stars_left") final Integer starsLeft,
                                   @QueryParam("stars_right") final Integer starsRight,
                                   @QueryParam("name_flag") final boolean nameFlag,
                                   @QueryParam("order") final Integer order,
                                   @QueryParam("comment_ammount") final Integer commentAmount,
                                   @QueryParam("last_comment") final Integer lastComment,
                                   @QueryParam("last_update") final Integer lastUpdate,
                                   @QueryParam("techs_page") final Long page,
                                   @QueryParam("posts_page") final Long postsPage,
                                   @QueryParam("is_post") final boolean isPost){

        List<FrameworkCategories> categoriesList = new ArrayList<>();
        List<String> categoriesQuery = new ArrayList<>();
        List<FrameworkType> typesList = new ArrayList<>();
        List<String> typesQuery = new ArrayList<>();
        final int DAYS = 1, WEEK = 2, MONTH = 3, MONTHS = 4, YEAR = 5;
        SearchDTO search = new SearchDTO();

        LOGGER.info("Explore: Searching results for: {}", toSearch);
        LOGGER.info("Explore: Searching categories among: {}", categories);
        LOGGER.info("Explore: Searching types among: {}", types);
        LOGGER.info("Explore: Searching stars between: {} and {}", starsLeft, starsRight);
        LOGGER.info("Explore: Using search only by name: {}", nameFlag);
        LOGGER.info("Explore: Searching by comment amount = {}", commentAmount);

        if( categories.size() != 1 || !categories.get(0).equals("")) {
            for (String c : categories) {
                categoriesQuery.add(c);

                try {
                    categoriesList.add(FrameworkCategories.valueOf(c));
                } catch (Exception e){
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        }

        if( types.size() != 1 || !types.get(0).equals("")) {
            for (String c : types) {

                typesQuery.add(c);

                try {
                    typesList.add(FrameworkType.valueOf(c));
                } catch (Exception e){
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
            }
        }

        List<String> allCategories = fs.getAllCategories();
        List<String> allTypes = fs.getAllTypes();

        if(allCategories.contains(toSearch)){
            categoriesList.add(FrameworkCategories.valueOf(toSearch));
            toSearch="";
        } else if(allTypes.contains(toSearch)){
            toSearch="";
        }

        Date tscomment = null;
        Date tsUpdated = null;
        LocalDate dateComment = null;
        LocalDate dateUpdate = null;

        String dateCommentTranslation = "";
        if(lastComment!=null) {
            LOGGER.info("Explore: Searching 'last comment' according to criteria {}", lastComment);
            switch (lastComment) {
                case DAYS:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_days");
                    dateComment = LocalDate.now().minusDays(3);
                    break;
                case WEEK:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_week");
                    dateComment = LocalDate.now().minusWeeks(1);
                    break;
                case MONTH:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_month");
                    dateComment = LocalDate.now().minusMonths(1);
                    break;
                case MONTHS:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_months");
                    dateComment = LocalDate.now().minusMonths(3);
                    break;
                case YEAR:
                    dateCommentTranslation = getMessageWithoutArguments("explore.last_year");
                    dateComment = LocalDate.now().minusYears(1);
                    break;
            }
        }

        String dateUpdateTranslation = "";
        if(lastUpdate!=null) {
            LOGGER.info("Explore: Searching 'last update' according to criteria {}", lastUpdate);
            switch (lastUpdate) {
                case DAYS:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_days");
                    dateUpdate = LocalDate.now().minusDays(3);
                    break;
                case WEEK:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_week");
                    dateUpdate = LocalDate.now().minusWeeks(1);
                    break;
                case MONTH:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_month");
                    dateUpdate = LocalDate.now().minusMonths(1);
                    break;
                case MONTHS:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_months");
                    dateUpdate = LocalDate.now().minusMonths(3);
                    break;
                case YEAR:
                    dateUpdateTranslation = getMessageWithoutArguments("explore.last_year");
                    dateUpdate = LocalDate.now().minusYears(1);
                    break;
            }
        }
        if(dateComment!=null){
            tscomment= Date.from(dateComment.atStartOfDay().toInstant(ZoneOffset.UTC));
        }
        if(dateUpdate!=null){
            tsUpdated=Date.from(dateUpdate.atStartOfDay().toInstant(ZoneOffset.UTC));
        }

        search.setToSearch(toSearch);
        search.setCategories(categoriesQuery);
        search.setTypes(typesQuery);
        search.setStarsLeft(starsLeft);
        search.setStarsRight(starsRight);
        search.setNameFlag(nameFlag);
        search.setLastComment(lastComment);
        search.setLastUpdate(lastUpdate);
        search.setPost(isPost);

        Integer searchResultsNumber;
        if (order != null) {
            if( order < -4 || order > 4 ){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            search.setOrder((int) Math.signum(order));
            search.setSort(Math.abs(order));
        }  else {
            search.setOrder(1);
            search.setSort(0);
        }

       /* --------------------- TECHS --------------------- */
        if( !isPost ) {
            List<Framework> frameworks = fs.search(!toSearch.equals("") ? toSearch : null, categoriesList.isEmpty() ? null : categoriesList, typesList.isEmpty() ? null : typesList, starsLeft == null ? 0 : starsLeft, starsRight == null ? 5 : starsRight, nameFlag, commentAmount == null ? 0 : commentAmount, tscomment, tsUpdated, order, page == null ? START_PAGE : page);
            searchResultsNumber = fs.searchResultsNumber(!toSearch.equals("") ? toSearch : null, categoriesList.isEmpty() ? null : categoriesList, typesList.isEmpty() ? null : typesList, starsLeft == null ? 0 : starsLeft, starsRight == null ? 5 : starsRight, nameFlag, commentAmount == null ? 0 : commentAmount, tscomment, tsUpdated);
            LOGGER.info("Explore: Found {} matching techs", searchResultsNumber);

            search.setFrameworksAmount(searchResultsNumber);
            search.setFrameworks(frameworks.stream().map((Framework framework) -> FrameworkDTO.fromExtern(framework,uriInfo)).collect(Collectors.toList()));

        }
        /* -------------------------------------------------- */
        /* --------------------- POSTS --------------------- */
        else {
            List<String> tags = new ArrayList<>();
            tags.addAll(categories);
            tags.addAll(types);

            List<Post> posts = ps.search(!toSearch.equals("") ? toSearch : null, tags.isEmpty() ? null : tags, 0, 0, commentAmount == null ? 0 : commentAmount, tscomment, tsUpdated, order, postsPage == null ? START_PAGE : postsPage, POSTS_PAGE_SIZE);
            searchResultsNumber = ps.searchResultsNumber(!toSearch.equals("") ? toSearch : null, tags.isEmpty() ? null : tags, 0, 0, commentAmount == null ? 0 : commentAmount, tscomment, tsUpdated, order);
            LOGGER.info("Explore: Found {} matching posts", searchResultsNumber);

            search.setPosts(posts.stream().map(PostDTO::fromPost).collect(Collectors.toList()));
            search.setPostsAmount(searchResultsNumber);
        }
        /* -------------------------------------------------- */


        return Response.ok(search).build();
    }
}


