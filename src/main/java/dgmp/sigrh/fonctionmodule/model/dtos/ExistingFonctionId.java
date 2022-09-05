package dgmp.sigrh.fonctionmodule.model.dtos;

import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingFonctionId.ExistingFonctionIdValidator.class})
public @interface ExistingFonctionId
{
    String message() default "Cette fonction est inexistante";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    public class ExistingFonctionIdValidator implements ConstraintValidator<ExistingFonctionId, Long>
    {
        private final FonctionDAO fonctionDAO;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return fonctionDAO.existsById(value);
        }
    }
}
