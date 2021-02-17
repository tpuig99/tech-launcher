package ar.edu.itba.paw.webapp.form.posts;

import ar.edu.itba.paw.webapp.form.register.PasswordForm;
import ar.edu.itba.paw.webapp.form.register.UserForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class NotEmptyTagsValidator implements ConstraintValidator<NotEmptyTags, Object> {
    @Override
    public void initialize(NotEmptyTags constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){

       AddPostForm postForm = (AddPostForm) obj;
       boolean categories = (postForm.getCategories() != null && !postForm.getCategories().isEmpty());
       boolean types = (postForm.getTypes() != null && !postForm.getTypes().isEmpty());
       boolean names = (postForm.getNames() != null && !postForm.getNames().isEmpty());
       return  categories || types || names ;

    }
}
