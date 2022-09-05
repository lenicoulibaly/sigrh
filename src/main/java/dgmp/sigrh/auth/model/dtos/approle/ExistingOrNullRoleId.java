package dgmp.sigrh.auth.model.dtos.approle;

import dgmp.sigrh.auth.controller.repositories.RoleDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingOrNullRoleId.ExistingOrNullRoleIdValidator.class})
@Documented
public @interface ExistingOrNullRoleId
{
    String message() default "Invalid roleId";
    Class<?> [] group() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullRoleIdValidator implements ConstraintValidator<ExistingOrNullRoleId, Long>
    {
        private final RoleDAO roleDAO;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            return roleDAO.existsById(value) || value==null;
        }
    }
}

