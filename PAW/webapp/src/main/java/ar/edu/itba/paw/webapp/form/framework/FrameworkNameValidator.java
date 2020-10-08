package ar.edu.itba.paw.webapp.form.framework;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.service.FrameworkService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

class FrameworkNameValidator implements ConstraintValidator<FrameworkName, Object> {
    @Autowired
    FrameworkService fs;

    @Override
    public void initialize(FrameworkName frameworkUpdate) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
            FrameworkForm form = (FrameworkForm) obj;
            List<Framework> framework = fs.search(form.getFrameworkName(),null,null,0,5,true,0,null,null);
            if(form.getFrameworkId()==null)
                return framework.isEmpty();
            if(framework.isEmpty())
                return true;
            return framework.get(0).getId() == form.getFrameworkId();
    }
}
