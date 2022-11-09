package dgmp.sigrh.auth2.model.dtos.appuser;

import dgmp.sigrh.auth2.controller.repositories.UserRepo;
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
        private final UserRepo userRepo;
        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return userRepo.existsById(value);
        }
    }

}


