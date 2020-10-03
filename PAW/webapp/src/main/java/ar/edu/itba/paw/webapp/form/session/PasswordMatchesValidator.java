package ar.edu.itba.paw.webapp.form.session;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        if(obj instanceof UserForm){
            UserForm user = (UserForm) obj;
            return user.getPassword().equals(user.getRepeatPassword());
        }
            PasswordForm user = (PasswordForm) obj;
            return user.getPassword().equals(user.getRepeatPassword());
    }
}
