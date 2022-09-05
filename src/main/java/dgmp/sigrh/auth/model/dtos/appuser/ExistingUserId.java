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
@Constraint(validatedBy = {ExistingUserId.ExistingUserIdValidator.class})
@Documented
public @interface ExistingUserId
{
    String message() default "Invalid userId";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingUserIdValidator implements ConstraintValidator<ExistingUserId, Long>
    {
        private final UserDAO userDAO;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return userDAO.existsById(value);
        }
    }

}


