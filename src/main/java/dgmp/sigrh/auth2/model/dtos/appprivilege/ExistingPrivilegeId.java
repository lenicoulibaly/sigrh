package dgmp.sigrh.auth2.model.dtos.appprivilege;

import dgmp.sigrh.auth2.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingPrivilegeId.ExistingPrivilegeIdValidator.class})
@Documented
public @interface ExistingPrivilegeId
{
    String message() default "Invalid privilegeId";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingPrivilegeIdValidator implements ConstraintValidator<ExistingPrivilegeId, Long>
    {
        private final PrvRepo prvRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context) {
            return prvRepo.existsById(value);
        }
    }
}
