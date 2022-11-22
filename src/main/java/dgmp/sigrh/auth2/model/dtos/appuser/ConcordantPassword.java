package dgmp.sigrh.auth2.model.dtos.appuser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConcordantPassword.ConcordantPasswordValidator.class})
@Documented
public @interface ConcordantPassword
{
    String message() default "confirmPassword::Le mot de passe de confirmation doit être identique au mot de passe";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ConcordantPasswordValidator implements ConstraintValidator <ConcordantPassword, ActivateAccountDTO>
    {
        @Override
        public boolean isValid(ActivateAccountDTO dto, ConstraintValidatorContext context)
        {
            return dto.getPassword() == null || dto.getConfirmPassword() == null ? false : dto.getPassword().equals(dto.getConfirmPassword());
        }
    }
}


