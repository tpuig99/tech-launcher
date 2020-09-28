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

import java.util.ArrayList;
import java.util.List;

@Controller
public class FrameworkListController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @RequestMapping(path = {"/search"}, method = RequestMethod.GET)
    public ModelAndView advancedSearch(@RequestParam(required = false) final String toSearch,
                                       @RequestParam(required = false) final List<String> categories,
                                       @RequestParam(required = false) final List<String> types,
                                       @RequestParam(required = false) final Integer stars,
                                       @RequestParam(required = false) final Integer order){

        final ModelAndView mav = new ModelAndView("frameworks/frameworks_list");
        List<FrameworkCategories> categoriesList = new ArrayList<>();
        List<String> categoriesQuery = new ArrayList<>();
        List<FrameworkType> typesList = new ArrayList<>();
        List<String> typesQuery = new ArrayList<>();

        if(categories!=null){
            for( String c : categories){
                String aux = c.replaceAll("%20", " ");
                categoriesQuery.add(aux);
                categoriesList.add(FrameworkCategories.getByName(aux));
            }
        }

        if(types!=null){
            for( String c : types){
                String aux = c.replaceAll("%20", " ");
                typesQuery.add(aux);
                typesList.add(FrameworkType.getByName(aux));
            }
        }

        mav.addObject("matchingFrameworks", fs.search(toSearch, categoriesList ,typesList,stars,order));
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("categories", FrameworkCategories.getAllCategories());
        mav.addObject("types", FrameworkType.getAllTypes());

        //Search Results For:
        mav.addObject("techNameQuery", toSearch );
        mav.addObject("categoriesQuery", categoriesQuery );
        mav.addObject("typesQuery", typesQuery );
        mav.addObject("starsQuery", stars );
        mav.addObject("orderQuery", order );

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }

        return mav;
    }
}


