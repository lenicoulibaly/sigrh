package dgmp.sigrh.auth2.model.dtos.appprivilege;

import dgmp.sigrh.auth2.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(value = {ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneExistingPrivilegeName.NoneExistingPrivilegeNameValidator.class})
@Documented
public @interface NoneExistingPrivilegeName
{
    String message() default "Ce nom de privilège est déjà utilisé";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingPrivilegeNameValidator implements ConstraintValidator<NoneExistingPrivilegeName, String>
    {
        private final PrvRepo prvDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return !prvDAO.alreadyExistsByName(value);
        }
    }
}
