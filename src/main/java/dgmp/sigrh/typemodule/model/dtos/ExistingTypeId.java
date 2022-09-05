package dgmp.sigrh.typemodule.model.dtos;

import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingTypeId.ExistingTypeIdValidator.class})
@Documented
public @interface ExistingTypeId
{
    String message() default "L'ID du type à modifier est inexistant";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
    @Component
    @RequiredArgsConstructor
    class ExistingTypeIdValidator implements ConstraintValidator<ExistingTypeId, Long>
    {
        private final TypeRepo typeRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            return typeRepo.existsById(value);
        }
    }
}


