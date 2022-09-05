package dgmp.sigrh.structuremodule.model.dtos;

import dgmp.sigrh.structuremodule.controller.repositories.StructureDAO;
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
    Class<?> [] group() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingStrIdValidator implements ConstraintValidator<ExistingStrId, Long>
    {
        private final StructureDAO strDAO;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return strDAO.existsById(value);
        }
    }
}