package dgmp.sigrh.auth2.model.dtos.appuser;

import dgmp.sigrh.auth2.controller.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueEmail.NoneExistingEmailValidatorOnCreate.class, UniqueEmail.NoneExistingEmailValidatorOnUpdate.class})
@Documented
public @interface UniqueEmail
{
    String message() default "email:Adresse mail déjà attribuée";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingEmailValidatorOnUpdate implements ConstraintValidator<UniqueEmail, UpdateUserDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(UpdateUserDTO dto, ConstraintValidatorContext context) {
            return !userRepo.alreadyExistsByEmail(dto.getEmail(), dto.getUserId());
        }
    }

    @Component @RequiredArgsConstructor
    class NoneExistingEmailValidatorOnCreate implements ConstraintValidator<UniqueEmail, String>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !userRepo.alreadyExistsByEmail(value);
        }
    }
}




