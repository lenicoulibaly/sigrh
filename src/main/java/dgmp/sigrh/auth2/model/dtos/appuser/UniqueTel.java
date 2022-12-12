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
@Constraint(validatedBy = {UniqueTel.NoneExistingTelValidatorOnCreate.class, UniqueTel.NoneExistingTelValidatorOnUpdate.class})
@Documented
public @interface UniqueTel
{
    String message() default "N° téléphone déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingTelValidatorOnCreate implements ConstraintValidator<UniqueTel, String>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return !userRepo.alreadyExistsByTel(value);
        }
    }

    @Component @RequiredArgsConstructor
    class NoneExistingTelValidatorOnUpdate implements ConstraintValidator<UniqueTel, UpdateUserDTO>
    {
        private final UserRepo userRepo;
        @Override
        public boolean isValid(UpdateUserDTO dto, ConstraintValidatorContext context) {
            return !userRepo.alreadyExistsByTel(dto.getTel(), dto.getUserId());
        }
    }
}


