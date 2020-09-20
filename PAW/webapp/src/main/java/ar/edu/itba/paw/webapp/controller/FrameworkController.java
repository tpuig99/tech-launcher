package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.ContentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.Authenticator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class FrameworkController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private ContentService contentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private FrameworkVoteService frameworkVoteService;

    @Autowired
    private UserService us;

    public static ModelAndView redirectToFramework(Long id, String category) {
        return new ModelAndView("redirect:/" + category + "/" + id);
    }

    @RequestMapping("/{category}/{id}")
    public ModelAndView framework(@PathVariable long id, @PathVariable String category,@ModelAttribute("contentForm") final ContentForm form) {
        final ModelAndView mav = new ModelAndView("frameworks/framework");
        Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            List<Comment> comments = commentService.getCommentsByFramework(id);
            Map<Long, String> commentsUsernames = us.getUsernamesByComments(comments);
            Map<Long, List<Comment>> replies = commentService.getRepliesByFramework(id);

            mav.addObject("framework", framework.get());

            mav.addObject("books", contentService.getNotPendingContentByFrameworkAndType(id, ContentTypes.book));
            mav.addObject("courses", contentService.getNotPendingContentByFrameworkAndType(id, ContentTypes.course));
            mav.addObject("tutorials", contentService.getNotPendingContentByFrameworkAndType(id, ContentTypes.tutorial));
            mav.addObject("category", category);
            mav.addObject("competitors", fs.getCompetitors(framework.get()));
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
            mav.addObject("comments", comments);
            mav.addObject("commentsUsernames", us.getUsernamesByComments(comments));
            mav.addObject("replies", replies);

            return mav;
        }
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/create"}, method= RequestMethod.GET)
    public ModelAndView saveComment(@RequestParam("id") final long id, @RequestParam("content") final String content, @RequestParam(name="commentId", required= false) final Long commentId) throws UserAlreadyExistException {
        Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (us.findByUsername(authentication.getName()).isPresent()) {
                final Comment comment = commentService.insertComment(framework.get().getId(), us.findByUsername(authentication.getName()).get().getId(), content, commentId);
            }

            return FrameworkController.redirectToFramework(framework.get().getId(), framework.get().getCategory());
        }

        return ErrorController.redirectToErrorView();
    }


    @RequestMapping(path={"/voteup"}, method= RequestMethod.GET)
    public ModelAndView voteUpComment(@RequestParam("id") final long frameworkId, @RequestParam("comment_id") final long commentId) throws UserAlreadyExistException {
        Optional<Framework> framework = fs.findById(frameworkId);

        if (framework.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (us.findByUsername(authentication.getName()).isPresent()) {
                final Optional<Comment> comment = commentService.voteUp(commentId, us.findByUsername(authentication.getName()).get().getId());
            }

            return FrameworkController.redirectToFramework(framework.get().getId(), framework.get().getCategory());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/votedown"}, method= RequestMethod.GET)
    public ModelAndView voteDownComment(@RequestParam("id") final long frameworkId, @RequestParam("comment_id") final long commentId) throws UserAlreadyExistException {
        Optional<Framework> framework = fs.findById(frameworkId);

        if (framework.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (us.findByUsername(authentication.getName()).isPresent()) {
                final Optional<Comment> comment = commentService.voteDown(commentId, us.findByUsername(authentication.getName()).get().getId());
            }

            return FrameworkController.redirectToFramework(framework.get().getId(), framework.get().getCategory());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/rate"}, method = RequestMethod.GET)
    public ModelAndView rateComment(@RequestParam("id") final long id, @RequestParam("rating") final int rating) throws UserAlreadyExistException {
        Optional<Framework> framework = fs.findById(id);

        if (framework.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (us.findByUsername(authentication.getName()).isPresent()) {
                final FrameworkVote frameworkVote = frameworkVoteService.insert(id, us.findByUsername(authentication.getName()).get().getId(), rating);
            }
            return FrameworkController.redirectToFramework(framework.get().getId(), framework.get().getCategory());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/content"}, method = RequestMethod.POST)
    public ModelAndView addContent(@Valid @ModelAttribute("contentForm") final ContentForm form, final BindingResult errors, final RedirectAttributes redirectAttributes){

        long frameworkId = form.getFrameworkId();
        Optional<Framework> framework = fs.findById(frameworkId);

        if (framework.isPresent()) {
            if(errors.hasErrors()){
                // redirectAttributes.addFlashAttribute("contentFormMessage","Error.");
                //redirectAttributes.addFlashAttribute("contentFormError",true);

                final ModelAndView framework1 = framework(frameworkId, framework.get().getCategory(), form);
                framework1.addObject("contentFormError", true);
                return framework1;
                //return new ModelAndView("redirect:/" + framework.getCategory() + "/"+frameworkId);
            }
            redirectAttributes.addFlashAttribute("contentFormError",false);
            redirectAttributes.addFlashAttribute("contentFormMessage","Your content is now pending approval.");


            ContentTypes type = ContentTypes.valueOf(form.getType());

            final Content content = contentService.insertContent(frameworkId, 1, form.getTitle(), form.getLink(), type, true );

            return FrameworkController.redirectToFramework(framework.get().getId(), framework.get().getCategory());
        }
        return ErrorController.redirectToErrorView();
    }
}

