package ar.edu.itba.paw.webapp.dto.custom_validators;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.service.ContentService;
import ar.edu.itba.paw.webapp.dto.custom_constraints.ContentName;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class ContentValidator implements ConstraintValidator<ContentName, ContentForm>  {
    @Autowired
    private ContentService cs;


    @Override
    public void initialize(ContentName contentName) {

    }

    @Override
    public boolean isValid(ContentForm s, ConstraintValidatorContext constraintValidatorContext) {
        List<Content> ls = cs.getContentByFrameworkAndTypeAndTitle(s.getFrameworkId(),Enum.valueOf(ContentTypes.class,s.getType()),s.getTitle());
        return ls.isEmpty();
    }

}
