package dgmp.sigrh.auth2.model.dtos.appuser;

import dgmp.sigrh.auth2.controller.repositories.AccountTokenRepo;
import dgmp.sigrh.auth2.model.entities.AccountToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDateTime;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneExpiredToken.ValidTokenValidator.class})
@Documented
public @interface NoneExpiredToken
{
    String message() default "Le lien a expiré";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class ValidTokenValidator implements ConstraintValidator<NoneExpiredToken, String>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            AccountToken token = tokenRepo.findByToken(value).orElse(null);
            return value == null ? false : token == null ? false : token.getExpirationDate().isAfter(LocalDateTime.now()) || token.getExpirationDate().equals(LocalDateTime.now());
        }
    }
}


