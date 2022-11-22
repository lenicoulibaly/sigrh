package dgmp.sigrh.auth2.model.dtos.appuser;

import dgmp.sigrh.auth2.controller.repositories.AccountTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidToken.ValidTokenValidator.class, ValidToken.ValidTokenValidatorGlobal.class})
@Documented
public @interface ValidToken
{
    String message() default "Lien invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidTokenValidator implements ConstraintValidator<ValidToken, String>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            return value == null ? false : tokenRepo.existsByToken(value);
        }
    }

    @Component @RequiredArgsConstructor
    class ValidTokenValidatorGlobal implements ConstraintValidator <ValidToken, ActivateAccountDTO>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(ActivateAccountDTO dto, ConstraintValidatorContext context)
        {
            return tokenRepo.existsByTokenAndUserId(dto.getActivationToken(), dto.getUserId());
        }
    }
}


