package dgmp.sigrh.agentmodule.model.dtos.validators;

import dgmp.sigrh.agentmodule.controller.repositories.AgentDAO;
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
@Constraint(validatedBy = {UniqueNumBadge.UniqueNumBadgeValidator.class, UniqueNumBadge.UniqueNumBadgeValidatorOnUpdate.class})
public @interface UniqueNumBadge
{
    String message() default "numBadge:Ce numéro de badge est déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueNumBadgeValidator implements ConstraintValidator<UniqueNumBadge, String>
    {
        private final AgentDAO agentDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return !agentDAO.existsByNumBadge(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueNumBadgeValidatorOnUpdate implements ConstraintValidator<UniqueNumBadge, UpdateAgentDTO>
    {
        private final AgentDAO agentDAO;
        @Override
        public boolean isValid(UpdateAgentDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getNumBadge()==null) return false;
            return !agentDAO.existsByNumBadge(dto.getNumBadge(), dto.getIdAgent());
        }
    }
}
