package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class FrameworkMenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkMenuController.class);

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;


    private final String START_PAGE = "1";
    private final long PAGE_SIZE = 7;

    @RequestMapping("/techs/{category}")
    public ModelAndView frameworkMenuPaging(@PathVariable String category,
                                            @RequestParam(value = "frameworks_page", required = false, defaultValue = START_PAGE) Long frameworksPage) {
        final ModelAndView mav = new ModelAndView("frameworks/frameworks_menu");

        final FrameworkCategories enumCategory = FrameworkCategories.valueOf(category);

        LOGGER.info("Techs: Getting Techs by category '{}'", enumCategory.name());

        mav.addObject("category",enumCategory);
//            mav.addObject("category_translation",ts.getCategory(category));
        mav.addObject("frameworksList", fs.getByCategory(enumCategory, frameworksPage));
        mav.addObject("frameworks_page", frameworksPage);
        mav.addObject("framework_amount",fs.getAmountByCategory(enumCategory));
        mav.addObject("page_size", PAGE_SIZE);
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        mav.addObject("categories_sidebar", fs.getAllCategories());

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));

        return mav;

    }
}