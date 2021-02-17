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
    final int DAYS = 1, WEEK = 2, MONTH = 3, MONTHS = 4, YEAR = 5;

    private String getMessageWithoutArguments(String code) {
        return messageSource.getMessage(code, Collections.EMPTY_LIST.toArray(), LocaleContextHolder.getLocale());
    }

    private Response pagination(UriInfo uriInfo,int page,int pages,Object dto){
        Response.ResponseBuilder response = Response.ok(dto)
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page",1).build(),"first")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page",pages).build(),"last");
        if(page < pages)
            response = response.link(uriInfo.getAbsolutePathBuilder().queryParam("page",page+1).build(),"next");
        if(page != 1)
            response = response.link(uriInfo.getAbsolutePathBuilder().queryParam("page",page-1).build(),"prev");
        return  response.build();
    }

    @GET
    @Path("techs")
    public Response advancedSearchTechs(@QueryParam("to_search") @DefaultValue("") String toSearch,
                                   @QueryParam("categories") final List<String> categories,
                                   @QueryParam("types") final List<String> types,
                                   @QueryParam("stars_left") final Integer starsLeft,
                                   @QueryParam("stars_right") final Integer starsRight,
                                   @QueryParam("name_flag") final boolean nameFlag,
                                   @QueryParam("order") final Integer order,
                                   @QueryParam("comment_amount") final Integer commentAmount,
                                   @QueryParam("last_comment") final Integer lastComment,
                                   @QueryParam("last_update") final Integer lastUpdate,
                                   @QueryParam("page") final int page){

        List<FrameworkCategories> categoriesList = new ArrayList<>();
        List<FrameworkType> typesList = new ArrayList<>();
        SearchDTO search = new SearchDTO();
        Integer searchResultsNumber;

        LOGGER.info("Explore: Searching results for: {}", toSearch);
        LOGGER.info("Explore: Searching categories among: {}", categories);
        LOGGER.info("Explore: Searching types among: {}", types);
        LOGGER.info("Explore: Searching stars between: {} and {}", starsLeft, starsRight);
        LOGGER.info("Explore: Using search only by name: {}", nameFlag);
        LOGGER.info("Explore: Searching by comment amount = {}", commentAmount);

        if (order != null && (order < -4 || order > 4)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(isExploringByMultiple(categories)) {
            try {
                categoriesList = parseCategories(categories);
            } catch (Exception e){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        if(isExploringByMultiple(types)) {
            try {
                typesList = parseTypes(types);
            } catch (Exception e){
                return Response.status(Response.Status.BAD_REQUEST).build();
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

        Date parsedLastComment = null;
        Date parsedLastUpdate = null;

        if (lastComment != null) {
            LOGGER.info("Explore: Searching 'last comment' according to criteria {}", lastComment);
            parsedLastComment = parseToDate(lastComment);
        }

        if (lastUpdate != null) {
            LOGGER.info("Explore: Searching 'last update' according to criteria {}", lastUpdate);
            parsedLastUpdate = parseToDate(lastUpdate);
        }

        setExploreParams(search, toSearch, categories, types, starsLeft, starsRight, nameFlag, lastComment, lastUpdate, order);

        /* --------------------- TECHS --------------------- */
        List<Framework> frameworks = fs.search(!toSearch.equals("") ? toSearch : null, categoriesList.isEmpty() ? null : categoriesList, typesList.isEmpty() ? null : typesList, starsLeft == null ? 0 : starsLeft, starsRight == null ? 5 : starsRight, nameFlag, commentAmount == null ? 0 : commentAmount, parsedLastComment, parsedLastUpdate, order, page == 0 ? 1 : page);
        searchResultsNumber = fs.searchResultsNumber(!toSearch.equals("") ? toSearch : null, categoriesList.isEmpty() ? null : categoriesList, typesList.isEmpty() ? null : typesList, starsLeft == null ? 0 : starsLeft, starsRight == null ? 5 : starsRight, nameFlag, commentAmount == null ? 0 : commentAmount, parsedLastComment, parsedLastUpdate);

        LOGGER.info("Explore: Found {} matching techs", searchResultsNumber);

        int pages = (int) Math.ceil(((double)searchResultsNumber)/TECHS_PAGE_SIZE);

        search.setFrameworksAmount(searchResultsNumber);
        search.setFrameworks(frameworks.stream().map((Framework framework) -> FrameworkDTO.fromExtern(framework,uriInfo)).collect(Collectors.toList()));

        return pagination(uriInfo, page == 0 ? 1 : page, pages, search);
        /* -------------------------------------------------- */
    }

    @GET
    @Path("posts")
    public Response advancedSearchPosts(@QueryParam("to_search") @DefaultValue("") String toSearch,
                                   @QueryParam("categories") final List<String> categories,
                                   @QueryParam("types") final List<String> types,
                                   @QueryParam("stars_left") final Integer starsLeft,
                                   @QueryParam("stars_right") final Integer starsRight,
                                   @QueryParam("name_flag") final boolean nameFlag,
                                   @QueryParam("order") final Integer order,
                                   @QueryParam("comment_amount") final Integer commentAmount,
                                   @QueryParam("last_comment") final Integer lastComment,
                                   @QueryParam("last_update") final Integer lastUpdate,
                                   @QueryParam("page") final int page) {


        List<String> categoriesQuery = new ArrayList<>();
        List<String> typesQuery = new ArrayList<>();
        Date parsedLastComment = null;
        Date parsedLastUpdate = null;
        SearchDTO search = new SearchDTO();
        Integer searchResultsNumber;
        List<String> tags = new ArrayList<>();

        LOGGER.info("Explore: Searching results for: {}", toSearch);
        LOGGER.info("Explore: Searching categories among: {}", categories);
        LOGGER.info("Explore: Searching types among: {}", types);
        LOGGER.info("Explore: Searching stars between: {} and {}", starsLeft, starsRight);
        LOGGER.info("Explore: Using search only by name: {}", nameFlag);
        LOGGER.info("Explore: Searching by comment amount = {}", commentAmount);

        if (order != null && (order < -4 || order > 4)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }



        setExploreParams(search, toSearch, categories, types, null, null, null, lastComment, lastUpdate, order);

        if (lastComment != null) {
            LOGGER.info("Explore: Searching 'last comment' according to criteria {}", lastComment);
            parsedLastComment = parseToDate(lastComment);
        }

        if (lastUpdate != null) {
            LOGGER.info("Explore: Searching 'last update' according to criteria {}", lastUpdate);
            parsedLastUpdate = parseToDate(lastUpdate);
        }

        tags = getTagsToExplore(categories, types);

        List<Post> posts = ps.search(!toSearch.equals("") ? toSearch : null, tags, 0, 0, commentAmount == null ? 0 : commentAmount, parsedLastComment, parsedLastUpdate, order, page == 0 ? 1 : page, POSTS_PAGE_SIZE);
        searchResultsNumber = ps.searchResultsNumber(!toSearch.equals("") ? toSearch : null, tags, 0, 0, commentAmount == null ? 0 : commentAmount, parsedLastComment, parsedLastUpdate, order);
        LOGGER.info("Explore: Found {} matching posts", searchResultsNumber);

        int pages = (int) Math.ceil(((double)searchResultsNumber)/POSTS_PAGE_SIZE);

        search.setPosts(posts.stream().map((Post post) -> PostDTO.fromPost(post, uriInfo)).collect(Collectors.toList()));
        search.setPostsAmount(searchResultsNumber);

        return pagination(uriInfo, page == 0 ? 1 : page, pages, search);

    }


    private Date parseToDate(Integer lastComment){
        LocalDate localDate = null;
        switch (lastComment) {
            case DAYS:
                localDate = LocalDate.now().minusDays(3);
                break;
            case WEEK:
                localDate = LocalDate.now().minusWeeks(1);
                break;
            case MONTH:
                localDate = LocalDate.now().minusMonths(1);
                break;
            case MONTHS:
                localDate = LocalDate.now().minusMonths(3);
                break;
            case YEAR:
                localDate = LocalDate.now().minusYears(1);
                break;
        }

        if(localDate != null){
            return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        }
        return null;

    }

   private void setExploreParams(SearchDTO searchDTO, String toSearch, List<String> categories, List<String> types, Integer starsLeft, Integer starsRight, Boolean nameFlag, Integer lastComment, Integer lastUpdate, Integer order){

        searchDTO.setToSearch(toSearch);
        searchDTO.setLastComment(lastComment);
        searchDTO.setLastUpdate(lastUpdate);

       List<String> parsedCategories = new ArrayList<>();
       List<String> parsedTypes = new ArrayList<>();

       if (categories.size() != 1 || !categories.get(0).equals("")) {
           parsedCategories.addAll(categories);
       }

       if (types.size() != 1 || !types.get(0).equals("")) {
           parsedTypes.addAll(types);
       }

       searchDTO.setCategories(parsedCategories);
       searchDTO.setTypes(parsedTypes);

       if (starsLeft != null) {
           searchDTO.setStarsLeft(starsLeft);
       }
       if (starsRight != null) {
           searchDTO.setStarsRight(starsRight);
       }
       if (nameFlag != null) {
           searchDTO.setNameFlag(nameFlag);
       }

       if (order != null) {
           searchDTO.setOrder((int) Math.signum(order));
           searchDTO.setSort(Math.abs(order));
       } else {
           searchDTO.setOrder(1);
           searchDTO.setSort(0);
       }
   }



   private boolean isOrderInvalid(Integer order){
        return  (order != null && (order < -4 || order > 4));
   }


   private List<FrameworkCategories> parseCategories(List<String> categories){
       List<FrameworkCategories> parsedCategories = new ArrayList<>();
       for (String c : categories) {
           parsedCategories.add(FrameworkCategories.valueOf(c));
       }
       return parsedCategories;
   }

    private List<FrameworkType> parseTypes(List<String> types){
        List<FrameworkType> parsedTypes = new ArrayList<>();
        for (String t : types) {
            parsedTypes.add(FrameworkType.valueOf(t));
        }
        return parsedTypes;
    }


    private boolean isExploringByMultiple(List<String> list){
        return (list.size() != 1 || !list.get(0).equals(""));
    }

    private List<String> getTagsToExplore(List<String> categories, List<String> types){
        List<String> tags = new ArrayList<>();
        tags.addAll(categories);
        tags.addAll(types);
        if(tags.isEmpty()){
            return null;
        }
        return tags;
    }
}


