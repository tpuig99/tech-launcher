package ar.edu.itba.paw.webapp.dto.custom_validators;

import ar.edu.itba.paw.service.FrameworkService;
import ar.edu.itba.paw.webapp.dto.custom_constraints.Framework;
import ar.edu.itba.paw.webapp.dto.validatedDTOs.ValidatedFrameworkDTO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FrameworkValidator implements ConstraintValidator<Framework, ValidatedFrameworkDTO> {

    @Autowired
    private FrameworkService frameworkService;

    private static final int MAX_FILE_SIZE = 1024 * 1024;

    @Override
    public void initialize(Framework constraintAnnotation) {

    }

    @Override
    public boolean isValid(ValidatedFrameworkDTO dto, ConstraintValidatorContext context) {
        boolean firstCondition = false, secondCondition = false;
        if (dto.getName() != null && dto.getName().length() > 0) {

            // If it's a creation
            if (dto.getId() == null) {
                firstCondition = frameworkService.nameAvailable(dto.getName(), null);
                // secondCondition = (dto.getPicture() != null && !dto.getPicture().isEmpty() && dto.getPicture().getSize() < (MAX_FILE_SIZE));
            } else {
                // Else, it's an update
                firstCondition = frameworkService.nameAvailable(dto.getName(), dto.getId());
                // File can be null, but if it's not if should be less than MAX_FILE_SIZE
                // secondCondition = dto.getPicture() == null || dto.getPicture().getSize() < (MAX_FILE_SIZE);
            }
        }

        return firstCondition /*&& secondCondition*/;
    }
}
