package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.ContentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/{category}/{id}")
    public ModelAndView framework(@PathVariable long id, @PathVariable String category,@ModelAttribute("contentForm") final ContentForm form) {
        final ModelAndView mav = new ModelAndView("frameworks/framework");
        Framework framework = fs.findById(id);
        List<Comment> comments = commentService.getCommentsByFramework(id);
        Map<Long, String> commentsUsernames = us.getUsernamesByComments(comments);


        mav.addObject("framework", framework);

        mav.addObject("books", contentService.getNotPendingContentByFrameworkAndType(id, ContentTypes.book));
        mav.addObject("courses", contentService.getNotPendingContentByFrameworkAndType(id, ContentTypes.course));
        mav.addObject("tutorials", contentService.getNotPendingContentByFrameworkAndType(id, ContentTypes.tutorial));
        mav.addObject("category", category);
        mav.addObject("competitors", fs.getCompetitors(framework));

        mav.addObject("comments", comments);
        mav.addObject("commentsUsernames", us.getUsernamesByComments(comments));

       // mav.addObject("contentFormError", false);

        return mav;
    }

    @RequestMapping(path={"/create"}, method= RequestMethod.GET)
    public ModelAndView saveComment(@RequestParam("id") final long id, @RequestParam("content") final String content, @RequestParam("username") final String username, @RequestParam("email") final String email) throws UserAlreadyExistException {
        Framework framework = fs.findById(id);
        final User user = us.create(username, email);
        final Comment comment = commentService.insertComment(framework.getId(),user.getId(),content,1);

        return new ModelAndView("redirect:/" + framework.getCategory() + "/"+framework.getId());
    }

    @RequestMapping(path={"/voteup"}, method= RequestMethod.GET)
    public ModelAndView voteUpComment(@RequestParam("id") final long frameworkId, @RequestParam("comment_id") final long commentId, @RequestParam("username") final String username, @RequestParam("email") final String email) throws UserAlreadyExistException {
        Framework framework = fs.findById(frameworkId);
        final User user = us.create(username, email);
        final Comment comment = commentService.voteUp(commentId,user.getId());
        return new ModelAndView("redirect:/" + framework.getCategory() + "/" + framework.getId());
    }

    @RequestMapping(path={"/votedown"}, method= RequestMethod.GET)
    public ModelAndView voteDownComment(@RequestParam("id") final long frameworkId, @RequestParam("comment_id") final long commentId, @RequestParam("username") final String username, @RequestParam("email") final String email) throws UserAlreadyExistException {
        Framework framework = fs.findById(frameworkId);
        final User user = us.create(username, email);
        final Comment comment = commentService.voteDown(commentId,user.getId());
        return new ModelAndView("redirect:/" + framework.getCategory() + "/"+framework.getId());
    }

    @RequestMapping(path={"/rate"}, method = RequestMethod.GET)
    public ModelAndView rateComment(@RequestParam("id") final long id, @RequestParam("rating") final int rating, @RequestParam("username") final String username, @RequestParam("email") final String email) throws UserAlreadyExistException {
        Framework framework = fs.findById(id);
        final User user = us.create(username, email);
        final FrameworkVote frameworkVote = frameworkVoteService.insert(id,user.getId(),rating);

        return new ModelAndView("redirect:/" + framework.getCategory() + "/"+id);
    }

    @RequestMapping(path={"/content"}, method = RequestMethod.POST)
    public ModelAndView addContent(@Valid @ModelAttribute("contentForm") final ContentForm form, final BindingResult errors, final RedirectAttributes redirectAttributes){

        long frameworkId = form.getFrameworkId();
        Framework framework = fs.findById(frameworkId);


        if(errors.hasErrors()){
           // redirectAttributes.addFlashAttribute("contentFormMessage","Error.");
            //redirectAttributes.addFlashAttribute("contentFormError",true);

            final ModelAndView framework1 = framework(frameworkId, framework.getCategory(), form);
            framework1.addObject("contentFormError", true);
            return framework1;
            //return new ModelAndView("redirect:/" + framework.getCategory() + "/"+frameworkId);
        }
        redirectAttributes.addFlashAttribute("contentFormError",false);
        redirectAttributes.addFlashAttribute("contentFormMessage","Your content is now pending approval.");


        ContentTypes type = ContentTypes.valueOf(form.getType());

        final Content content = contentService.insertContent(frameworkId, 1, form.getTitle(), form.getLink(), type, true );

        return new ModelAndView("redirect:/" + framework.getCategory() + "/"+frameworkId);
    }
}

