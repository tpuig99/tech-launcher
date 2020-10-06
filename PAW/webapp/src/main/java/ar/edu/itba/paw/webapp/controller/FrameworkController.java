package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.framework.ContentForm;
import ar.edu.itba.paw.webapp.form.framework.ReportCommentForm;
import ar.edu.itba.paw.webapp.form.framework.ReportForm;
import ar.edu.itba.paw.webapp.form.FrameworkForm;
import ar.edu.itba.paw.webapp.form.frameworks.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.*;
import java.util.*;
import java.util.List;

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
    public ModelAndView framework(@PathVariable long id, @PathVariable String category, @ModelAttribute("contentForm") final ContentForm form, @ModelAttribute("reportForm") final ReportForm reportForm, @ModelAttribute("reportCommentForm") final ReportCommentForm reportCommentForm) {
        final ModelAndView mav = new ModelAndView("frameworks/framework");
        Optional<Framework> framework = fs.findById(id);
        mav.addObject("ratingForm", new RatingForm());
        mav.addObject("upVoteForm", new VoteForm());
        mav.addObject("downVoteForm", new DownVoteForm());
        mav.addObject("commentForm", new CommentForm());
        mav.addObject("replyForm", new ReplyForm());
        mav.addObject("deleteCommentForm", new DeleteCommentForm());
        mav.addObject("deleteContentForm", new DeleteContentForm());

        if (framework.isPresent()) {
            Map<Long, List<Comment>> replies = commentService.getRepliesByFramework(id);
            mav.addObject("framework", framework.get());

            mav.addObject("books", contentService.getContentByFrameworkAndType(id, ContentTypes.book));
            mav.addObject("courses", contentService.getContentByFrameworkAndType(id, ContentTypes.course));
            mav.addObject("tutorials", contentService.getContentByFrameworkAndType(id, ContentTypes.tutorial));
            mav.addObject("category", category);
            mav.addObject("competitors", fs.getCompetitors(framework.get()));
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());

            mav.addObject("replies", replies);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> optionalUser = us.findByUsername(username);
            if( optionalUser.isPresent()){
                User user = optionalUser.get();
                List<Comment> comments = commentService.getCommentsWithoutReferenceByFrameworkWithUser(id,user.getId());
                mav.addObject("comments",comments);
                mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
                mav.addObject("verifyForFramework", user.isVerifyForFramework(id));
                mav.addObject("isAdmin",user.isAdmin());
                mav.addObject("isEnable",user.isEnable());
                mav.addObject("allowMod",user.isAllowMod());
                Optional<FrameworkVote> fv = frameworkVoteService.getByFrameworkAndUser(id,user.getId());
                if(fv.isPresent()){
                    mav.addObject("stars",fv.get().getStars());
                }else{
                    mav.addObject("stars",0);
                }
            }
            else{
                List<Comment> comments = commentService.getCommentsWithoutReferenceByFrameworkWithUser(id,null);
                mav.addObject("comments",comments);
            }

            return mav;
        }
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/comment"}, method= RequestMethod.POST)
    public ModelAndView saveComment(@Valid @ModelAttribute("commentForm") final CommentForm form)   throws UserAlreadyExistException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (us.findByUsername(authentication.getName()).isPresent()) {
                final Comment comment = commentService.insertComment(form.getCommentFrameworkId(), us.findByUsername(authentication.getName()).get().getId(), form.getContent(), form.getCommentId());

            return FrameworkController.redirectToFramework(form.getCommentFrameworkId(), comment.getCategory());
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/reply"}, method= RequestMethod.POST)
    public ModelAndView replyComment(@Valid @ModelAttribute("replyForm") final ReplyForm form)   throws UserAlreadyExistException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (us.findByUsername(authentication.getName()).isPresent()) {
            final Comment comment = commentService.insertComment(form.getReplyFrameworkId(), us.findByUsername(authentication.getName()).get().getId(), form.getReplyContent(), form.getReplyCommentId());

            return FrameworkController.redirectToFramework(form.getReplyFrameworkId(), comment.getCategory());
        }

        return ErrorController.redirectToErrorView();
    }


    @RequestMapping(path={"/upvote"}, method= RequestMethod.POST)
    public ModelAndView voteUpComment(@Valid @ModelAttribute("upVoteForm") final VoteForm form) throws UserAlreadyExistException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (us.findByUsername(authentication.getName()).isPresent()) {
                final Optional<Comment> comment = commentService.voteUp(form.getCommentId(), us.findByUsername(authentication.getName()).get().getId());
                if(comment.isPresent()){
                    return FrameworkController.redirectToFramework(comment.get().getFrameworkId(), comment.get().getCategory());
                }
            }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/downvote"}, method= RequestMethod.POST)
    public ModelAndView voteDownComment(@Valid @ModelAttribute("downVoteForm") final DownVoteForm form) throws UserAlreadyExistException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (us.findByUsername(authentication.getName()).isPresent()) {
            final Optional<Comment> comment = commentService.voteDown(form.getDownVoteCommentId(), us.findByUsername(authentication.getName()).get().getId());
            if(comment.isPresent()){
                return FrameworkController.redirectToFramework(comment.get().getFrameworkId(), comment.get().getCategory());
            }
        }

        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/rate"}, method = RequestMethod.POST)
    public ModelAndView rateComment(@Valid @ModelAttribute("ratingForm") final RatingForm form, final BindingResult errors) throws UserAlreadyExistException {

        long frameworkId = form.getFrameworkId();
        Optional<Framework> framework = fs.findById(frameworkId);

        if (framework.isPresent()) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (us.findByUsername(authentication.getName()).isPresent()) {
                final FrameworkVote frameworkVote = frameworkVoteService.insert(frameworkId, us.findByUsername(authentication.getName()).get().getId(), form.getRating());
                return FrameworkController.redirectToFramework(frameworkId, frameworkVote.getCategory());
            }
            return ErrorController.redirectToErrorView();
        }
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping("/mod/quit")
    public ModelAndView QuitMod(@RequestParam("fId") final long fId,@RequestParam("category") final String cat) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(us.findByUsername(authentication.getName()).isPresent()){
            User user = us.findByUsername(authentication.getName()).get();
            for( VerifyUser verifyUser : user.getVerifications() ){
                if (verifyUser.getFrameworkId() == fId) {
                    us.deleteVerification(verifyUser.getVerificationId());
                    return FrameworkController.redirectToFramework(fId, cat);
                }
            }
            return ErrorController.redirectToErrorView();
        }
        return ErrorController.redirectToErrorView();
    }


    @RequestMapping(path={"/content"}, method = RequestMethod.POST)
    public ModelAndView addContent(@Valid @ModelAttribute("contentForm") final ContentForm form, final BindingResult errors, final RedirectAttributes redirectAttributes){

        long frameworkId = form.getFrameworkId();
        Optional<Framework> framework = fs.findById(frameworkId);

        if (framework.isPresent()) {

            if(errors.hasErrors()){
                final ModelAndView framework1 = framework(frameworkId, framework.get().getCategory(), form, new ReportForm(), new ReportCommentForm());
                framework1.addObject("contentFormError", true);
                framework1.addObject("ratingForm", new RatingForm());
                framework1.addObject("upVoteForm", new VoteForm());
                framework1.addObject("downVoteForm", new DownVoteForm());
                framework1.addObject("commentForm", new CommentForm());
                framework1.addObject("replyForm", new ReplyForm());
                framework1.addObject("deleteCommentForm", new DeleteCommentForm());
                framework1.addObject("deleteContentForm", new DeleteContentForm());
                return framework1;
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            redirectAttributes.addFlashAttribute("contentFormError",false);
            redirectAttributes.addFlashAttribute("contentFormMessage","Your content is now pending approval.");


            ContentTypes type = ContentTypes.valueOf(form.getType());
            if (us.findByUsername(authentication.getName()).isPresent()) {
                String pathToContent = form.getLink();
                if( !pathToContent.contains("://")){
                    pathToContent = "http://".concat(pathToContent);
                }
                final Content content = contentService.insertContent(frameworkId, us.findByUsername(authentication.getName()).get().getId(), form.getTitle(), pathToContent, type);
                return FrameworkController.redirectToFramework(frameworkId, framework.get().getCategory());
            }
            return ErrorController.redirectToErrorView();
        }
        return ErrorController.redirectToErrorView();
    }


    @RequestMapping(path={"/content/delete"}, method = RequestMethod.POST)
    public ModelAndView deleteComment(@Valid @ModelAttribute("deleteContentForm") final DeleteContentForm form, final BindingResult errors){
        Optional<Framework> framework = fs.findById(form.getDeleteContentFrameworkId());

        if (framework.isPresent()) {
            contentService.deleteContent(form.getDeleteContentId());

            return FrameworkController.redirectToFramework(form.getDeleteContentFrameworkId(), framework.get().getCategory());
        }
        return ErrorController.redirectToErrorView();

    }
    @RequestMapping(path={"/content/report"}, method = RequestMethod.POST)
    public ModelAndView reportContent(@Valid @ModelAttribute("reportForm")ReportForm form, final BindingResult errors, HttpServletRequest request){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
            Optional<Framework> framework = fs.findById(form.getReportFrameworkId());
            if( framework.isPresent() ) {
                contentService.addReport(form.getId(), user.getId(), form.getDescription());
                framework.get().getCategory();
                return FrameworkController.redirectToFramework(form.getReportFrameworkId(), framework.get().getCategory());
            }
        }
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/content/report/cancel"}, method = RequestMethod.PUT)
    public ModelAndView cancelReportContent(@RequestParam("id")long reportId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
            contentService.deleteReport(reportId);
        }
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/report/content/accept"}, method = RequestMethod.PUT)
    public ModelAndView acceptReportContent(@RequestParam("id")long reportId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
            contentService.acceptReport(reportId);
        }
        return ErrorController.redirectToErrorView();
    }
    @RequestMapping(path={"/report/content/deny"}, method = RequestMethod.PUT)
    public ModelAndView denyReportContent(@RequestParam("id")long reportId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
            contentService.denyReport(reportId);
        }
        return ErrorController.redirectToErrorView();
    }
    @RequestMapping(path={"/comment/report"}, method = RequestMethod.POST)
    public ModelAndView reportComment(@Valid @ModelAttribute("reportCommentForm")ReportCommentForm form, final BindingResult errors, HttpServletRequest request){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();

            Optional<Framework> framework = fs.findById(form.getReportCommentFrameworkId());
            if( framework.isPresent() ) {
                commentService.addReport(form.getReportCommentId(), user.getId(),form.getReportCommentDescription());
                framework.get().getCategory();
                return FrameworkController.redirectToFramework(form.getReportCommentFrameworkId(), framework.get().getCategory());
            }
        }
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/report/comment/accept"}, method = RequestMethod.PUT)
    public ModelAndView acceptReportComment(@RequestParam("id")long reportId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
            commentService.acceptReport(reportId);
        }
        return ErrorController.redirectToErrorView();
    }
    @RequestMapping(path={"/report/comment/deny"}, method = RequestMethod.PUT)
    public ModelAndView denyReportComment(@RequestParam("id")long reportId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
            commentService.denyReport(reportId);
        }
        return ErrorController.redirectToErrorView();
    }
    @RequestMapping(path={"/comment/report/cancel"}, method = RequestMethod.PUT)
    public ModelAndView cancelReportComment(@RequestParam("id")long reportId){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
            commentService.deleteReport(reportId);
        }
        return ErrorController.redirectToErrorView();
    }
    @RequestMapping(path={"/reports"}, method = RequestMethod.GET)
    public ModelAndView getReports(){
        Optional<User> userOptional = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if( userOptional.isPresent()){
            User user = userOptional.get();
                //acordate que admin solo puede ver comments y
                //para los de content tener el user.isverifyfor(frameworkid)
                List<ReportComment> reportComments = commentService.getAllReport();
                List<ReportContent> reportContents = contentService.getAllReports();

        }
        return ErrorController.redirectToErrorView();
    }


    @RequestMapping(path={"/comment/delete"}, method = RequestMethod.POST)
    public ModelAndView deleteComment(@Valid @ModelAttribute("deleteCommentForm") final DeleteCommentForm form, final BindingResult errors){
        Optional<Framework> framework = fs.findById(form.getCommentDeleteFrameworkId());

        if (framework.isPresent()) {
            int deleted = commentService.deleteComment(form.getCommentDeleteId());
            if (deleted != 1) {
                return ErrorController.redirectToErrorView();
            }

            return FrameworkController.redirectToFramework(form.getCommentDeleteFrameworkId(), framework.get().getCategory());
        }
        return ErrorController.redirectToErrorView();

    }
    @RequestMapping("/addFramewokr")
    public ModelAndView addFramework(@ModelAttribute("frameworkForm") final FrameworkForm form) {
        ModelAndView mav = new ModelAndView("session/...");
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if( us.findByUsername(username).isPresent()){
            User user = us.findByUsername(username).get();
            mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
        }
        return mav;
    }

    @RequestMapping(value = "/createFramework", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("frameworkForm") final FrameworkForm form, final BindingResult errors, HttpServletRequest request) {
        if (errors.hasErrors()) {
            return addFramework(form);
        }
        FrameworkType type = FrameworkType.getByName(form.getType());
        FrameworkCategories category = FrameworkCategories.getByName(form.getCategory());
        fs.create(form.getFrameworkName(),category,form.getDescription(),form.getIntroduction(),type,form.getUserId());
        return new ModelAndView("redirect:/...");
    }

}

