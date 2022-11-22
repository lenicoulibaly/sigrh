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
@Constraint(validatedBy = {UniqueUsername.UniqueUsernameValidatorOnCreate.class,
        UniqueUsername.UniqueUsernameValidatorOnUpdate.class,
        UniqueUsername.UniqueUsernameValidatorOnActivation.class})
@Documented
public @interface UniqueUsername
{
    String message() default "Login déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueUsernameValidatorOnCreate implements ConstraintValidator<UniqueUsername, String>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !userRepo.alreadyExistsByUsername(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueUsernameValidatorOnUpdate implements ConstraintValidator<UniqueUsername, UpdateUserDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(UpdateUserDTO dto, ConstraintValidatorContext context)
        {
            return !userRepo.alreadyExistsByUsername(dto.getUsername(), dto.getUserId());
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueUsernameValidatorOnActivation implements ConstraintValidator <UniqueUsername, ActivateAccountDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(ActivateAccountDTO dto, ConstraintValidatorContext context)
        {
            return !userRepo.alreadyExistsByUsernameAndToken(dto.getUserId(), dto.getActivationToken());
        }
    }
}


