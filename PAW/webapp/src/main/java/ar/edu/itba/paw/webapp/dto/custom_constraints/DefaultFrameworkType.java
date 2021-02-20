package ar.edu.itba.paw.webapp.dto.custom_constraints;

import ar.edu.itba.paw.webapp.dto.custom_validators.DefaultContentTypeValidator;
import ar.edu.itba.paw.webapp.dto.custom_validators.DefaultFrameworkTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DefaultFrameworkTypeValidator.class)
@Documented
public @interface DefaultFrameworkType {
    String message() default "Invalid tech type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
