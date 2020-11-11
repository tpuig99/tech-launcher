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

       return (postForm.getCategories() != null && !postForm.getCategories().isEmpty()) || (postForm.getTypes() != null && !postForm.getTypes().isEmpty()) || (postForm.getNames() != null && !postForm.getNames().isEmpty());

    }
}
