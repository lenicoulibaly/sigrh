package dgmp.sigrh.fonctionmodule.model.dtos;

import dgmp.sigrh.fonctionmodule.controller.repositories.FonctionRepo;
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
    class ExistingFonctionIdValidator implements ConstraintValidator<ExistingFonctionId, Long>
    {
        private final FonctionRepo fonctionRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return fonctionRepo.existsById(value);
        }
    }
}
