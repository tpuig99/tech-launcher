package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.register.ProfileForm;
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

    public static ModelAndView redirectToProfile(String username) {
        return new ModelAndView("redirect:/users/" + username);
    }

    @RequestMapping(path={"/users/{username}"}, method = RequestMethod.GET)
    public ModelAndView userProfile(@PathVariable String username, @ModelAttribute("profileForm") final ProfileForm form) {
        ModelAndView mav = new ModelAndView("session/user_profile");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        if (us.findByUsername(username).isPresent()) {
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            long userId = user.getId();
            mav.addObject("profile", user);
            mav.addObject("previousDescription", user.getDescription());

            final List<Comment> commentList = commentService.getCommentsByUser(userId);
            final List<Content> contentList = contentService.getContentByUser(userId);
            final List<FrameworkVote> votesList = voteService.getAllByUser(userId);
            final List<Framework> frameworks = frameworkService.getByUser(userId);

            mav.addObject("verifiedList", us.getAllVerifyByUser(userId));
            mav.addObject("contents", contentList);
            mav.addObject("comments", commentList);
            mav.addObject("votes", votesList);
            mav.addObject("frameworks",frameworks);
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
            mav.addObject("isAdmin", user.isAdmin());
            mav.addObject("isAllowMod", user.isAllowMod());
            return mav;
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path = {"/users/{username}/upload"}, method = RequestMethod.POST)
    public ModelAndView uploadPicture(@RequestParam("picture") MultipartFile picture, @PathVariable String username) throws IOException {
        Optional<User> user = us.findByUsername(username);

        if (user.isPresent() && SecurityContextHolder.getContext().getAuthentication().getName().equals(user.get().getUsername())) {
            if (!picture.isEmpty()) {
                us.updatePicture(user.get().getId(), picture.getBytes());
            }

            return UserProfileController.redirectToProfile(user.get().getUsername());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/users/{username}"}, method = RequestMethod.POST)
    public ModelAndView editProfile(@Valid @ModelAttribute("profileForm") final ProfileForm form, final BindingResult errors, final RedirectAttributes redirectAttributes, @PathVariable String username) {

        Optional<User> user = us.findByUsername(username);

        if (user.isPresent() && SecurityContextHolder.getContext().getAuthentication().getName().equals(user.get().getUsername())) {
            if(errors.hasErrors()){
                final ModelAndView userError = userProfile(username, form);
                userError.addObject("profileFormError", true);
                userError.addObject("previousDescription", form.getDescription());
                return userError;
            }
            redirectAttributes.addFlashAttribute("profileFormError",false);
            redirectAttributes.addFlashAttribute("profileFormMessage","Your profile has been updated successfully!");

//            if (!form.getPicture().isEmpty()) {
//                us.updatePicture(user.get().getId(), form.getPicture().getBytes());
//            }

            us.updateDescription(user.get().getId(), form.getDescription());

            return UserProfileController.redirectToProfile(user.get().getUsername());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/user/{username}/enableMod/{value}"})
    public ModelAndView enableMod( @PathVariable("username") String username, @PathVariable("value") Boolean value){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(us.findByUsername(authentication.getName()).isPresent()){
            User user = us.findByUsername(authentication.getName()).get();
            if( username.equals(user.getUsername())){
                us.updateModAllow(user.getId(), value);
                return UserProfileController.redirectToProfile(username);
            }
        }

        return ErrorController.redirectToErrorView();

    }

}
