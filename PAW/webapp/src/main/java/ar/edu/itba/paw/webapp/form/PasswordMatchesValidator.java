package ar.edu.itba.paw.webapp.form;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserForm user = (UserForm) obj;
        return user.getPassword().equals(user.getRepeatPassword());
    }
}
