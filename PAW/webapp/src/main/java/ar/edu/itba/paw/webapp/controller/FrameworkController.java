package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.framework.*;
import ar.edu.itba.paw.webapp.form.frameworks.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class FrameworkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrameworkController.class);

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

    @Autowired
    private MessageSource messageSource;

    private final String START_PAGE = "1";
    private final long PAGE_SIZE = 5;

    public static ModelAndView redirectToFramework(Long id, String category) {
        return new ModelAndView("redirect:/techs/" + category + "/" + id);
    }

    private void loadForms(ModelAndView mav) {
        mav.addObject("ratingForm", new RatingForm());
        mav.addObject("upVoteForm", new VoteForm());
        mav.addObject("downVoteForm", new DownVoteForm());
        mav.addObject("commentForm", new CommentForm());
        mav.addObject("replyForm", new ReplyForm());
        mav.addObject("deleteCommentForm", new DeleteCommentForm());
        mav.addObject("deleteContentForm", new DeleteContentForm());
        mav.addObject("contentForm", new ContentForm());
        mav.addObject("reportForm", new ReportForm());
        mav.addObject("reportCommentForm", new ReportCommentForm());
        mav.addObject("deleteFrameworkForm", new DeleteFrameworkForm());
    }

    @RequestMapping("/techs/{category}/{id}")
    public ModelAndView framework(@PathVariable long id,
                                  @PathVariable String category,
                                  @RequestParam(value = "books_page", required = false, defaultValue = START_PAGE) Long booksPage,
                                  @RequestParam(value = "courses_page", required = false, defaultValue = START_PAGE) Long coursesPage,
                                  @RequestParam(value = "tutorials_page", required = false, defaultValue = START_PAGE) Long tutorialsPage,
                                  @RequestParam(value = "comments_page", required = false, defaultValue = START_PAGE) Long commentsPage) {

        final ModelAndView mav = new ModelAndView("frameworks/framework");
        Optional<Framework> framework = fs.findById(id);

        loadForms(mav);

        if (framework.isPresent()) {
            LOGGER.info("Tech {}: Requested and found, retrieving data", id);
            mav.addObject("framework", framework.get());
//            mav.addObject("category_translated", ts.getCategory(framework.get().getCategory().name()));
//            mav.addObject("type_translated", ts.getType(framework.get().getType().name()));

            mav.addObject("books", contentService.getContentByFrameworkAndType(id, ContentTypes.book, booksPage));
            mav.addObject("books_page", booksPage);
            mav.addObject("courses", contentService.getContentByFrameworkAndType(id, ContentTypes.course, coursesPage));
            mav.addObject("courses_page", coursesPage);
            mav.addObject("tutorials", contentService.getContentByFrameworkAndType(id, ContentTypes.tutorial, tutorialsPage));
            mav.addObject("tutorials_page", tutorialsPage);
            mav.addObject("page_size", PAGE_SIZE);
            mav.addObject("category", category);
            mav.addObject("competitors", fs.getCompetitors(framework.get()));
            mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
            mav.addObject("comments_page", commentsPage);


            final Optional<User> optionalUser = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            List<Comment> comments = commentService.getCommentsWithoutReferenceByFramework(id, commentsPage);
            mav.addObject("comments",comments);
            mav.addObject("categories_sidebar", fs.getAllCategories());

            if( optionalUser.isPresent()){
                User user = optionalUser.get();

                mav.addObject("user_isMod", user.isVerify() || user.isAdmin());
                mav.addObject("verifyForFramework", user.isVerifyForFramework(id));
                mav.addObject("isAdmin",user.isAdmin());
                mav.addObject("isEnable",user.isEnable());
                mav.addObject("allowMod",user.isAllowMod());
                mav.addObject("isOwner", framework.get().getAuthor().getUsername().equals(user.getUsername()));
                mav.addObject("hasAppliedToFramework",user.hasAppliedToFramework(framework.get().getId()));
                Optional<FrameworkVote> fv = user.getVoteForFramework(id);

                if(fv.isPresent()){
                    mav.addObject("stars",fv.get().getStars());
                }else{
                    mav.addObject("stars",0);
                }
            }

            return mav;
        }
        LOGGER.error("Tech {}: Requested and not found", id);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/techs/{category}/{id}/image"}, method = RequestMethod.GET)
    public @ResponseBody byte[] getImage(@PathVariable long id,
                                         @PathVariable String category) throws IOException {
        Optional<Framework> framework = fs.findById(id);
        return framework.map(Framework::getPicture).orElse(null);
    }

    @RequestMapping(path={"/comment"}, method= RequestMethod.POST)
    public ModelAndView saveComment(@Valid @ModelAttribute("commentForm") final CommentForm form) {

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isPresent()) {
            final Comment comment = commentService.insertComment(form.getCommentFrameworkId(), user.get().getId(), form.getComment(), form.getCommentId());
            LOGGER.info("Tech {}: User {} inserted a comment", form.getCommentFrameworkId(), user.get().getId());
            return FrameworkController.redirectToFramework(form.getCommentFrameworkId(), comment.getCategory());
        }

        LOGGER.error("Tech {}: Unauthorized user tried to insert a comment", form.getCommentFrameworkId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/reply"}, method= RequestMethod.POST)
    public ModelAndView replyComment(@Valid @ModelAttribute("replyForm") final ReplyForm form) {

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isPresent()) {
            final Comment comment = commentService.insertComment(form.getReplyFrameworkId(), user.get().getId(), form.getReplyComment(), form.getReplyCommentId());
            LOGGER.info("Tech {}: User {} replied comment {}", form.getReplyFrameworkId(), user.get().getId(), form.getReplyCommentId());
            return FrameworkController.redirectToFramework(form.getReplyFrameworkId(), comment.getCategory());
        }

        LOGGER.error("Tech {}: Unauthorized user tried to reply comment {}", form.getReplyFrameworkId(), form.getReplyCommentId());
        return ErrorController.redirectToErrorView();
    }


    @RequestMapping(path={"/upvote"}, method= RequestMethod.POST)
    public ModelAndView voteUpComment(@Valid @ModelAttribute("upVoteForm") final VoteForm form) {

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isPresent()) {
            final Optional<CommentVote> commentVote = commentService.vote(form.getCommentId(), user.get().getId(),1);
            LOGGER.info("Tech {}: User {} upvoted comment {}", form.getFrameworkId(), user.get().getId(), form.getCommentId());
            final Optional<Framework> fw = fs.findById(form.getFrameworkId());
            if( fw.isPresent()) {
                return FrameworkController.redirectToFramework(fw.get().getId(), fw.get().getCategory().name());
            }
            LOGGER.error("Tech {}: Tech doesn't exist", form.getFrameworkId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: Unauthorized user tried to upvote comment {}", form.getFrameworkId(), form.getCommentId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/downvote"}, method= RequestMethod.POST)
    public ModelAndView voteDownComment(@Valid @ModelAttribute("downVoteForm") final DownVoteForm form) {

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isPresent()) {
            final Optional<CommentVote> commentVote = commentService.vote(form.getDownVoteCommentId(), user.get().getId(),-1);
            LOGGER.info("Tech {}: User {} downvoted comment {}", form.getDownVoteFrameworkId(), user.get().getId(), form.getDownVoteCommentId());
            final Optional<Framework> fw = fs.findById(form.getDownVoteFrameworkId());
            if( fw.isPresent()) {
                return FrameworkController.redirectToFramework(fw.get().getId(), fw.get().getCategory().name());
            }
            LOGGER.error("Tech {}: Tech doesn't exist", form.getDownVoteFrameworkId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: Unauthorized user tried to downvote comment {}", form.getDownVoteFrameworkId(), form.getDownVoteCommentId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/rate"}, method = RequestMethod.POST)
    public ModelAndView rateComment(@Valid @ModelAttribute("ratingForm") final RatingForm form, final BindingResult errors) {

        long frameworkId = form.getFrameworkId();
        final Optional<Framework> framework = fs.findById(frameworkId);

        if (framework.isPresent()) {

            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isPresent()) {
                final FrameworkVote frameworkVote = frameworkVoteService.insert(framework.get(), user.get().getId(), form.getRating());
                LOGGER.info("Tech {}: User {} rated the Tech", frameworkId, user.get().getId());
                return FrameworkController.redirectToFramework(frameworkId, frameworkVote.getCategory());
            }

            LOGGER.error("Tech {}: Unauthorized user tried to rate the Tech", frameworkId);
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: Requested for rating and not found", frameworkId);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping("/mod/quit")
    public ModelAndView QuitMod(@RequestParam("fId") final long fId,@RequestParam("category") final String cat) {
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(user.isPresent()){
            if (us.quitModdingFromTech(user.get(), fId)) {
                LOGGER.info("Tech {}: User {} is no longer a Mod", fId, user.get().getId());
                return FrameworkController.redirectToFramework(fId, cat);
            }

            LOGGER.error("Tech {}: User {} tried to quit from being a Mod without previously being one", fId, user.get().getId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: Unauthorized user tried to quit modding", fId);
        return ErrorController.redirectToErrorView();
    }


    @RequestMapping(path={"/content"}, method = RequestMethod.POST)
    public ModelAndView addContent(@Valid @ModelAttribute("contentForm") final ContentForm form, final BindingResult errors, final RedirectAttributes redirectAttributes){

        long frameworkId = form.getFrameworkId();
        final Optional<Framework> framework = fs.findById(frameworkId);

        if (framework.isPresent()) {
            if(errors.hasErrors()){
                LOGGER.info("Tech {}: Content Form has errors", frameworkId);
                final ModelAndView framework1 = framework(frameworkId, framework.get().getCategory().name(), Long.valueOf(START_PAGE), Long.valueOf(START_PAGE), Long.valueOf(START_PAGE), Long.valueOf(START_PAGE));

                loadForms(framework1);
                framework1.addObject("contentFormError", true);
                framework1.addObject("contentForm", form);

                return framework1;
            }

            redirectAttributes.addFlashAttribute("contentFormError",false);

            redirectAttributes.addFlashAttribute("contentFormMessage",messageSource.getMessage("tech.content.pending", new Object[]{},LocaleContextHolder.getLocale()));

            ContentTypes type = ContentTypes.valueOf(form.getType());

            final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

            if (user.isPresent()) {
                String pathToContent = form.getLink();
                if( !pathToContent.contains("://")){
                    pathToContent = "http://".concat(pathToContent);
                }
                contentService.insertContent(frameworkId, user.get().getId(), form.getTitle(), pathToContent, type);

                LOGGER.info("Tech {}: User {} inserted new content", frameworkId, user.get().getId());
                return FrameworkController.redirectToFramework(frameworkId, framework.get().getCategory().name());
            }

            LOGGER.error("Tech {}: Unauthorized user tried to insert content", frameworkId);
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: requested for inserting content and not found", frameworkId);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/content/delete"}, method = RequestMethod.POST)
    public ModelAndView deleteComment(@Valid @ModelAttribute("deleteContentForm") final DeleteContentForm form, final BindingResult errors){

        final Optional<Framework> framework = fs.findById(form.getDeleteContentFrameworkId());
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent()) {
            if (user.isPresent()) {
                contentService.deleteContent(form.getDeleteContentId());
                LOGGER.info("Tech {}: User {} deleted content {}", form.getDeleteContentFrameworkId(), user.get().getId(), form.getDeleteContentId());
                return FrameworkController.redirectToFramework(form.getDeleteContentFrameworkId(), framework.get().getCategory().name());
            }
            LOGGER.error("Tech {}: Unauthorized user tried to delete content", form.getDeleteContentFrameworkId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: requested for deleting content and not found", form.getDeleteContentFrameworkId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/content/report"}, method = RequestMethod.POST)
    public ModelAndView reportContent(@Valid @ModelAttribute("reportForm")ReportForm form, final BindingResult errors, HttpServletRequest request){

        final Optional<Framework> framework = fs.findById(form.getReportFrameworkId());
        final Optional<User> user= us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent()) {
            if (user.isPresent()) {
                contentService.addReport(form.getId(), user.get().getId(), form.getDescription());
                LOGGER.info("Tech {}: User {} reported content {}", form.getReportFrameworkId(), user.get().getId(), form.getId());
                return FrameworkController.redirectToFramework(form.getReportFrameworkId(), framework.get().getCategory().name());
            }

            LOGGER.error("Tech {}: Unauthorized user tried to report content", form.getReportFrameworkId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: requested for reporting content and not found", form.getReportFrameworkId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/content/report/cancel"}, method = RequestMethod.PUT)
    public ModelAndView cancelReportContent(@RequestParam("id")long reportId){
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if( user.isPresent()){
            contentService.deleteReport(reportId);
            LOGGER.info("Report {}: User {} cancelled content report", reportId, user.get().getId());
            return UserController.redirectToModView();
        }

        LOGGER.error("Report {}: Unauthorized user tried to cancel content report", reportId);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/report/content/accept"}, method = RequestMethod.PUT)
    public ModelAndView acceptReportContent(@RequestParam("id")long reportId){
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(user.isPresent()){
            contentService.acceptReport(reportId);
            LOGGER.info("Report {}: User {} accepted content report", reportId, user.get().getId());
            return UserController.redirectToModView();
        }

        LOGGER.error("Report {}: Unauthorized user tried to accept content report", reportId);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/report/content/deny"}, method = RequestMethod.PUT)
    public ModelAndView denyReportContent(@RequestParam("id")long reportId){
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if( user.isPresent()){
            contentService.denyReport(reportId);
            LOGGER.info("Report {}: User {} denied content report", reportId, user.get().getId());
            return UserController.redirectToModView();
        }

        LOGGER.error("Report {}: Unauthorized user tried to deny content report", reportId);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/comment/report"}, method = RequestMethod.POST)
    public ModelAndView reportComment(@Valid @ModelAttribute("reportCommentForm")ReportCommentForm form, final BindingResult errors, HttpServletRequest request){

        final Optional<Framework> framework = fs.findById(form.getReportCommentFrameworkId());
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent()) {
            if(user.isPresent()) {
                commentService.addReport(form.getReportCommentId(), user.get().getId(),form.getReportCommentDescription());
                LOGGER.info("Tech {}: User {} reported comment {}", form.getReportCommentFrameworkId(), user.get().getId(), form.getReportCommentId());
                return FrameworkController.redirectToFramework(form.getReportCommentFrameworkId(), framework.get().getCategory().name());
            }

            LOGGER.error("Tech {}: Unauthorized user tried to report comment", form.getReportCommentFrameworkId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: requested for reporting comment and not found", form.getReportCommentFrameworkId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/report/comment/accept"}, method = RequestMethod.PUT)
    public ModelAndView acceptReportComment(@RequestParam("id")long reportId){
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if( user.isPresent()){
            commentService.acceptReport(reportId);
            LOGGER.info("Report {}: User {} accepted comment report", reportId, user.get().getId());
            return UserController.redirectToModView();
        }

        LOGGER.error("Report {}: Unauthorized user tried to accept comment report", reportId);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/report/comment/deny"}, method = RequestMethod.PUT)
    public ModelAndView denyReportComment(@RequestParam("id")long reportId){
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if( user.isPresent()){
            commentService.denyReport(reportId);
            LOGGER.info("Report {}: User {} denied comment report", reportId, user.get().getId());
            return UserController.redirectToModView();
        }

        LOGGER.error("Report {}: Unauthorized user tried to deny comment report", reportId);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/comment/report/cancel"}, method = RequestMethod.PUT)
    public ModelAndView cancelReportComment(@RequestParam("id")long reportId) {
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (user.isPresent()) {
            commentService.deleteReport(reportId);
            LOGGER.info("Report {}: User {} cancelled comment report", reportId, user.get().getId());
            return UserController.redirectToModView();
        }

        LOGGER.error("Report {}: Unauthorized user tried to cancel comment report", reportId);
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/comment/delete"}, method = RequestMethod.POST)
    public ModelAndView deleteComment(@Valid @ModelAttribute("deleteCommentForm") final DeleteCommentForm form, final BindingResult errors){
        Optional<Framework> framework = fs.findById(form.getCommentDeleteFrameworkId());
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent()) {
            if (user.isPresent()) {
                commentService.deleteComment(form.getCommentDeleteId());
                return FrameworkController.redirectToFramework(form.getCommentDeleteFrameworkId(), framework.get().getCategory().name());
            }

            LOGGER.error("Tech {}: Unauthorized user tried to delete comment", form.getCommentDeleteFrameworkId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: requested for deleting comment and not found", form.getCommentDeleteFrameworkId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(value = "/add_tech",  method = { RequestMethod.GET})
    public ModelAndView addTech(@ModelAttribute("frameworkForm") final FrameworkForm form) {
        ModelAndView mav = new ModelAndView("frameworks/add_tech");

        mav.addObject("categories", fs.getAllCategories());
        mav.addObject("types", fs.getAllTypes());
        mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(user.isPresent()){
            LOGGER.info("Techs: User {} requested for accessing page for adding a new Tech", user.get().getId());
            mav.addObject("user_isMod", user.get().isVerify() || user.get().isAdmin());
            return mav;
        }

        LOGGER.error("Techs: Unauthorized user tried to access page for adding a new Tech");
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(value = "/add_tech", method = { RequestMethod.POST })
    public ModelAndView createTech(@Valid @ModelAttribute("frameworkForm") final FrameworkForm form, final BindingResult errors, HttpServletRequest request) throws IOException {
        if (errors.hasErrors()) {
            LOGGER.info("Techs: Add Tech Form has errors");
            return addTech(form);
        }

        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if( user.isPresent()){
            FrameworkType type = FrameworkType.valueOf(form.getType());
            FrameworkCategories category = FrameworkCategories.valueOf(form.getCategory());
            byte[] picture = form.getPicture().getBytes();

            Optional<Framework> framework = fs.create(form.getFrameworkName(),category,form.getDescription(),form.getIntroduction(),type,user.get().getId(), picture);

            if (framework.isPresent()) {
                LOGGER.info("Techs: User {} added a new Tech with id: {}", user.get().getId(), framework.get().getId());
                return FrameworkController.redirectToFramework(framework.get().getId(), framework.get().getCategory().name());
            }

            LOGGER.error("Techs: A problem occurred while creating the new Tech");
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Techs: Unauthorized user attempted to add a new Tech");
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(value = "/update_tech",  method = { RequestMethod.GET})
    public ModelAndView updateTech(@ModelAttribute("frameworkForm") final FrameworkForm form, @RequestParam("id") final long frameworkId) {
        final Optional<Framework> framework = fs.findById(frameworkId);
        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if (framework.isPresent()) {
            if (user.isPresent()) {
                if (framework.get().getAuthor().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    form.setCategory(framework.get().getCategory().name());
                    form.setDescription(framework.get().getDescription());
                    if(form.getFrameworkName() == null)
                        form.setFrameworkName(framework.get().getName());
                    form.setIntroduction(framework.get().getIntroduction());
                    form.setFrameworkId(frameworkId);
                    form.setType(framework.get().getType().name());

                    ModelAndView mav = new ModelAndView("frameworks/update_tech");

                    mav.addObject("categories", fs.getAllCategories());
                    mav.addObject("types", fs.getAllTypes());
                    mav.addObject("user", SecurityContextHolder.getContext().getAuthentication());
                    mav.addObject("category",framework.get().getCategory());
                    mav.addObject("user_isMod", user.get().isVerify() || user.get().isAdmin());

                    LOGGER.info("Tech {}: User {} accessed the page for updating the Tech", form.getFrameworkId(), user.get().getId());
                    return mav;
                }

                LOGGER.error("Tech {}: User without enough privileges attempted to access page for updating", form.getFrameworkId());
                return ErrorController.redirectToErrorView();
            }

            LOGGER.error("Tech {}: Unauthorized user attempted to access page for updating", form.getFrameworkId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: Requested for getting update page and not found", form.getFrameworkId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(value = "/update_tech", method = { RequestMethod.POST })
    public ModelAndView updateTech(@Valid @ModelAttribute("frameworkForm") final FrameworkForm form, final BindingResult errors, HttpServletRequest request) throws IOException {
        if (errors.hasErrors()) {
            LOGGER.info("Tech {}: Update Form has errors", form.getFrameworkId());
            return updateTech(form,form.getFrameworkId());
        }

        final Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        final Optional<Framework> framework = fs.findById(form.getFrameworkId());

        if(framework.isPresent()) {
            if (user.isPresent()) {
                if (framework.get().getAuthor().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    FrameworkType type = FrameworkType.valueOf(form.getType());
                    FrameworkCategories category = FrameworkCategories.valueOf(form.getCategory());
                    byte[] picture = form.getPicture().getBytes();
                    final Optional<Framework> updatedFramework = fs.update(form.getFrameworkId(),form.getFrameworkName(),category,form.getDescription(),form.getIntroduction(),type,picture);

                    if (updatedFramework.isPresent()) {
                        LOGGER.info("Tech {}: User {} updated the Tech", form.getFrameworkId(), user.get().getId());
                        return FrameworkController.redirectToFramework(framework.get().getId(), framework.get().getCategory().name());
                    }

                    LOGGER.error("Tech {}: A problem occurred while updating the Tech", form.getFrameworkId());
                    return ErrorController.redirectToErrorView();
                }

                LOGGER.error("Tech {}: User without enough privileges attempted to update tech", form.getFrameworkId());
                return ErrorController.redirectToErrorView();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to update Tech", form.getFrameworkId());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: Requested for updating tech and not found", form.getFrameworkId());
        return ErrorController.redirectToErrorView();
    }

    @RequestMapping(path={"/delete_tech"}, method = RequestMethod.POST)
    public ModelAndView deleteFramework(@Valid @ModelAttribute("deleteFrameworkForm") final DeleteFrameworkForm form, final BindingResult errors){
        Optional<Framework> framework = fs.findById(form.getFrameworkIdx());
        Optional<User> user = us.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        if(framework.isPresent()) {
            if (user.isPresent()) {
                if (framework.get().getAuthor().getUsername().equals(user.get().getUsername()) || user.get().isAdmin()) {
                    fs.delete(form.getFrameworkIdx());

                    LOGGER.info("Techs: Tech {} deleted successfully", form.getFrameworkIdx());
                    return new ModelAndView("redirect:/" + "techs");
                }

                LOGGER.error("Tech {}: User without enough privileges attempted to delete the Tech", form.getFrameworkIdx());
                return ErrorController.redirectToErrorView();
            }

            LOGGER.error("Tech {}: Unauthorized user tried to delete the Tech", form.getFrameworkIdx());
            return ErrorController.redirectToErrorView();
        }

        LOGGER.error("Tech {}: Requested for deleting Tech and not found", form.getFrameworkIdx());
        return ErrorController.redirectToErrorView();
    }
}

