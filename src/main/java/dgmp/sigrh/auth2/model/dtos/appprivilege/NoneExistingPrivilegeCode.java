package dgmp.sigrh.auth2.model.dtos.appprivilege;

import dgmp.sigrh.auth2.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneExistingPrivilegeCode.NoneExistingPrivilegeCodeValidator.class})
@Documented
public @interface NoneExistingPrivilegeCode
{
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneExistingPrivilegeCodeValidator implements ConstraintValidator<NoneExistingPrivilegeCode, String>
    {
        private final PrvRepo prvDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return !prvDAO.alreadyExistsByCode(value);
        }
    }
}
