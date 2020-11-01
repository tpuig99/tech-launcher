package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.register.ProfileForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class UserProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    ContentService contentService;

    @Autowired
    FrameworkVoteService voteService;

    @Autowired
    FrameworkService frameworkService;

    @Autowired
    UserService us;

    final private long PAGE_SIZE = 5;
    final private long FRAMEWORK_PAGE_SIZE = 7;
    final private long VOTE_PAGE_SIZE = 10;
    final private String START_PAGE = "1";

    public static ModelAndView redirectToProfile(String username) {
        return new ModelAndView("redirect:/users/" + username);
    }

    @RequestMapping(path={"/users/{username}"}, method = RequestMethod.GET)
    public ModelAndView userProfilePagination(@PathVariable String username,
                                              @ModelAttribute("profileForm") final ProfileForm form,
                                              @RequestParam(value = "comments_page",  required = false, defaultValue = START_PAGE) final Long commentsPage,
                                              @RequestParam(value = "contents_page", required = false, defaultValue = START_PAGE) final Long contentsPage,
                                              @RequestParam(value = "votes_page", required = false, defaultValue = START_PAGE) final Long votesPage,
                                              @RequestParam(value = "frameworks_page", required = false, defaultValue = START_PAGE) final Long frameworksPage) {
        ModelAndView mav = new ModelAndView("session/user_profile");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

        final Optional<User> user = us.findByUsername(username);
        if (user.isPresent()) {
            LOGGER.info("User Profile: Requested user {} exists, retrieving data", username);

            long userId = user.get().getId();
            mav.addObject("profile", user.get());
            mav.addObject("previousDescription", user.get().getDescription());

            final List<Comment> commentList = commentService.getCommentsByUser(userId, commentsPage);
            final List<Content> contentList = contentService.getContentByUser(userId, contentsPage);
            final List<FrameworkVote> votesList = voteService.getAllByUser(userId, votesPage);
            final List<Framework> frameworks = frameworkService.getByUser(userId, frameworksPage);
            final Optional<Long> contentCount = contentService.getContentCountByUser(userId);
            final Optional<Integer> commentsCount = commentService.getCommentsCountByUser(userId);
            final Optional<Integer> votesCount = voteService.getAllCountByUser(userId);
            final Optional<Integer> frameworksCount = frameworkService.getByUserCount(userId);

            mav.addObject("verifiedList", us.getAllVerifyByUser(userId));
            mav.addObject("contents", contentList);
            mav.addObject("contents_page", contentsPage);
            contentCount.ifPresent(value -> mav.addObject("contentCount", value));
            mav.addObject("comments", commentList);
            mav.addObject("comments_page", commentsPage);
            commentsCount.ifPresent(value -> mav.addObject("commentsCount", value));
            mav.addObject("votes", votesList);
            mav.addObject("votes_page", votesPage);
            mav.addObject("votes_page_size",VOTE_PAGE_SIZE);
            votesCount.ifPresent(value -> mav.addObject("votesCount", value));
            mav.addObject("frameworks",frameworks);
            mav.addObject("frameworks_page", frameworksPage);
            mav.addObject("frameworks_page_size", FRAMEWORK_PAGE_SIZE);
            frameworksCount.ifPresent(value -> mav.addObject("frameworksCount", value));
            mav.addObject("page_size", PAGE_SIZE);
            mav.addObject("user_isMod", user.get().isVerify() || user.get().isAdmin());
            mav.addObject("isAdmin", user.get().isAdmin());
            mav.addObject("isAllowMod", user.get().isAllowMod());
            return mav;
        }

        LOGGER.error("User Profile: Requested user {} does not exist", username);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path = {"/users/{username}/upload"}, method = RequestMethod.POST)
    public ModelAndView uploadPicture(@RequestParam("picture") MultipartFile picture, @PathVariable String username) throws IOException {
        Optional<User> user = us.findByUsername(username);

        if (user.isPresent() && SecurityContextHolder.getContext().getAuthentication().getName().equals(user.get().getUsername())) {
            if (!picture.isEmpty()) {
                us.updatePicture(user.get().getId(), picture.getBytes());
                LOGGER.info("User Profile: User {} updated picture successfully",user.get().getId());
            } else {
                LOGGER.error("User Profile: Sent picture was unreadable");
            }

            return UserProfileController.redirectToProfile(user.get().getUsername());
        }

        LOGGER.error("User Profile: Unauthorized user attempted to update another profile");
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/users/{username}"}, method = RequestMethod.POST)
    public ModelAndView editProfile(@Valid @ModelAttribute("profileForm") final ProfileForm form, final BindingResult errors, final RedirectAttributes redirectAttributes, @PathVariable String username) {

        Optional<User> user = us.findByUsername(username);

        if (user.isPresent() && SecurityContextHolder.getContext().getAuthentication().getName().equals(user.get().getUsername())) {
            if(errors.hasErrors()){
                LOGGER.info("User Profile: Description Form por updating User {} profile has errors",user.get().getId());
                final ModelAndView userError = userProfilePagination(username, form,null,null,null,null);
                userError.addObject("profileFormError", true);
                userError.addObject("previousDescription", form.getDescription());
                return userError;
            }
            redirectAttributes.addFlashAttribute("profileFormError",false);
            redirectAttributes.addFlashAttribute("profileFormMessage","Your profile has been updated successfully!");

            us.updateDescription(user.get().getId(), form.getDescription());
            LOGGER.info("User Profile: User {} updated its description successfully",user.get().getId());
            return UserProfileController.redirectToProfile(user.get().getUsername());
        }
        LOGGER.error("User Profile: Unauthorized user attempted to update another profile");
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/user/{username}/enableMod/{value}"})
    public ModelAndView enableMod( @PathVariable("username") String username, @PathVariable("value") Boolean value){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(us.findByUsername(authentication.getName()).isPresent()){
            User user = us.findByUsername(authentication.getName()).get();
            if( username.equals(user.getUsername())){
                us.updateModAllow(user.getId(), value);
                LOGGER.info("User Profile: User {} updated its Mod Status successfully",user.getId());
                return UserProfileController.redirectToProfile(username);
            }
        }

        LOGGER.error("User Profile: Unauthorized user attempted to update another profile");
        return ErrorController.redirectToErrorView();

    }

}
