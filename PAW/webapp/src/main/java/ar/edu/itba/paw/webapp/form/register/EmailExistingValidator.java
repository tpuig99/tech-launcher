package ar.edu.itba.paw.webapp.form.register;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

class EmailExistingValidator implements ConstraintValidator<ExistingEmail, String> {
   @Autowired
    UserService us;

    @Override
    public boolean isValid(String mail, ConstraintValidatorContext context){
        Optional<User> user = us.findByMail(mail);
            return user.isPresent();
    }

    @Override
    public void initialize(ExistingEmail existingEmail) {

    }

}
