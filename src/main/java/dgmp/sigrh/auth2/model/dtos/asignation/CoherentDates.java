package dgmp.sigrh.auth2.model.dtos.asignation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {CoherentDates.CoherentDatesValidatorOnCreate.class, CoherentDates.CoherentDatesValidatorOnUpdate.class})
public @interface CoherentDates
{
    String message() default "dates::La date de début ne peut être ultérieure à la date de fin";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnCreate implements ConstraintValidator<CoherentDates, CreatePrincipalAssDTO>
    {
        @Override
        public boolean isValid(CreatePrincipalAssDTO dto, ConstraintValidatorContext context)
        {
            return dto.getStartsAt() == null || dto.getEndsAt() == null ? true : dto.getStartsAt().isBefore(dto.getEndsAt());
        }
    }

    @Component @RequiredArgsConstructor
    class CoherentDatesValidatorOnUpdate implements ConstraintValidator<CoherentDates, UpdatePrincipalAssDTO>
    {
        @Override
        public boolean isValid(UpdatePrincipalAssDTO dto, ConstraintValidatorContext context)
        {
            return dto.getStartsAt() == null || dto.getEndsAt() == null ? true : dto.getStartsAt().isBefore(dto.getEndsAt());
        }
    }
}
