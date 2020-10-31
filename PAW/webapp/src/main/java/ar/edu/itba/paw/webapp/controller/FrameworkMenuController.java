package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.service.TranslationService;
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

import java.util.List;
import java.util.Optional;

@Controller
public class FrameworkMenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkMenuController.class);

    @Autowired
    private FrameworkService fs;

    @Autowired
    private UserService us;

    @Autowired
    private TranslationService ts;

    private final String START_PAGE = "1";
    final private long PAGESIZE = 7;

    @RequestMapping("/{category}")
    public ModelAndView frameworkMenuPaging(@PathVariable String category,
                                            @RequestParam(value = "frameworks_page", required = false, defaultValue = START_PAGE) Long frameworksPage) {
        final ModelAndView mav = new ModelAndView("frameworks/frameworks_menu");

        final Optional<FrameworkCategories> enumCategory = Optional.ofNullable(FrameworkCategories.getByName(category));

        if (enumCategory.isPresent()) {
            LOGGER.info("Techs: Getting Techs by category '{}'", enumCategory.get().getNameCat());

            List<Framework> frameworks = fs.getByCategory(enumCategory.get(), frameworksPage);

            mav.addObject("category",category);
            mav.addObject("category_translation",ts.getCategory(category));
            mav.addObject("frameworksList", frameworks);
            mav.addObject("frameworks_page", frameworksPage);
            mav.addObject("page_size", PAGESIZE);
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            user.ifPresent(value -> mav.addObject("user_isMod", value.isVerify() || value.isAdmin()));

            return mav;
        }

        LOGGER.error("Techs: Requested category '{}' was invalid", category);
        return ErrorController.redirectToErrorView();
    }
}