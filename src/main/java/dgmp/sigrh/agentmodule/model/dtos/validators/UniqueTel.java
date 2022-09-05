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
@Constraint(validatedBy = {UniqueTel.UniqueTelValidator.class, UniqueTel.UniqueTelValidatorOnUpdate.class})
public @interface UniqueTel
{
    String message() default "tel:Ce numéro de téléphone est déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueTelValidator implements ConstraintValidator<UniqueTel, String>
    {
        private final AgentDAO agentDAO;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return !agentDAO.existsByTel(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueTelValidatorOnUpdate implements ConstraintValidator<UniqueTel, UpdateAgentDTO>
    {
        private final AgentDAO agentDAO;
        @Override
        public boolean isValid(UpdateAgentDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getTel()==null) return false;
            return !agentDAO.existsByTel(dto.getTel(), dto.getIdAgent());
        }
    }
}
