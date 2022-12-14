package dgmp.sigrh.agentmodule.model.dtos.validators;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.agentmodule.model.dtos.UpdateAgentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueFixeBureau.UniqueFixeBureauValidator.class, UniqueFixeBureau.UniqueFixeBureauValidatorOnUpdate.class})
public @interface UniqueFixeBureau
{
    String message() default "fixeBureau:Ce numéro de post fixe est déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueFixeBureauValidator implements ConstraintValidator<UniqueFixeBureau, String>
    {
        private final AgentRepo agentRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return true;
            if(value.trim().equals("")) return true;
            return !agentRepo.existsByFixeBureau(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueFixeBureauValidatorOnUpdate implements ConstraintValidator<UniqueFixeBureau, UpdateAgentDTO>
    {
        private final AgentRepo agentRepo;
        @Override
        public boolean isValid(UpdateAgentDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getFixeBureau()==null) return false;
            return !agentRepo.existsByFixeBureau(dto.getFixeBureau(), dto.getAgentId());
        }
    }
}
