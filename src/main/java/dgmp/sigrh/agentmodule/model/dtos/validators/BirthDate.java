package dgmp.sigrh.agentmodule.model.dtos.validators;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BirthDate.BirthDateValidator.class})
public @interface BirthDate
{
    String message() default "L'agent doit avoir un age compris entre 15 et 70 ans";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};

    class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate>
    {
        @Override
        public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
            if(value == null) return false;
            return LocalDate.now().minusYears(15).isAfter(value) && LocalDate.now().minusYears(70).isBefore(value);
        }
    }
}
