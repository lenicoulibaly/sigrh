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
@Constraint(validatedBy = {NoneExistingTel.NoneExistingTelValidatorOnCreate.class, NoneExistingTel.NoneExistingTelValidatorOnUpdate.class})
@Documented
public @interface NoneExistingTel
{
    String message() default "tel:N° téléphone déjà attribué";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingTelValidatorOnCreate implements ConstraintValidator<NoneExistingTel, String>
    {
        private final UserDAO userDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !userDAO.alreadyExistsByTel(value);
        }
    }

    @Component @RequiredArgsConstructor
    class NoneExistingTelValidatorOnUpdate implements ConstraintValidator<NoneExistingTel, UpdateUserDTO>
    {
        private final UserDAO userDAO;
        @Override
        public boolean isValid(UpdateUserDTO dto, ConstraintValidatorContext context) {
            return !userDAO.alreadyExistsByTel(dto.getTel(), dto.getUserId());
        }
    }
}


