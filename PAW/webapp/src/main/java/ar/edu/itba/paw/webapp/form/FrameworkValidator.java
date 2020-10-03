package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.service.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class FrameworkValidator implements ConstraintValidator<FrameworkName, String>  {
    @Autowired
    FrameworkService fs;

    @Override
    public void initialize(FrameworkName frameworkName) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        List<Framework> ls = fs.search(s,null,null,null,true);
        return ls.isEmpty();
    }
}
