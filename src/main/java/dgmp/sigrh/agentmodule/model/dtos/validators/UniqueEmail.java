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
@Constraint(validatedBy = {UniqueEmail.UniqueEmailValidator.class, UniqueEmail.UniqueEmailValidatorOnUpdate.class})
public @interface UniqueEmail
{
    String message() default "Cette adresse mail est déjà attribuée";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component @RequiredArgsConstructor
    class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String>
    {
        private final AgentRepo agentRepo;
        private final UserRepo userRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return true;
            if(value.trim().equals("")) return true;
            return !(agentRepo.existsByEmail(value) || agentRepo.existsByEmailPro(value) || userRepo.alreadyExistsByEmail(value));
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueEmailValidatorOnUpdate implements ConstraintValidator<UniqueEmail, UpdateAgentDTO>
    {
        private final AgentRepo agentRepo; private final UserRepo userRepo;
        @Override
        public boolean isValid(UpdateAgentDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getEmail()==null) return true;
            if(dto.getEmail().trim().equals("")) return true;
            return !(agentRepo.existsByEmail(dto.getEmail(), dto.getAgentId()) ||
                    agentRepo.existsByEmailPro(dto.getEmailPro(), dto.getAgentId()) ||
                    userRepo.alreadyExistsByEmail(dto.getEmail(), agentRepo.getUserId(dto.getAgentId())) ||
                    userRepo.alreadyExistsByEmail(dto.getEmailPro(), agentRepo.getUserId(dto.getAgentId())));
        }
    }
}
