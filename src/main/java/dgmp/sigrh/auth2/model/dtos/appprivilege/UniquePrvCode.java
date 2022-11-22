package dgmp.sigrh.auth2.model.dtos.appprivilege;

import dgmp.sigrh.auth2.controller.repositories.PrvRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniquePrvCode.UniqueRoleNameValidatorOnCreate.class})
@Documented
public @interface UniquePrvCode
{
    String message() default "Code de privilège déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnCreate implements ConstraintValidator<UniquePrvCode, String>
    {
        private final PrvRepo prvRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !prvRepo.alreadyExistsByCode(value);
        }
    }
}


