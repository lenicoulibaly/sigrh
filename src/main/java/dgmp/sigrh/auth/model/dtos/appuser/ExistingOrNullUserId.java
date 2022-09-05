package dgmp.sigrh.auth.model.dtos.appuser;

import dgmp.sigrh.auth.controller.repositories.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingOrNullUserId.ExistingOrNullUserIdValidator.class})
@Documented
public @interface ExistingOrNullUserId
{
    String message() default "Invalid userId";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullUserIdValidator implements ConstraintValidator<ExistingOrNullUserId, Long>
    {
        private final UserDAO userDAO;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            return userDAO.existsById(value) || value == null;
        }
    }
}


