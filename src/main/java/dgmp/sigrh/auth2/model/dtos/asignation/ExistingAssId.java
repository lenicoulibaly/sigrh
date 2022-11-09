package dgmp.sigrh.auth2.model.dtos.asignation;

import dgmp.sigrh.auth2.controller.repositories.PrincipalAssRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ExistingAssId.ExistingAssIdValidator.class})
public @interface ExistingAssId
{
    String message() default "Identitifiant de l'assignation invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingAssIdValidator implements ConstraintValidator<ExistingAssId, Long>
    {
        private final PrincipalAssRepo assRepo;
        @Override
        public boolean isValid(Long assId, ConstraintValidatorContext context)
        {
            return assRepo.existsById(assId);
        }
    }
}
