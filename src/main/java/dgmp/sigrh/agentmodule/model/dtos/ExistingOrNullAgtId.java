package dgmp.sigrh.agentmodule.model.dtos;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ExistingOrNullAgtId.ExistingOrNullAgtIdValidator.class})
@Documented
public @interface ExistingOrNullAgtId
{
    String message() default "Invalid Structure Id";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};

    @Component @RequiredArgsConstructor
    class ExistingOrNullAgtIdValidator implements ConstraintValidator<ExistingOrNullAgtId, Long>
    {
        private final AgentRepo agentRepo;

        @Override
        public boolean isValid(Long value, ConstraintValidatorContext context)
        {
            if(value == null) return true;
            return agentRepo.existsById(value) ;
        }
    }
}
