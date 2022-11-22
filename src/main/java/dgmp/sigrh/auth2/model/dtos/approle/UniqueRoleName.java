package dgmp.sigrh.auth2.model.dtos.approle;

import dgmp.sigrh.auth2.controller.repositories.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueRoleName.UniqueRoleNameValidatorOnCreate.class})
@Documented
public @interface UniqueRoleName
{
    String message() default "Rôle déjà créé";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueRoleNameValidatorOnCreate implements ConstraintValidator<UniqueRoleName, String>
    {
        private final RoleRepo roleRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return !roleRepo.alreadyExistsByName(value);
        }
    }
}


