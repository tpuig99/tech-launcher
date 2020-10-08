package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.service.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MultipartValidator implements ConstraintValidator<Multipart, Object>  {
    @Autowired
    FrameworkService fs;

    @Override
    public void initialize(Multipart frameworkName) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        FrameworkForm form = (FrameworkForm) obj;
        if(form.getFrameworkId()==null){
            return !form.getPicture().isEmpty();
        }
        return true;
    }
}
