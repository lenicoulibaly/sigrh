package dgmp.sigrh.structuremodule.model.dtos.str;

import dgmp.sigrh.structuremodule.controller.repositories.structure.StrRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingStrId.ExistingStrIdValidator.class})
@Documented
public @interface ExistingStrId
{
    String message() default "Invalid Structure Id";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingStrIdValidator implements ConstraintValidator<ExistingStrId, Long>
    {
        private final StrRepo strDAO;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return strDAO.existsById(value);
        }
    }
}