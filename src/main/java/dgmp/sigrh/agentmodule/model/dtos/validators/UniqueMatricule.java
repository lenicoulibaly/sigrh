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
@Constraint(validatedBy = {UniqueMatricule.UniqueMatriculeValidator.class, UniqueMatricule.UniqueMatriculeValidatorOnUpdate.class})
public @interface UniqueMatricule
{
    String message() default "matricule:Ce numéro matricule est déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueMatriculeValidator implements ConstraintValidator<UniqueMatricule, String>
    {
        private final AgentRepo agentRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return !agentRepo.existsByMatricule(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueMatriculeValidatorOnUpdate implements ConstraintValidator<UniqueMatricule, UpdateAgentDTO>
    {
        private final AgentRepo agentRepo;
        @Override
        public boolean isValid(UpdateAgentDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getMatricule()==null) return false;
            return !agentRepo.existsByMatricule(dto.getMatricule(), dto.getIdAgent());
        }
    }
}
