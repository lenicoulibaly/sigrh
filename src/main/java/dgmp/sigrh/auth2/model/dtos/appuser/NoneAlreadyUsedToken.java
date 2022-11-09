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

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NoneAlreadyUsedToken.NoneAlreadyUsedTokenValidator.class})
@Documented
public @interface NoneAlreadyUsedToken
{
    String message() default "Le lien a déjà été utilisé";
    Class<?>[] group() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class NoneAlreadyUsedTokenValidator implements ConstraintValidator<NoneAlreadyUsedToken, String>
    {
        private final AccountTokenRepo tokenRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            AccountToken token = tokenRepo.findByToken(value).orElse(null);
            return value == null ? false : token == null ? false : !token.isAlreadyUsed();
        }
    }
}


