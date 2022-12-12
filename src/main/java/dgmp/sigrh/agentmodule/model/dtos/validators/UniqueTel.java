package dgmp.sigrh.agentmodule.model.dtos.validators;

import dgmp.sigrh.agentmodule.controller.repositories.AgentRepo;
import dgmp.sigrh.agentmodule.model.dtos.UpdateAgentDTO;
import dgmp.sigrh.auth2.controller.repositories.UserRepo;
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
    String message() default "Ce numéro de téléphone est déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueTelValidator implements ConstraintValidator<UniqueTel, String>
    {
        private final AgentRepo agentRepo;
        private final UserRepo userRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return true;
            return !(agentRepo.existsByTel(value) || userRepo.alreadyExistsByTel(value));
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueTelValidatorOnUpdate implements ConstraintValidator<UniqueTel, UpdateAgentDTO>
    {
        private final AgentRepo agentRepo;
        private final UserRepo userRepo;
        @Override
        public boolean isValid(UpdateAgentDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getTel()==null) return true;
            return !(agentRepo.existsByTel(dto.getTel(), dto.getAgentId()) || userRepo.alreadyExistsByTelAndAgtId(dto.getTel(), dto.getAgentId()));
        }
    }
}
