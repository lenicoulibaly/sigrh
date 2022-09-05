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
@Constraint(validatedBy = {NoneExistingUserName.NoneExistingUserNameValidatorOnCreate.class, NoneExistingUserName.NoneExistingUserNameValidatorOnUpdate.class})
@Documented
public @interface NoneExistingUserName
{
    String message() default "username:Username déjà attribué";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingUserNameValidatorOnCreate implements ConstraintValidator<NoneExistingUserName, String>
    {
        private final UserDAO userDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !userDAO.alreadyExistsByUsername(value);
        }
    }

    @Component @RequiredArgsConstructor
    class NoneExistingUserNameValidatorOnUpdate implements ConstraintValidator<NoneExistingUserName, UpdateUserDTO>
    {
        private final UserDAO userDAO;
        @Override
        public boolean isValid(UpdateUserDTO dto, ConstraintValidatorContext context)
        {
            return !userDAO.alreadyExistsByUsername(dto.getUsername(), dto.getUserId());
        }
    }
}


