package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.webapp.form.ContentForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ContentController {

    @Autowired
    private FrameworkService fs;

    @Autowired
    private ContentService contentService;


    @RequestMapping(path={"/content"}, method = RequestMethod.GET)
    public ModelAndView showContentForm(@ModelAttribute("contentForm") final ContentForm form){

        return new ModelAndView("frameworks/contentForm");
    }

    @RequestMapping(path={"/content"}, method = RequestMethod.POST)
    public ModelAndView addContent(@Valid @ModelAttribute("contentForm") final ContentForm form, final BindingResult errors/*, @RequestParam("id") final long id, @RequestParam("userid") final long userId*/){

        if(errors.hasErrors()){
            return showContentForm(form);
        }

        Framework framework = fs.findById(3);
       // ContentTypes type = ContentTypes.valueOf(form.getType());

        final Content content = contentService.insertContent(3, 1, form.getTitle(), form.getLink(), ContentTypes.book, false );

        return new ModelAndView("redirect:/" + framework.getCategory() + "/"+3);
    }

        /*@RequestMapping(path={"/content"}, method = RequestMethod.GET)
    public ModelAndView uploadContent(@RequestBody Content content, @RequestParam("id") final long id){
        Framework framework = fs.findById(id);
        final Content c = contentService.insertContent(id, content.getUserId(), content.getTitle(), content.getLink(), content.getType());

        return new ModelAndView("redirect:/" + framework.getCategory() + "/"+id);
    }*/

}
