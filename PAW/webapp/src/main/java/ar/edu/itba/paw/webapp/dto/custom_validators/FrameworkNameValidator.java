package ar.edu.itba.paw.webapp.dto.custom_validators;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.webapp.dto.custom_constraints.FrameworkName;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

class FrameworkNameValidator implements ConstraintValidator<FrameworkName, Object> {
    @Autowired
    private FrameworkService fs;

    @Override
    public void initialize(FrameworkName frameworkUpdate) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        FrameworkForm form = (FrameworkForm) obj;
        Optional<Framework> framework = fs.getByName(form.getFrameworkName());
        if(form.getFrameworkId()==null)
            return !framework.isPresent();
        if(!framework.isPresent())
            return true;
        return framework.get().getId() == form.getFrameworkId();
    }
}
