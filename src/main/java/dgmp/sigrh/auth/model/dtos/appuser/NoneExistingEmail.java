package dgmp.sigrh.auth.model.dtos.appuser;

import dgmp.sigrh.auth.controller.repositories.UserDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneExistingEmail.NoneExistingEmailValidatorOnCreate.class, NoneExistingEmail.NoneExistingEmailValidatorOnUpdate.class})
@Documented
public @interface NoneExistingEmail
{
    String message() default "email:Adresse mail déjà attribuée";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingEmailValidatorOnUpdate implements ConstraintValidator<NoneExistingEmail, UpdateUserDTO>
    {
        private final UserDAO userDAO;
        @Override
        public boolean isValid(UpdateUserDTO dto, ConstraintValidatorContext context) {
            return !userDAO.alreadyExistsByEmail(dto.getEmail(), dto.getUserId());
        }
    }

    @Component @RequiredArgsConstructor
    class NoneExistingEmailValidatorOnCreate implements ConstraintValidator<NoneExistingEmail, String>
    {
        private final UserDAO userDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !userDAO.alreadyExistsByEmail(value);
        }
    }
}




