package dgmp.sigrh.emploimodule.model.dtos;

import dgmp.sigrh.emploimodule.controller.repositories.EmploiDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingEmploiId.ExistingEmploiIdValidator.class})
public @interface ExistingEmploiId
{
    String message() default "Cet emploi n'existe pas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class ExistingEmploiIdValidator implements ConstraintValidator<ExistingEmploiId, Long>
    {
        private final EmploiDAO emploiDAO;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return emploiDAO.existsById(value);
        }
    }
}
