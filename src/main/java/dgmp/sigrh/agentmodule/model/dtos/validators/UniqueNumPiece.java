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
@Constraint(validatedBy = {UniqueNumPiece.UniqueNumPieceValidator.class, UniqueNumPiece.UniqueNumPieceValidatorOnUpdate.class})
public @interface UniqueNumPiece
{
    String message() default "numPiece:Ce numéro de pièce est déjà attribué";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    @RequiredArgsConstructor
    class UniqueNumPieceValidator implements ConstraintValidator<UniqueNumPiece, String>
    {
        private final AgentRepo agentRepo;
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context)
        {
            if(value==null) return false;
            return !agentRepo.existsByNumPiece(value);
        }
    }

    @Component @RequiredArgsConstructor
    class UniqueNumPieceValidatorOnUpdate implements ConstraintValidator<UniqueNumPiece, UpdateAgentDTO>
    {
        private final AgentRepo agentRepo;
        @Override
        public boolean isValid(UpdateAgentDTO dto, ConstraintValidatorContext context)
        {
            if(dto.getNumPiece()==null) return false;
            return !agentRepo.existsByNumPiece(dto.getNumPiece(), dto.getIdAgent());
        }
    }
}
